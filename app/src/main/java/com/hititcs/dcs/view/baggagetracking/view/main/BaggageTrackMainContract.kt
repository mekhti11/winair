package com.hititcs.dcs.view.baggagetracking.view.main

import com.hititcs.dcs.view.LoadPresenter
import com.hititcs.dcs.view.LoadView
import com.hititcs.dcs.view.baggagetracking.domain.model.TrackBaggageLocationDto

interface BaggageTrackMainContract {
  interface BaggageTrackMainPresenter : LoadPresenter<BaggageTrackMainView> {
    fun getLocationNamesAndCodes()
    fun scanSingleBaggageTag(tagNo: String, locationCode: String, locationName: String)
  }

  interface BaggageTrackMainView : LoadView {
    fun setLocationNamesAndCodes(locationAndNameCodes: List<TrackBaggageLocationDto>)
    fun scannedBaggageTag(tagNo: String, isSuccess: Boolean, errorMessage: String?)
  }
}