package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.kranger

import com.hititcs.dcs.view.LoadPresenter
import com.hititcs.dcs.view.LoadView

interface BaggageTrackScanKrangerContract {
  interface BaggageTrackScanKrangerPresenter : LoadPresenter<BaggageTrackScanKrangerView> {
    fun scanBaggageBarcode(locationName: String, locationCode: String, barcodeTagNo: String)
  }

  interface BaggageTrackScanKrangerView : LoadView {
    fun scannedBaggageTag(tagNo: String, isSuccess: Boolean, message: String?)
  }
}