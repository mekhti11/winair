package com.hititcs.dcs.view.home.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import butterknife.OnClick
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hititcs.dcs.R
import com.hititcs.dcs.util.FragmentUtils
import com.hititcs.dcs.view.BaseActivity
import com.hititcs.dcs.view.BaseFragment
import com.hititcs.dcs.view.Presenter
import com.hititcs.dcs.view.baggagetracking.view.main.BaggageTrackMainActivity
import com.hititcs.dcs.view.flight.FlightListActivity
import com.hititcs.dcs.view.settings.SettingsFragment
import javax.inject.Inject

class HomeFragment : BaseFragment<HomeFragment>(), HomeContract.HomeView {

  @Inject lateinit var presenter: HomeContract.HomePresenter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val fragmentView = inflater.inflate(R.layout.content_home, container, false)
    bindView(fragmentView)
    return fragmentView
  }

  @OnClick(R.id.rlt_home_flights)
  fun onPressedBtnFlights() {
    val intent = Intent(context, FlightListActivity::class.java)
    startActivity(intent)
  }

  @OnClick(R.id.rlt_baggage_tracking)
  fun onPressedBtnBaggageTracking() {
    val intent = Intent(context, BaggageTrackMainActivity::class.java)
    startActivity(intent)
  }

  @OnClick(R.id.iv_menu_expand)
  fun onPressedMenuExpand() {
    createAndShowExpandMenu()
  }

  private fun createAndShowExpandMenu() {
    val bottomSheetDialog = BottomSheetDialog(context(), R.style.CustomSheetDialog)
    bottomSheetDialog.setContentView(R.layout.home_expand_menu)
    var rltLogout = bottomSheetDialog.findViewById<RelativeLayout>(R.id.rlt_logout)
    var rltSettings = bottomSheetDialog.findViewById<RelativeLayout>(R.id.rlt_settings)

    rltLogout!!.setOnClickListener { (activity as BaseActivity<*>?)!!.logout() }
    rltSettings!!.setOnClickListener {
      bottomSheetDialog.dismiss()
      FragmentUtils.replaceFragmentWithStack(
        fragmentManager,
        SettingsFragment.newInstance(),
        SettingsFragment.javaClass.simpleName
      )
    }
    //TODO fix this make logout composable in BaseActivity
    bottomSheetDialog.show()
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