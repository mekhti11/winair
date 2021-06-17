package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage;

import com.hititcs.dcs.subscriber.SingleSubscriber;
import com.hititcs.dcs.view.baggagetracking.domain.interceptor.ScanBaggageBarcodeUseCase;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageRequest;
import javax.inject.Inject;

public class BaggageTrackScanPresenterImpl
    implements BaggageTrackScanContract.BaggageTrackScanPresenter {

  private final ScanBaggageBarcodeUseCase scanBaggageBarcodeUseCase;
  private BaggageTrackScanContract.BaggageTrackScanView view;

  @Inject
  public BaggageTrackScanPresenterImpl(BaggageTrackScanContract.BaggageTrackScanView view,
      ScanBaggageBarcodeUseCase scanBaggageBarcodeUseCase) {
    this.view = view;
    this.scanBaggageBarcodeUseCase = scanBaggageBarcodeUseCase;
  }

  @Override
  public void scanBaggageBarcode(String locationName, String locationCode, String barcodeTagNo) {
    showViewLoading();
    ScanBaggageRequest request = new ScanBaggageRequest();
    request.setLocationCode(locationCode);
    request.setLocationName(locationName);
    request.setTagNo(barcodeTagNo);
    scanBaggageBarcodeUseCase.execute(new SingleSubscriber<ScanBaggageOutputDto>(this) {
      @Override public void onResponse(ScanBaggageOutputDto data) {
        hideViewLoading();
        view.showScanBaggageBarcodeResponse(true, "");
      }

      @Override public void onError(Throwable e) {
        hideViewLoading();
        view.showScanBaggageBarcodeResponse(false, e.getMessage());
      }
    }, request);
  }

  @Override
  public BaggageTrackScanContract.BaggageTrackScanView getView() {
    return view;
  }

  @Override
  public void setView(BaggageTrackScanContract.BaggageTrackScanView view) {
    this.view = view;
  }

  @Override
  public void dispose() {
    if (scanBaggageBarcodeUseCase != null) {
      scanBaggageBarcodeUseCase.dispose();
    }
  }
}
