package com.hititcs.dcs.view.baggagetracking.view

import android.os.Bundle
import com.hititcs.dcs.R
import com.hititcs.dcs.R.layout
import com.hititcs.dcs.view.BaseActivity

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
  }

  var locationName: String? = null
  var locationCode: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_scan_baggage)
    bindView()
    setToolbar()
    hideToolbar()
    setUpFragment()
  }

  private fun setUpFragment() {
    supportFragmentManager
      .beginTransaction()
      .replace(
        R.id.content_frame,
        BaggageTrackScanFragment.newInstance(),
        BaggageTrackScanFragment::class.java.simpleName
      )
      .commit()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putString(STATE_LOCATION_CODE, locationCode)
    outState.putString(STATE_LOCATION_NAME, locationName)
    super.onSaveInstanceState(outState)
  }

  override fun getActivity(): BaggageTrackScanActivity {
    return this
  }
}
