package com.hititcs.dcs.view.barcode.zebra;

import com.hititcs.dcs.domain.interactor.boarding.ScanBarcodeUseCase;
import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.subscriber.SingleSubscriber;
import com.hititcs.dcs.util.MessageUtils;

public class ScanBarcodeZebraPresenterImpl implements ScanBarcodeZebraPresenter {

  private final ScanBarcodeUseCase scanBarcodeUseCase;
  private ScanBarcodeZebraView scanBarcodeZebraView;

  public ScanBarcodeZebraPresenterImpl(ScanBarcodeZebraView scanBarcodeZebraView,
      ScanBarcodeUseCase scanBarcodeUseCase) {
    this.scanBarcodeZebraView = scanBarcodeZebraView;
    this.scanBarcodeUseCase = scanBarcodeUseCase;
  }

  @Override
  public ScanBarcodeZebraView getView() {
    return scanBarcodeZebraView;
  }

  @Override
  public void setView(ScanBarcodeZebraView view) {
    this.scanBarcodeZebraView = view;
  }

  @Override
  public void dispose() {
    if (scanBarcodeUseCase != null) {
      scanBarcodeUseCase.dispose();
    }
  }

  @Override
  public void scanBarcode(BoardWithBarcodeRequest request,
      ScanBarcodeZebraFragment.ResponseListener<BoardingResponse> responseListener) {
    scanBarcodeZebraView.showProgressDialog();
    scanBarcodeUseCase.execute(new SingleSubscriber<BoardingResponse>(this) {
      @Override
      public void onResponse(BoardingResponse data) {
        responseListener.onResponse(data);
        scanBarcodeZebraView.hideProgressDialog();
      }

      @Override
      public void onError(Throwable e) {
        responseListener.onError(MessageUtils.getMessage(super.getErrorMessage(e)));
        scanBarcodeZebraView.hideProgressDialog();
      }
    }, request);
  }
}