package com.hititcs.dcs.view.barcode.kranger;

import com.hititcs.dcs.domain.interactor.boarding.ScanBarcodeUseCase;
import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.subscriber.SingleSubscriber;
import com.hititcs.dcs.util.MessageUtils;

public class ScanBarcodeKrangerPresenterImpl
    implements ScanBarcodeKrangerContract.ScanBarcodeKrangerPresenter {

  private final ScanBarcodeUseCase scanBarcodeUseCase;
  private ScanBarcodeKrangerContract.ScanBarcodeKrangerView scanBarcodeKrangerView;

  public ScanBarcodeKrangerPresenterImpl(
      ScanBarcodeKrangerContract.ScanBarcodeKrangerView scanBarcodeKrangerView,
      ScanBarcodeUseCase scanBarcodeUseCase) {
    this.scanBarcodeKrangerView = scanBarcodeKrangerView;
    this.scanBarcodeUseCase = scanBarcodeUseCase;
  }

  @Override
  public ScanBarcodeKrangerContract.ScanBarcodeKrangerView getView() {
    return scanBarcodeKrangerView;
  }

  @Override
  public void setView(ScanBarcodeKrangerContract.ScanBarcodeKrangerView view) {
    this.scanBarcodeKrangerView = view;
  }

  @Override
  public void dispose() {
    if (scanBarcodeUseCase != null) {
      scanBarcodeUseCase.dispose();
    }
  }

  @Override
  public void scanBarcode(BoardWithBarcodeRequest request,
      ScanBarcodeKrangerFragment.ResponseListener<BoardingResponse> responseListener) {
    scanBarcodeKrangerView.showProgressDialog();
    scanBarcodeUseCase.execute(new SingleSubscriber<BoardingResponse>(this) {
      @Override
      public void onResponse(BoardingResponse data) {
        responseListener.onResponse(data);
        scanBarcodeKrangerView.hideProgressDialog();
      }

      @Override
      public void onError(Throwable e) {
        responseListener.onError(MessageUtils.getMessage(super.getErrorMessage(e)));
        scanBarcodeKrangerView.hideProgressDialog();
      }
    }, request);
  }
}
