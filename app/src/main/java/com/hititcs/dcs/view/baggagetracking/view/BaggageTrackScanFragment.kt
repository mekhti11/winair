package com.hititcs.dcs.view.baggagetracking.view

import android.Manifest.permission
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import butterknife.BindView
import com.google.zxing.ResultPoint
import com.hititcs.dcs.R
import com.hititcs.dcs.R.layout
import com.hititcs.dcs.domain.model.BoardingResponse
import com.hititcs.dcs.util.MessageUtils
import com.hititcs.dcs.view.BaseFragment
import com.hititcs.dcs.view.Presenter
import com.hititcs.dcs.view.baggagetracking.domain.model.GetTrackingBaggageLocationNamesOutputDto
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DecoratedBarcodeView.TorchListener
import timber.log.Timber
import javax.inject.Inject

class BaggageTrackScanFragment : BaseFragment<BaggageTrackScanFragment>(),
  BaggageTrackScanContract.BaggageTrackView,
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

  @Inject lateinit var presenter: BaggageTrackScanContract.BaggageTrackPresenter

  @BindView(R.id.tw_boarded_count) lateinit var twBoardedCount: TextView
  @BindView(R.id.zxing_barcode_scanner) lateinit var zxingBarcodeScanner: DecoratedBarcodeView
  @BindView(R.id.img_pause) lateinit var imgPause: AppCompatImageView
  @BindView(R.id.img_close) lateinit var imgClose: AppCompatImageView
  @BindView(R.id.img_switch_flashlight) lateinit var imgSwitchFlashlight: AppCompatImageView
  @BindView(R.id.img_barcode_success) lateinit var imgBarcodeSuccess: AppCompatImageView
  @BindView(R.id.img_barcode_error) lateinit var imgBarcodeError: AppCompatImageView
  @BindView(R.id.barcode_error_txt) lateinit var barcodeErrorTxt: TextView

  companion object {
    fun newInstance(): Fragment {
      val args = Bundle()
      val fragment = BaggageTrackScanFragment()
      fragment.arguments = args
      return fragment
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
    twBoardedCount.text = "asd"
    imgPause.setOnClickListener { onClickPause() }
    imgClose.setOnClickListener { onClickClose() }
    zxingBarcodeScanner.decodeContinuous(this)
    presenter.getLocationNamesAndCodes()
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
    activity!!.onBackPressed()
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

  fun showBarcodeResult(data: BoardingResponse) {
    twBoardedCount.text = MessageUtils.getStringFromList(data.flightDetail?.boarded)
    showResultIcon(true, null)
  }

  private fun showResultIcon(success: Boolean, errorMsg: String?) {
    hideProgressDialog()
    var delayTime: Long = SUCCESS_DELAY_BETWEEN_SCANS
    if (success) {
      imgBarcodeSuccess.visibility = View.VISIBLE
    } else {
      delayTime = ERROR_DELAY_BETWEEN_SCANS
      barcodeErrorTxt.text = errorMsg
      imgBarcodeError.visibility = View.VISIBLE
      barcodeErrorTxt.visibility = View.VISIBLE
    }
    Handler().postDelayed(clearUiAfterBarcodeResponse(), delayTime)
  }

  private fun clearUiAfterBarcodeResponse(): Runnable {
    return Runnable {
      imgBarcodeSuccess.visibility = View.GONE
      imgBarcodeError.visibility = View.GONE
      barcodeErrorTxt.visibility = View.GONE
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
    presenter.scanBaggageBarcode("CHECK_IN", "CK", barcodeTagNo.takeLast(6))
  }

  override fun getFragment(): BaggageTrackScanFragment {
    return this
  }

  override fun getPresenter(): Presenter<*> {
    return presenter
  }

  override fun showScanBaggageBarcodeResponse(isSuccess: Boolean, message: String?) {
    var test: String = if (isSuccess)
      "True"
    else
      "False"
    Timber.d("$test $message")
    try {
      twBoardedCount.text = message
    } catch (e: Exception) {
      Timber.e(e)
    }
    showResultIcon(true, null)
  }

  override fun setLocationNamesAndCodes(locationAndNameCode: GetTrackingBaggageLocationNamesOutputDto) {
  }

  override fun showError(message: String?) {
    TODO("Not yet implemented")
  }

  override fun showError(messageId: Int) {
    TODO("Not yet implemented")
  }
}
