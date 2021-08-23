package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.kranger

import android.Manifest.permission
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
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
import com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.kranger.BaggageTrackScanKrangerContract.BaggageTrackScanKrangerPresenter
import com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.kranger.BaggageTrackScanKrangerContract.BaggageTrackScanKrangerView
import java.io.Serializable
import javax.inject.Inject

class BaggageTrackScanKrangerFragment : BaseFragment<BaggageTrackScanKrangerFragment>(),
  BaggageTrackScanKrangerView {

  val MY_PERMISSIONS_REQUEST_CAMERA = 124
  private val SUCCESS_DELAY_BETWEEN_SCANS = 1000L
  private val ERROR_DELAY_BETWEEN_SCANS = 3000L

  @Inject lateinit var presenter: BaggageTrackScanKrangerPresenter

  @BindView(R.id.img_pause) lateinit var imgPause: AppCompatImageView
  @BindView(R.id.tv_baggage_scan_close) lateinit var tvClose: TextView
  @BindView(R.id.ln_baggage_scan_success) lateinit var lnBaggageScanSuccess: LinearLayout
  @BindView(R.id.ln_baggage_scan_fail) lateinit var lnBaggageScanFail: LinearLayout
  @BindView(R.id.barcode_error_txt) lateinit var barcodeErrorTxt: TextView
  @BindView(R.id.rcv_last_three_items_camera) lateinit var rcvLastThree: RecyclerView
  @BindView(R.id.tv_baggage_scan_location_name) lateinit var tvLocationName: TextView
  @BindView(R.id.tv_baggage_scan_location_code) lateinit var tvLocationCode: TextView
  @BindView(R.id.btn_scan) lateinit var startScanningBtn: AppCompatButton

  private var locationCode: String? = null
  private var locationName: String? = null
  var scannedTagList: MutableList<ScannedTag>? = null
  private lateinit var lastThreeBagAdapter: LastThreeBagAdapter
  private lateinit var successAudioUri: Uri
  private lateinit var failAudioUri: Uri
  private lateinit var mReceiver: BroadcastReceiver
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
      "EXTRA_LOCATION_NAME." + BaggageTrackScanKrangerFragment.javaClass.simpleName
    var EXTRA_LOCATION_CODE: String =
      "EXTRA_LOCATION_CODE." + BaggageTrackScanKrangerFragment.javaClass.simpleName
    var EXTRA_SCANNED_TAG_LIST: String =
      "EXTRA_SCANNED_TAG_LIST." + BaggageTrackScanKrangerFragment.javaClass.simpleName

    val ACTION_BARCODE = "com.barcodeservice.broadcast.string"
    val ACTION_START_BCR_SERVICE = "com.barcodeservice.on"
    val ACTION_STOP_BCR_SERVICE = "com.barcodeservice.off"
    val ACTION_TRIGGER_ON = "com.barcodeservice.start.scan"
    val ACTION_TRIGGER_OFF = "com.barcodeservice.stop.scan"
    val EXTRA_BARCODE = "barcodedata"

    fun newInstance(
      locationCode: String,
      locationName: String,
      scannedTagList: MutableList<ScannedTag>?
    ): Fragment {
      val args = Bundle()
      val fragment = BaggageTrackScanKrangerFragment()
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
    setupBroadcastReceiver()
  }

  private fun setupBroadcastReceiver() {
    mReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_BARCODE) {
          val barcode = intent.getStringExtra(EXTRA_BARCODE)
          if (barcode != null) {
            stopBarcodeService()
            presenter.scanBaggageBarcode(locationName!!, locationCode!!, barcode.takeLast(6))
          }
        }
      }
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
    val fragmentView =
      inflater.inflate(R.layout.content_baggage_track_scan_kranger, container, false)
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
    hideStartScanningBtn()
    startBarcodeService()
  }

  private fun showStartScanningBtn() {
    startScanningBtn.visibility = View.VISIBLE
  }

  private fun hideStartScanningBtn() {
    startScanningBtn.visibility = View.GONE
  }

  fun startBarcodeService() {
    activity!!.sendBroadcast(Intent(ACTION_START_BCR_SERVICE))
  }

  fun stopBarcodeService() {
    activity!!.sendBroadcast(Intent(ACTION_STOP_BCR_SERVICE))
  }

  override fun onResume() {
    super.onResume()
    activity!!.registerReceiver(mReceiver, IntentFilter(ACTION_BARCODE))
  }

  override fun onPause() {
    super.onPause()
    stopBarcodeService()
    showStartScanningBtn()
    activity!!.unregisterReceiver(mReceiver)
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

  private fun onClickClose() {
    var intent = Intent()
    intent.putExtra(EXTRA_SCANNED_TAG_LIST, scannedTagList as Serializable)
    activity?.let { activity?.setResult(Activity.RESULT_OK, intent) }
    activity?.let { activity?.finish() }
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

  override fun getFragment(): BaggageTrackScanKrangerFragment {
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
