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
import com.google.zxing.ResultPoint
import com.hititcs.dcs.R
import com.hititcs.dcs.util.AnimUtils
import com.hititcs.dcs.view.BaseFragment
import com.hititcs.dcs.view.Presenter
import com.hititcs.dcs.view.baggagetracking.domain.model.ScannedTag
import com.hititcs.dcs.view.baggagetracking.view.main.LastThreeBagAdapter
import com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.BaggageTrackScanContract.BaggageTrackScanPresenter
import com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.BaggageTrackScanContract.BaggageTrackScanView
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DecoratedBarcodeView.TorchListener
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject

class BaggageTrackScanFragment : BaseFragment<BaggageTrackScanFragment>(),
  BaggageTrackScanView,
  TorchListener,
  BarcodeCallback {

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

  @BindView(R.id.zxing_barcode_scanner) lateinit var zxingBarcodeScanner: DecoratedBarcodeView
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
    val fragmentView = inflater.inflate(R.layout.content_baggage_track_scan, container, false)
    bindView(fragmentView)
    return fragmentView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupAudios()
    initFlash()
    setupLastThreeItems()
    imgPause.setOnClickListener { onClickPause() }
    tvClose.setOnClickListener { onClickClose() }
    initTextViews()
    zxingBarcodeScanner.decodeContinuous(this)
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

  private fun initTextViews() {
    tvLocationName.text =
      String.format("%s: %s", getString(R.string.baggage_track_scan_location_name), locationName)
    tvLocationCode.text =
      String.format("%s: %s", getString(R.string.baggage_track_scan_location_code), locationCode)
  }

  override fun onDestroy() {
    if (zxingBarcodeScanner != null) {
      zxingBarcodeScanner!!.barcodeView.stopDecoding()
      //binding?.barcodeScannerView = null
    }
    mediaPlayer?.release()
    mediaPlayer = null
    super.onDestroy()
  }

  private fun hasFlash(): Boolean {
    return activity!!.applicationContext.packageManager
      .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
  }

  fun switchFlashlight() {
    mFlashOpen = if (mFlashOpen) {
      tvFlashlight.alpha = 0.5f
      zxingBarcodeScanner.setTorchOff()
      false
    } else {
      tvFlashlight.alpha = 1.0f
      zxingBarcodeScanner.setTorchOn()
      true
    }
  }

  private fun toggleCamera() {
    if (mCameraPause) {
      toggleBarcodeView(false)
    } else {
      toggleBarcodeView(true)
    }
  }

  private fun initFlash() {
    if (!hasFlash()) {
      tvFlashlight.visibility = View.GONE
    } else {
      tvFlashlight.setOnClickListener { switchFlashlight() }
    }
  }

  private fun toggleBarcodeView(pause: Boolean) {
    if (zxingBarcodeScanner != null) {
      mCameraPause = pause
      if (pause) {
        zxingBarcodeScanner.pause()
      } else {
        zxingBarcodeScanner.resume()
      }
    }
  }

  override fun onTorchOn() {
    //    imgFlash.setImageDrawable(mCloseFlashDrawable);
  }

  override fun onTorchOff() {
    //    imgFlash.setImageDrawable(mOpenFlashDrawable);
  }

  private fun onClickPause() {
    toggleCamera()
  }

  private fun onClickClose() {
    (activity as BaggageTrackScanActivity).isExitedFlagForRunnable = true
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

  override fun onResume() {
    super.onResume()
    zxingBarcodeScanner.resume()
  }

  override fun onPause() {
    zxingBarcodeScanner.pause()
    //scanBarcodePresenter.destroyView()
    super.onPause()
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
      if (activity != null && !(activity as BaggageTrackScanActivity).isExitedFlagForRunnable) {
        lnBaggageScanSuccess.visibility = View.GONE
        lnBaggageScanFail.visibility = View.GONE
        barcodeErrorTxt.visibility = View.INVISIBLE
        toggleBarcodeView(false)
      }
    }
  }

  override fun barcodeResult(result: BarcodeResult?) {
    val resultText = if (result?.text == null) "" else result.text
    if (TextUtils.isEmpty(resultText)) {
      Timber.d("Barcode okunamadi!") //TODO unread
      return
    } else {
      Timber.d("Barcode %s", resultText) //TODO read
      toggleBarcodeView(true)
      sendBaggageBarcodeScanRequest(resultText)
    }
  }

  override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
  }

  private fun sendBaggageBarcodeScanRequest(barcodeTagNo: String) {
    presenter.scanBaggageBarcode(locationName!!, locationCode!!, barcodeTagNo.takeLast(6))
  }

  override fun getFragment(): BaggageTrackScanFragment {
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
