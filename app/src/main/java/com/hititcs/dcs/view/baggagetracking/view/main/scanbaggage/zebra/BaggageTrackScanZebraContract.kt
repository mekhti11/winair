package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.zebra

import com.hititcs.dcs.view.LoadPresenter
import com.hititcs.dcs.view.LoadView

interface BaggageTrackScanZebraContract {
  interface BaggageTrackScanZebraPresenter : LoadPresenter<BaggageTrackScanZebraView> {
    fun scanBaggageBarcode(locationName: String, locationCode: String, barcodeTagNo: String)
  }

  interface BaggageTrackScanZebraView : LoadView {
    fun scannedBaggageTag(tagNo: String, isSuccess: Boolean, message: String?)
  }
}