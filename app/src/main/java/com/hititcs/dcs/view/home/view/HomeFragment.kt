package com.hititcs.dcs.view.home.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import com.hititcs.dcs.R
import com.hititcs.dcs.R.layout
import com.hititcs.dcs.view.BaseFragment
import com.hititcs.dcs.view.Presenter
import com.hititcs.dcs.view.baggagetracking.view.BaggageTrackScanActivity
import com.hititcs.dcs.view.flight.FlightListActivity
import com.hititcs.dcs.view.home.view.HomeContract.HomePresenter
import com.hititcs.dcs.view.home.view.HomeContract.HomeView
import javax.inject.Inject

class HomeFragment : BaseFragment<HomeFragment>(), HomeView {

  @Inject lateinit var presenter: HomePresenter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val fragmentView = inflater.inflate(layout.content_home, container, false)
    bindView(fragmentView)
    return fragmentView
  }

  @OnClick(R.id.btn_flights)
  fun onPressedBtnFlights() {
    val intent = Intent(context, FlightListActivity::class.java)
    startActivity(intent)
  }

  @OnClick(R.id.btn_baggage_tracking)
  fun onPressedBtnBaggageTracking() {
    val intent = Intent(context, BaggageTrackScanActivity::class.java)
    startActivity(intent)
  }

  override fun getFragment(): HomeFragment {
    return this
  }

  override fun getPresenter(): Presenter<*> {
    return presenter
  }

  override fun showError(message: String?) {
  }

  override fun showError(messageId: Int) {
  }

  companion object {
    fun newInstance(): HomeFragment {
      val args = Bundle()
      val fragment = HomeFragment()
      fragment.arguments = args
      return fragment
    }
  }
}