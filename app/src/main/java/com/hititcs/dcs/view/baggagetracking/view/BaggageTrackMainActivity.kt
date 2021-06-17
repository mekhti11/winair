package com.hititcs.dcs.view.baggagetracking.view

import android.os.Bundle
import com.hititcs.dcs.R
import com.hititcs.dcs.R.layout
import com.hititcs.dcs.view.BaseActivity

class BaggageTrackMainActivity : BaseActivity<BaggageTrackMainActivity>() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_content)
    bindView()
    setToolbar()
    setTitle(getString(R.string.page_title_baggage_tracking))
    setUpFragment()
  }

  private fun setUpFragment() {
    supportFragmentManager
      .beginTransaction()
      .replace(
        R.id.content_frame,
        BaggageTrackMainMainFragment.newInstance(),
        BaggageTrackMainMainFragment::class.java.simpleName
      )
      .commit()
  }

  override fun getActivity(): BaggageTrackMainActivity {
    return this
  }
}