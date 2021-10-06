package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage

import com.hititcs.dcs.view.LoadPresenter
import com.hititcs.dcs.view.LoadView

interface BaggageTrackScanContract {
  interface BaggageTrackScanPresenter : LoadPresenter<BaggageTrackScanView> {
    fun scanBaggageBarcode(locationName: String, locationCode: String, barcodeTagNo: String)
  }

  interface BaggageTrackScanView : LoadView {
    fun scannedBaggageTag(tagNo: String, isSuccess: Boolean, message: String?)
  }
}