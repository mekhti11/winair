package com.hititcs.dcs.view.baggagetracking.view

import com.hititcs.dcs.view.LoadPresenter
import com.hititcs.dcs.view.LoadView
import com.hititcs.dcs.view.baggagetracking.domain.model.GetTrackingBaggageLocationNamesOutputDto

interface BaggageTrackScanContract {
  interface BaggageTrackPresenter : LoadPresenter<BaggageTrackView> {
    fun scanBaggageBarcode(locationName: String, locationCode: String, barcodeTagNo: String)
    fun getLocationNamesAndCodes()
  }

  interface BaggageTrackView : LoadView {
    fun showScanBaggageBarcodeResponse(isSuccess: Boolean, message: String?);
    fun setLocationNamesAndCodes(locationAndNameCode: GetTrackingBaggageLocationNamesOutputDto)
  }
}