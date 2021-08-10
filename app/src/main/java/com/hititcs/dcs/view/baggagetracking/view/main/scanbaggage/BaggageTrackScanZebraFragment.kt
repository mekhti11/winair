package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage

import android.Manifest.permission
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import com.hititcs.dcs.R
import com.hititcs.dcs.util.AnimUtils
import com.hititcs.dcs.view.BaseFragment
import com.hititcs.dcs.view.Presenter
import com.hititcs.dcs.view.baggagetracking.domain.model.ScannedTag
import com.hititcs.dcs.view.baggagetracking.view.main.LastThreeBagAdapter
import com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.BaggageTrackScanContract.BaggageTrackScanPresenter
import com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.BaggageTrackScanContract.BaggageTrackScanView
import com.symbol.emdk.EMDKManager
import com.symbol.emdk.EMDKManager.EMDKListener
import com.symbol.emdk.EMDKManager.FEATURE_TYPE.BARCODE
import com.symbol.emdk.EMDKResults.STATUS_CODE
import com.symbol.emdk.barcode.BarcodeManager
import com.symbol.emdk.barcode.BarcodeManager.ConnectionState
import com.symbol.emdk.barcode.BarcodeManager.ConnectionState.CONNECTED
import com.symbol.emdk.barcode.BarcodeManager.ConnectionState.DISCONNECTED
import com.symbol.emdk.barcode.BarcodeManager.ScannerConnectionListener
import com.symbol.emdk.barcode.ScanDataCollection
import com.symbol.emdk.barcode.Scanner
import com.symbol.emdk.barcode.Scanner.DataListener
import com.symbol.emdk.barcode.Scanner.StatusListener
import com.symbol.emdk.barcode.Scanner.TriggerType.HARD
import com.symbol.emdk.barcode.Scanner.TriggerType.SOFT_ONCE
import com.symbol.emdk.barcode.ScannerException
import com.symbol.emdk.barcode.ScannerInfo
import com.symbol.emdk.barcode.ScannerResults.SUCCESS
import com.symbol.emdk.barcode.StatusData
import com.symbol.emdk.barcode.StatusData.ScannerStates.DISABLED
import com.symbol.emdk.barcode.StatusData.ScannerStates.ERROR
import com.symbol.emdk.barcode.StatusData.ScannerStates.IDLE
import com.symbol.emdk.barcode.StatusData.ScannerStates.SCANNING
import com.symbol.emdk.barcode.StatusData.ScannerStates.WAITING
import timber.log.Timber
import java.io.Serializable
import java.util.ArrayList
import javax.inject.Inject

