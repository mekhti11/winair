package com.hititcs.dcs.view.baggagetracking.view

import com.hititcs.dcs.view.LoadPresenter
import com.hititcs.dcs.view.LoadView
import com.hititcs.dcs.view.baggagetracking.domain.model.GetTrackingBaggageLocationNamesOutputDto

interface BaggageTrackMainContract {
  interface BaggageTrackMainPresenter : LoadPresenter<BaggageTrackMainView> {
    fun getLocationNamesAndCodes()
  }

  interface BaggageTrackMainView : LoadView {
    fun setLocationNamesAndCodes(locationAndNameCode: GetTrackingBaggageLocationNamesOutputDto)
  }
}