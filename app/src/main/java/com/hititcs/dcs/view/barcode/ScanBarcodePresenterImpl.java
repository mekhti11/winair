package com.hititcs.dcs.view.barcode;

import com.hititcs.dcs.di.scope.ScanBarcodeScope;
import com.hititcs.dcs.domain.interactor.boarding.ScanBarcodeUseCase;
import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.subscriber.SingleSubscriber;
import com.hititcs.dcs.util.MessageUtils;
import com.hititcs.dcs.view.barcode.ScanBarcodeFragment.ResponseListener;

@ScanBarcodeScope
public class ScanBarcodePresenterImpl implements ScanBarcodePresenter {

  private ScanBarcodeView scanBarcodeView;
  private final ScanBarcodeUseCase scanBarcodeUseCase;

  public ScanBarcodePresenterImpl(ScanBarcodeView scanBarcodeView, ScanBarcodeUseCase scanBarcodeUseCase) {
    this.scanBarcodeView = scanBarcodeView;
    this.scanBarcodeUseCase = scanBarcodeUseCase;
  }

  @Override
  public ScanBarcodeView getView() {
    return scanBarcodeView;
  }

  @Override
  public void setView(ScanBarcodeView view) {
    this.scanBarcodeView = view;
  }

  @Override
  public void dispose() {
    if (scanBarcodeUseCase != null) {
      scanBarcodeUseCase.dispose();
    }
  }

  @Override
  public void scanBarcode(BoardWithBarcodeRequest request,
      ResponseListener<BoardingResponse> responseListener) {
    scanBarcodeUseCase.execute(new SingleSubscriber<BoardingResponse>(this) {
      @Override
      public void onResponse(BoardingResponse data) {
        responseListener.onResponse(data);
      }

      @Override
      public void onError(Throwable e) {
        responseListener.onError(MessageUtils.getMessage(super.getErrorMessage(e)));
      }
    }, request);
  }
}
