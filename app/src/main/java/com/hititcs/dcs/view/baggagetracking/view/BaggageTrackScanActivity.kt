package com.hititcs.dcs.view.baggagetracking.view

import android.os.Bundle
import com.hititcs.dcs.R
import com.hititcs.dcs.view.BaseActivity

class BaggageTrackScanActivity : BaseActivity<BaggageTrackScanActivity>() {
/*  companion object {
    var EXTRA_FLIGHT_ID: String = "EXTRA_FLIGHT_ID." + BaggageTrackActivity::
    class.java.simpleName
    var STATE_FLIGHT_ID: String =
      "STATE_FLIGHT_ID." + BaggageTrackActivity::class.java.simpleName
    var EXTRA_BOARDED_COUNT_START: String = "EXTRA_BOARDED_COUNT." + BaggageTrackActivity::
    class.java.simpleName
    var STATE_BOARDED_COUNT_START: String =
      "STATE_BOARDED_COUNT." + BaggageTrackActivity::class.java.simpleName
  }

  var flightId: String? = null
  var boardedCountStart: String? = null*/

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_scan_barcode)
    bindView()
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

/*  override fun onSaveInstanceState(outState: Bundle) {
    outState.putString(STATE_FLIGHT_ID, flightId)
    outState.putString(STATE_BOARDED_COUNT_START, boardedCountStart)
    super.onSaveInstanceState(outState)
  }*/

  override fun getActivity(): BaggageTrackScanActivity {
    return this
  }
}