class BaggageTrackScanZebraFragment : BaseFragment<BaggageTrackScanZebraFragment>(),
  BaggageTrackScanView, EMDKListener, ScannerConnectionListener, DataListener, StatusListener {

  private val STATE_FLASH_OPEN = "state:flashOpen"
  private val STATE_CAMERA_PAUSE = "state:cameraPause"
  val MY_PERMISSIONS_REQUEST_CAMERA = 124
  private val SUCCESS_DELAY_BETWEEN_SCANS = 1000L
  private val ERROR_DELAY_BETWEEN_SCANS = 3000L

  private var mFlashOpen = false
  private var mCameraPause = false

  private var mOpenFlashDrawable: Drawable? = null
  private var mCloseFlashDrawable: Drawable? = null
  private var mPauseDrawable: Drawable? = null
  private var mPlayDrawable: Drawable? = null

  @Inject lateinit var presenter: BaggageTrackScanPresenter

  @BindView(R.id.img_pause) lateinit var imgPause: AppCompatImageView
  @BindView(R.id.tv_baggage_scan_close) lateinit var tvClose: TextView
  @BindView(R.id.tv_baggage_scan_flashlight) lateinit var tvFlashlight: TextView
  @BindView(R.id.ln_baggage_scan_success) lateinit var lnBaggageScanSuccess: LinearLayout
  @BindView(R.id.ln_baggage_scan_fail) lateinit var lnBaggageScanFail: LinearLayout
  @BindView(R.id.barcode_error_txt) lateinit var barcodeErrorTxt: TextView
  @BindView(R.id.rcv_last_three_items_camera) lateinit var rcvLastThree: RecyclerView
  @BindView(R.id.tv_baggage_scan_location_name) lateinit var tvLocationName: TextView
  @BindView(R.id.tv_baggage_scan_location_code) lateinit var tvLocationCode: TextView

  private var locationCode: String? = null
  private var locationName: String? = null
  var scannedTagList: MutableList<ScannedTag>? = null
  private lateinit var lastThreeBagAdapter: LastThreeBagAdapter
  private lateinit var successAudioUri: Uri
  private lateinit var failAudioUri: Uri
  private var emdkManager: EMDKManager? = null
  private var barcodeManager: BarcodeManager? = null
  private var scanner: Scanner? = null
  private var deviceList: List<ScannerInfo>? = null
  private var defaultIndex = 0
  private var bSoftTriggerSelected = false
  private var bDecoderSettingsChanged = false
  private var bExtScannerDisconnected = false
  private val lock = Any()
  private var mediaPlayer: MediaPlayer? = MediaPlayer().apply {
    setAudioAttributes(
      AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .build()
    )
  }

  companion object {
    var EXTRA_LOCATION_NAME: String =
      "EXTRA_LOCATION_NAME." + BaggageTrackScanFragment.javaClass.simpleName
    var EXTRA_LOCATION_CODE: String =
      "EXTRA_LOCATION_CODE." + BaggageTrackScanFragment.javaClass.simpleName
    var EXTRA_SCANNED_TAG_LIST: String =
      "EXTRA_SCANNED_TAG_LIST." + BaggageTrackScanFragment.javaClass.simpleName

    fun newInstance(
      locationCode: String,
      locationName: String,
      scannedTagList: MutableList<ScannedTag>?
    ): Fragment {
      val args = Bundle()
      val fragment = BaggageTrackScanFragment()
      args.putString(EXTRA_LOCATION_CODE, locationCode)
      args.putString(EXTRA_LOCATION_NAME, locationName)
      args.putSerializable(EXTRA_SCANNED_TAG_LIST, scannedTagList as Serializable)
      fragment.arguments = args
      return fragment
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    locationCode = arguments?.getString(EXTRA_LOCATION_CODE)
    locationName = arguments?.getString(EXTRA_LOCATION_NAME)
    scannedTagList =
      arguments?.getSerializable(EXTRA_SCANNED_TAG_LIST) as (MutableList<ScannedTag>?)
    val results = EMDKManager.getEMDKManager(activity!!.applicationContext, this)
    if (results.statusCode != STATUS_CODE.SUCCESS) {
      Timber.d("EMDKManager object request failed!")
      return
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    if (ContextCompat.checkSelfPermission(activity!!, permission.CAMERA)
      != PackageManager.PERMISSION_GRANTED
    ) {
      ActivityCompat
        .requestPermissions(
          activity!!, arrayOf(permission.CAMERA),
          MY_PERMISSIONS_REQUEST_CAMERA
        )
    }
    if (savedInstanceState != null) {
      mFlashOpen = savedInstanceState.getBoolean(STATE_FLASH_OPEN, false)
      mCameraPause =
        savedInstanceState.getBoolean(STATE_CAMERA_PAUSE, false)
    }
  }

  private fun setupLastThreeItems() {
    rcvLastThree.layoutManager = LinearLayoutManager(context)
    lastThreeBagAdapter = LastThreeBagAdapter(false)
    rcvLastThree.adapter = lastThreeBagAdapter
    lastThreeBagAdapter.itemList = scannedTagList
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val fragmentView = inflater.inflate(R.layout.content_baggage_track_scan_zebra, container, false)
    bindView(fragmentView)
    return fragmentView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupAudios()
    setupLastThreeItems()
    tvClose.setOnClickListener { onClickClose() }
    initTextViews()
  }

  override fun onStatus(statusData: StatusData) {
    val state = statusData.state
    var statusString = ""
    when (state) {
      IDLE -> {
        statusString = statusData.friendlyName + " is enabled and idle..."
        Timber.d(statusString)
        // set trigger type
        if (bSoftTriggerSelected) {
          scanner!!.triggerType = SOFT_ONCE
          bSoftTriggerSelected = false
        } else {
          scanner!!.triggerType = HARD
        }
        // set decoders
        if (bDecoderSettingsChanged) {
          setDecoders()
          bDecoderSettingsChanged = false
        }
        // submit read
        if (!scanner!!.isReadPending && !bExtScannerDisconnected) {
          try {
            scanner!!.read()
          } catch (e: ScannerException) {
            Timber.e(e.message)
          }
        }
      }
      WAITING -> {
        statusString = "Scanner is waiting for trigger press..."
        Timber.d(statusString)
      }
      SCANNING -> {
        statusString = "Scanning..."
        Timber.d(statusString)
      }
      DISABLED -> {
        statusString = statusData.friendlyName + " is disabled."
        Timber.d(statusString)
      }
      ERROR -> {
        statusString = "An error has occurred."
        Timber.d(statusString)
      }
      else -> {
      }
    }
  }

  private fun setDecoders() {
    if (scanner != null) {
      try {
        val config = scanner!!.config

        // Set EAN8
        config.decoderParams.ean8.enabled = true
        // Set EAN13
        config.decoderParams.ean13.enabled = true
        // Set Code39
        config.decoderParams.code39.enabled = true
        //Set Code128
        config.decoderParams.code128.enabled = true
        scanner!!.config = config
      } catch (e: ScannerException) {
        Timber.e(e.message)
      }
    }
  }

  private fun setupAudios() {
    successAudioUri = Uri.Builder()
      .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
      .authority(context().packageName)
      .path(R.raw.success.toString())
      .build()
    failAudioUri = Uri.Builder()
      .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
      .authority(context().packageName)
      .path(R.raw.error.toString())
      .build()
  }

  @OnClick(R.id.btn_scan)
  fun pressedScanBtn() {
    bSoftTriggerSelected = true
    cancelRead()
  }

  private fun cancelRead() {
    if (scanner != null) {
      if (scanner!!.isReadPending) {
        try {
          scanner!!.cancelRead()
        } catch (e: ScannerException) {
          Timber.e(e.message)
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()
    // The application is in foreground
    if (emdkManager != null) {
      // Acquire the barcode manager resources
      initBarcodeManager()
      enumerateScannerDevices()
      bSoftTriggerSelected = false
      bExtScannerDisconnected = false
      deInitScanner()
      initScanner()
    }
  }

  override fun onPause() {
    super.onPause()
    // The application is in background
    // Release the barcode manager resources
    deInitScanner()
    deInitBarcodeManager()
  }

  override fun onOpened(emdkManager: EMDKManager?) {
    this.emdkManager = emdkManager
    initBarcodeManager()
    enumerateScannerDevices()
    bSoftTriggerSelected = false
    bExtScannerDisconnected = false
    deInitScanner()
    initScanner()
  }

  private fun initBarcodeManager() {
    barcodeManager = emdkManager!!.getInstance(BARCODE) as BarcodeManager
    // Add connection listener
    if (barcodeManager != null) {
      barcodeManager!!.addConnectionListener(this)
    }
  }

  private fun deInitBarcodeManager() {
    if (emdkManager != null) {
      emdkManager!!.release(BARCODE)
    }
  }

  private fun initScanner() {
    if (scanner == null) {
      if (deviceList != null && deviceList!!.size != 0) {
        if (barcodeManager != null) {
          scanner = barcodeManager!!.getDevice(deviceList!!.get(0))
        }
      } else {
        Timber
          .w("Failed to get the specified scanner device! Please close and restart the application.")
        return
      }
      if (scanner != null) {
        scanner!!.addDataListener(this)
        scanner!!.addStatusListener(this)
        try {
          scanner!!.enable()
        } catch (e: ScannerException) {
          Timber.e(e)
          deInitScanner()
        }
      } else {
        Timber.w("Failed to initialize the scanner device.")
      }
    }
  }

  private fun enumerateScannerDevices() {
    if (barcodeManager != null) {
      val friendlyNameList: MutableList<String> = ArrayList()
      var spinnerIndex = 0
      deviceList = barcodeManager!!.getSupportedDevicesInfo()
      if (deviceList != null && deviceList!!.size != 0) {
        val it = deviceList!!.iterator()
        while (it.hasNext()) {
          val scnInfo = it.next()
          friendlyNameList.add(scnInfo.friendlyName)
          if (scnInfo.isDefaultScanner) {
            defaultIndex = spinnerIndex
          }
          ++spinnerIndex
        }
      } else {
        Timber
          .w("Failed to get the list of supported scanner devices! Please close and restart the application.")
      }
    }
  }

  private fun deInitScanner() {
    if (scanner != null) {
      try {
        scanner!!.disable()
      } catch (e: Exception) {
        Timber.e(e.message)
      }
      try {
        scanner!!.removeDataListener(this)
        scanner!!.removeStatusListener(this)
      } catch (e: Exception) {
        Timber.e(e.message)
      }
      try {
        scanner!!.release()
      } catch (e: Exception) {
        Timber.e(e.message)
      }
      scanner = null
    }
  }

  override fun onClosed() {
    if (emdkManager != null) {
      emdkManager!!.release()
      emdkManager = null
    }
  }

  override fun onConnectionChange(scannerInfo: ScannerInfo, connectionState: ConnectionState) {
    val status: String
    var scannerName = ""
    val statusExtScanner = connectionState.toString()
    val scannerNameExtScanner = scannerInfo.friendlyName
    if (deviceList!!.size != 0) {
      scannerName = deviceList!![0].friendlyName
    }
    if (scannerName.equals(scannerNameExtScanner, ignoreCase = true)) {
      when (connectionState) {
        CONNECTED -> {
          bSoftTriggerSelected = false
          synchronized(lock) {
            initScanner()
            bExtScannerDisconnected = false
          }
        }
        DISCONNECTED -> {
          bExtScannerDisconnected = true
          synchronized(lock) { deInitScanner() }
        }
      }
      status = "$scannerNameExtScanner:$statusExtScanner"
      Timber.d(status)
    } else {
      bExtScannerDisconnected = false
      Timber.d("scannerNameExtScanner + \":\" + statusExtScanner;")
    }
  }

  override fun onData(scanDataCollection: ScanDataCollection?) {
    if (scanDataCollection != null && (scanDataCollection.result
        == SUCCESS)
    ) {
      val scanData = scanDataCollection.scanData
      for (data in scanData) {
        if (!TextUtils.isEmpty(data.data)) {
          Timber.e("<font color='gray'>" + data.labelType + "</font> : " + data.data)
          presenter.scanBaggageBarcode(locationName!!, locationCode!!, data.data.takeLast(6))
          break
        }
      }
    }
  }

  private fun initTextViews() {
    tvLocationName.text =
      String.format("%s: %s", getString(R.string.baggage_track_scan_location_name), locationName)
    tvLocationCode.text =
      String.format("%s: %s", getString(R.string.baggage_track_scan_location_code), locationCode)
  }

  override fun onDestroy() {
    mediaPlayer?.release()
    mediaPlayer = null
    super.onDestroy()
  }

  private fun hasFlash(): Boolean {
    return activity!!.applicationContext.packageManager
      .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
  }

  private fun onClickClose() {
    var intent = Intent()
    intent.putExtra(EXTRA_SCANNED_TAG_LIST, scannedTagList as Serializable)
    activity?.let { activity?.setResult(Activity.RESULT_OK, intent) }
    activity?.let { activity?.finish() }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putBoolean(STATE_FLASH_OPEN, mFlashOpen)
    outState.putBoolean(STATE_CAMERA_PAUSE, mCameraPause)
    super.onSaveInstanceState(outState)
  }

  private fun showResultIcon(success: Boolean, errorMsg: String?) {
    hideProgressDialog()
    var delayTime: Long = SUCCESS_DELAY_BETWEEN_SCANS
    if (success) {
      playSuccessAudio()
      lnBaggageScanSuccess.visibility = View.VISIBLE
    } else {
      delayTime = ERROR_DELAY_BETWEEN_SCANS
      playFailAudio()
      barcodeErrorTxt.text = errorMsg
      lnBaggageScanFail.visibility = View.VISIBLE
      barcodeErrorTxt.visibility = View.VISIBLE
    }
    Handler().postDelayed(clearUiAfterBarcodeResponse(), delayTime)
  }

  private fun playSuccessAudio() {
    mediaPlayer?.reset()
    mediaPlayer?.setDataSource(context(), successAudioUri)
    mediaPlayer?.prepare()
    mediaPlayer?.seekTo(0)
    mediaPlayer?.start()
  }

  private fun playFailAudio() {
    mediaPlayer?.reset()
    mediaPlayer?.setDataSource(context(), failAudioUri)
    mediaPlayer?.prepare()
    mediaPlayer?.seekTo(0)
    mediaPlayer?.start()
  }

  private fun clearUiAfterBarcodeResponse(): Runnable {
    return Runnable {
      lnBaggageScanSuccess.visibility = View.GONE
      lnBaggageScanFail.visibility = View.GONE
      barcodeErrorTxt.visibility = View.INVISIBLE
    }
  }

  override fun getFragment(): BaggageTrackScanZebraFragment {
    return this
  }

  override fun getPresenter(): Presenter<*> {
    return presenter
  }

  override fun scannedBaggageTag(tagNo: String, isSuccess: Boolean, message: String?) {
    if (isSuccess) {
      showResultIcon(true, null)
    } else {
      showResultIcon(false, message)
    }
    val scannedTag = ScannedTag()
    scannedTag.success = isSuccess
    scannedTag.tagNo = tagNo
    scannedTag.errorMessage = message
    scannedTagList?.add(0, scannedTag)
    lastThreeBagAdapter.notifyDataSetChanged()
    AnimUtils.animateShowView(rcvLastThree)
  }

  override fun showError(message: String?) {
  }

  override fun showError(messageId: Int) {
  }
}
