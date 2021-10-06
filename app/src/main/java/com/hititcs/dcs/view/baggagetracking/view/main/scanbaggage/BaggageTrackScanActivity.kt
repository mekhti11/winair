package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.hititcs.dcs.R
import com.hititcs.dcs.R.layout
import com.hititcs.dcs.model.DeviceEnum
import com.hititcs.dcs.util.FragmentUtils
import com.hititcs.dcs.view.BaseActivity
import com.hititcs.dcs.view.baggagetracking.domain.model.ScannedTag
import com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.kranger.BaggageTrackScanKrangerFragment
import com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.zebra.BaggageTrackScanZebraFragment
import com.hititcs.dcs.view.flight.detail.FlightDetailFragment
import java.io.Serializable

class BaggageTrackScanActivity : BaseActivity<BaggageTrackScanActivity>() {
  companion object {
    var EXTRA_LOCATION_NAME: String = "EXTRA_LOCATION_NAME." + BaggageTrackScanActivity::
    class.java.simpleName
    var STATE_LOCATION_NAME: String =
      "STATE_LOCATION_NAME." + BaggageTrackScanActivity::class.java.simpleName
    var EXTRA_LOCATION_CODE: String = "EXTRA_LOCATION_CODE." + BaggageTrackScanActivity::
    class.java.simpleName
    var STATE_LOCATION_CODE: String =
      "STATE_LOCATION_CODE." + BaggageTrackScanActivity::class.java.simpleName
    var EXTRA_SCANNED_TAG_LIST: String = "STATE_SCANNED_TAG_LIST." + BaggageTrackScanActivity::
    class.java.simpleName
    var STATE_SCANNED_TAG_LIST: String = "STATE_SCANNED_TAG_LIST." + BaggageTrackScanActivity::
    class.java.simpleName
    var EXTRA_SELECTED_DEVICE =
      "EXTRA_SELECTED_DEVICE." + FlightDetailFragment::class.java.simpleName
    var STATE_SELECTED_DEVICE: String = "STATE_SELECTED_DEVICE." + BaggageTrackScanActivity::
    class.java.simpleName
  }

  var locationName: String? = null
  var locationCode: String? = null
  var scannedTagList: MutableList<ScannedTag>? = null
  var selectedDevice: String? = null
  var isExitedFlagForRunnable: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_scan_baggage)
    locationName = intent.getStringExtra(EXTRA_LOCATION_NAME)
    locationCode = intent.getStringExtra(EXTRA_LOCATION_CODE)
    scannedTagList =
      intent.getSerializableExtra(EXTRA_SCANNED_TAG_LIST) as (MutableList<ScannedTag>?)
    selectedDevice = intent.getStringExtra(EXTRA_SELECTED_DEVICE)
    bindView()
    setToolbar()
    hideToolbar()
    if (selectedDevice!!.equals(DeviceEnum.CAMERA.value)) {
      setUpCameraFragment()
    } else if (selectedDevice!!.equals(DeviceEnum.ZEBRA.value)) {
      setUpZebraFragment()
    } else if (selectedDevice!!.equals(DeviceEnum.K_RANGER.value)) {
      setUpKrangerFragment()
    }
  }

  override fun onBackPressed() {
    isExitedFlagForRunnable = true
    var intent = Intent()
    if (FragmentUtils.getVisibleFragment(supportFragmentManager) is BaggageTrackScanFragment) {
      intent.putExtra(
        BaggageTrackScanFragment.EXTRA_SCANNED_TAG_LIST,
        scannedTagList as Serializable
      )
    } else if (FragmentUtils.getVisibleFragment(supportFragmentManager) is BaggageTrackScanZebraFragment) {
      intent.putExtra(
        BaggageTrackScanZebraFragment.EXTRA_SCANNED_TAG_LIST,
        scannedTagList as Serializable
      )
    } else if (FragmentUtils.getVisibleFragment(supportFragmentManager) is BaggageTrackScanKrangerFragment) {
      intent.putExtra(
        BaggageTrackScanKrangerFragment.EXTRA_SCANNED_TAG_LIST,
        scannedTagList as Serializable
      )
    }
    activity?.let { activity?.setResult(Activity.RESULT_OK, intent) }
    activity?.let { activity?.finish() }
  }

  private fun setUpCameraFragment() {
    if (scannedTagList == null) {
      scannedTagList = mutableListOf()
    }
    supportFragmentManager
      .beginTransaction()
      .replace(
        R.id.content_frame,
        BaggageTrackScanFragment.newInstance(locationCode!!, locationName!!, scannedTagList!!),
        BaggageTrackScanFragment::class.java.simpleName
      )
      .commit()
  }

  private fun setUpZebraFragment() {
    if (scannedTagList == null) {
      scannedTagList = mutableListOf()
    }
    supportFragmentManager
      .beginTransaction()
      .replace(
        R.id.content_frame,
        BaggageTrackScanZebraFragment.newInstance(locationCode!!, locationName!!, scannedTagList!!),
        BaggageTrackScanZebraFragment::class.java.simpleName
      )
      .commit()
  }

  private fun setUpKrangerFragment() {
    if (scannedTagList == null) {
      scannedTagList = mutableListOf()
    }
    supportFragmentManager
      .beginTransaction()
      .replace(
        R.id.content_frame,
        BaggageTrackScanKrangerFragment.newInstance(
          locationCode!!,
          locationName!!,
          scannedTagList!!
        ),
        BaggageTrackScanKrangerFragment::class.java.simpleName
      )
      .commit()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putString(STATE_LOCATION_CODE, locationCode)
    outState.putString(STATE_LOCATION_NAME, locationName)
    outState.putString(STATE_SELECTED_DEVICE, selectedDevice)
    outState.putSerializable(STATE_SCANNED_TAG_LIST, scannedTagList as Serializable)
    super.onSaveInstanceState(outState)
  }

  override fun getActivity(): BaggageTrackScanActivity {
    return this
  }
}
