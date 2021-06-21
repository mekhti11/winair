package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage

import android.Manifest.permission
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
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
import com.hititcs.dcs.R.layout
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
  @BindView(R.id.img_close) lateinit var imgClose: AppCompatImageView
  @BindView(R.id.img_switch_flashlight) lateinit var imgSwitchFlashlight: AppCompatImageView
  @BindView(R.id.ln_baggage_scan_success) lateinit var lnBaggageScanSuccess: LinearLayout
  @BindView(R.id.ln_baggage_scan_fail) lateinit var lnBaggageScanFail: LinearLayout
  @BindView(R.id.barcode_error_txt) lateinit var barcodeErrorTxt: TextView
  @BindView(R.id.rcv_last_three_items_camera) lateinit var rcvLastThree: RecyclerView

  private var locationCode: String? = null
  private var locationName: String? = null
  var scannedTagList: MutableList<ScannedTag>? = null
  private lateinit var lastThreeBagAdapter: LastThreeBagAdapter

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
    lastThreeBagAdapter = LastThreeBagAdapter()
    rcvLastThree.adapter = lastThreeBagAdapter
    lastThreeBagAdapter.itemList = scannedTagList
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val fragmentView = inflater.inflate(layout.content_baggage_track_scan, container, false)
    bindView(fragmentView)
    return fragmentView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initFlash()
    setupLastThreeItems()
    imgPause.setOnClickListener { onClickPause() }
    imgClose.setOnClickListener { onClickClose() }
    zxingBarcodeScanner.decodeContinuous(this)
  }

  override fun onDestroy() {
    if (zxingBarcodeScanner != null) {
      zxingBarcodeScanner!!.barcodeView.stopDecoding()
      //binding?.barcodeScannerView = null
    }
    super.onDestroy()
  }

  private fun hasFlash(): Boolean {
    return activity!!.applicationContext.packageManager
      .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
  }

  fun switchFlashlight() {
    mFlashOpen = if (mFlashOpen) {
      zxingBarcodeScanner.setTorchOff()
      false
    } else {
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
      imgSwitchFlashlight.visibility = View.GONE
    } else {
      imgSwitchFlashlight.setOnClickListener { switchFlashlight() }
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
      lnBaggageScanSuccess.visibility = View.VISIBLE
    } else {
      delayTime = ERROR_DELAY_BETWEEN_SCANS
      barcodeErrorTxt.text = errorMsg
      lnBaggageScanFail.visibility = View.VISIBLE
      barcodeErrorTxt.visibility = View.VISIBLE
    }
    Handler().postDelayed(clearUiAfterBarcodeResponse(), delayTime)
  }

  private fun clearUiAfterBarcodeResponse(): Runnable {
    return Runnable {
      lnBaggageScanSuccess.visibility = View.GONE
      lnBaggageScanFail.visibility = View.GONE
      barcodeErrorTxt.visibility = View.INVISIBLE
      toggleBarcodeView(false)
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
    scannedTagList?.add(0, scannedTag)
    lastThreeBagAdapter.notifyDataSetChanged()
    AnimUtils.animateShowView(rcvLastThree)
  }

  override fun showError(message: String?) {
  }

  override fun showError(messageId: Int) {
  }
}
