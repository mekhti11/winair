package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage

import android.os.Bundle
import com.hititcs.dcs.R
import com.hititcs.dcs.R.layout
import com.hititcs.dcs.view.BaseActivity
import com.hititcs.dcs.view.baggagetracking.domain.model.ScannedTag
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
  }

  var locationName: String? = null
  var locationCode: String? = null
  var scannedTagList: MutableList<ScannedTag>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_scan_baggage)
    locationName = intent.getStringExtra(EXTRA_LOCATION_NAME)
    locationCode = intent.getStringExtra(EXTRA_LOCATION_CODE)
    scannedTagList =
      intent.getSerializableExtra(EXTRA_SCANNED_TAG_LIST) as (MutableList<ScannedTag>?)
    bindView()
    setToolbar()
    hideToolbar()
    setUpFragment()
  }

  private fun setUpFragment() {
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

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putString(STATE_LOCATION_CODE, locationCode)
    outState.putString(STATE_LOCATION_NAME, locationName)
    outState.putSerializable(STATE_SCANNED_TAG_LIST, scannedTagList as Serializable)
    super.onSaveInstanceState(outState)
  }

  override fun getActivity(): BaggageTrackScanActivity {
    return this
  }
}
