package com.hititcs.dcs.view.barcode.zebra;

import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.view.LoadPresenter;

public interface ScanBarcodeZebraPresenter extends LoadPresenter<ScanBarcodeZebraView> {
  void scanBarcode(BoardWithBarcodeRequest request,
      ScanBarcodeZebraFragment.ResponseListener<BoardingResponse> responseListener);
}
