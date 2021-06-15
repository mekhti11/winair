package com.hititcs.dcs.view.baggagetracking.view

import com.hititcs.dcs.subscriber.SingleSubscriber
import com.hititcs.dcs.view.baggagetracking.domain.interceptor.GetLocationNamesUseCase
import com.hititcs.dcs.view.baggagetracking.domain.interceptor.ScanBaggageBarcodeUseCase
import com.hititcs.dcs.view.baggagetracking.domain.model.GetTrackingBaggageLocationNamesOutputDto
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageOutputDto
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageRequest
import com.hititcs.dcs.view.baggagetracking.view.BaggageTrackScanContract.BaggageTrackPresenter
import javax.inject.Inject

class BaggageTrackScanPresenterImpl @Inject constructor(
  private var view: BaggageTrackScanContract.BaggageTrackView,
  private val scanBaggageBarcodeUseCase: ScanBaggageBarcodeUseCase,
  private val getLocationNamesUseCase: GetLocationNamesUseCase
) : BaggageTrackPresenter {

  override fun scanBaggageBarcode(
    locationName: String,
    locationCode: String,
    barcodeTagNo: String
  ) {
    showViewLoading()
    val request = ScanBaggageRequest()
    request.locationCode = locationCode
    request.locationName = locationName
    request.tagNo = barcodeTagNo
    scanBaggageBarcodeUseCase.execute(object : SingleSubscriber<ScanBaggageOutputDto>(this) {
      override fun onResponse(data: ScanBaggageOutputDto) {
        hideViewLoading()
        view.showScanBaggageBarcodeResponse(true, "")
      }

      override fun onError(e: Throwable) {
        hideViewLoading()
        view.showScanBaggageBarcodeResponse(true, e.message)
      }
    }, request)
  }

  override fun getLocationNamesAndCodes() {
    showViewLoading()
    getLocationNamesUseCase.execute(object :
      SingleSubscriber<GetTrackingBaggageLocationNamesOutputDto>(this) {
      override fun onResponse(data: GetTrackingBaggageLocationNamesOutputDto) {
        view.setLocationNamesAndCodes(data)
        hideViewLoading()
      }
    })
  }

  override fun getView(): BaggageTrackScanContract.BaggageTrackView {
    return view
  }

  override fun setView(view: BaggageTrackScanContract.BaggageTrackView) {
    this.view = view
  }

  override fun dispose() {
    scanBaggageBarcodeUseCase.dispose()
  }
}

