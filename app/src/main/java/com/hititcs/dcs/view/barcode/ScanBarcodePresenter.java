package com.hititcs.dcs.view.barcode;

import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.view.LoadPresenter;
import com.hititcs.dcs.view.barcode.ScanBarcodeFragment.ResponseListener;

public interface ScanBarcodePresenter extends LoadPresenter<ScanBarcodeView> {

  void scanBarcode(BoardWithBarcodeRequest request,
      ResponseListener<BoardingResponse> responseListener);
}
