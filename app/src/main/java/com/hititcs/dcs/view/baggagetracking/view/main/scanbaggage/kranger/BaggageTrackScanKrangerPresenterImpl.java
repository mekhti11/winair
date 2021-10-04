package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.kranger;

import com.hititcs.dcs.subscriber.SingleSubscriber;
import com.hititcs.dcs.view.baggagetracking.domain.interceptor.ScanBaggageBarcodeUseCase;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageRequest;
import javax.inject.Inject;

public class BaggageTrackScanKrangerPresenterImpl
    implements BaggageTrackScanKrangerContract.BaggageTrackScanKrangerPresenter {

  private final ScanBaggageBarcodeUseCase scanBaggageBarcodeUseCase;
  private BaggageTrackScanKrangerContract.BaggageTrackScanKrangerView view;

  @Inject
  public BaggageTrackScanKrangerPresenterImpl(
      BaggageTrackScanKrangerContract.BaggageTrackScanKrangerView view,
      ScanBaggageBarcodeUseCase scanBaggageBarcodeUseCase) {
    this.view = view;
    this.scanBaggageBarcodeUseCase = scanBaggageBarcodeUseCase;
  }

  @Override
  public void scanBaggageBarcode(String locationName, String locationCode, String barcodeTagNo) {
    view.showProgressDialog();
    ScanBaggageRequest request = new ScanBaggageRequest();
    request.setLocationCode(locationCode);
    request.setLocationName(locationName);
    request.setTagNo(barcodeTagNo);
    scanBaggageBarcodeUseCase.execute(new SingleSubscriber<ScanBaggageOutputDto>(this) {
      @Override public void onResponse(ScanBaggageOutputDto data) {
        view.hideProgressDialog();
        view.scannedBaggageTag(barcodeTagNo, true, null);
      }

      @Override public void onError(Throwable e) {
        view.hideProgressDialog();
        view.scannedBaggageTag(barcodeTagNo, false, super.getErrorMessage(e));
      }
    }, request);
  }

  @Override
  public BaggageTrackScanKrangerContract.BaggageTrackScanKrangerView getView() {
    return view;
  }

  @Override
  public void setView(BaggageTrackScanKrangerContract.BaggageTrackScanKrangerView view) {
    this.view = view;
  }

  @Override
  public void dispose() {
    if (scanBaggageBarcodeUseCase != null) {
      scanBaggageBarcodeUseCase.dispose();
    }
  }
}