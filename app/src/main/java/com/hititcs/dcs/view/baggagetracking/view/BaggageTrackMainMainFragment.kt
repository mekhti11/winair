package com.hititcs.dcs.view.baggagetracking.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hititcs.dcs.R.layout
import com.hititcs.dcs.view.BaseFragment
import com.hititcs.dcs.view.Presenter
import com.hititcs.dcs.view.baggagetracking.domain.model.GetTrackingBaggageLocationNamesOutputDto
import javax.inject.Inject

class BaggageTrackMainMainFragment : BaseFragment<BaggageTrackMainMainFragment>(),
  BaggageTrackMainContract.BaggageTrackMainView {

  @Inject lateinit var presenter: BaggageTrackMainContract.BaggageTrackMainPresenter

  companion object {
    fun newInstance(): Fragment {
      val args = Bundle()
      val fragment = BaggageTrackMainMainFragment()
      fragment.arguments = args
      return fragment
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val fragmentView = inflater.inflate(layout.content_baggage_track_main, container, false)
    bindView(fragmentView)
    return fragmentView
  }

  override fun getFragment(): BaggageTrackMainMainFragment {
    return this
  }

  override fun getPresenter(): Presenter<*> {
    return presenter
  }

  override fun setLocationNamesAndCodes(locationAndNameCode: GetTrackingBaggageLocationNamesOutputDto) {
  }

  override fun showError(message: String?) {
  }

  override fun showError(messageId: Int) {
  }
}