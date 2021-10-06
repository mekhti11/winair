package com.hititcs.dcs.view.barcode.kranger;

import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.view.LoadPresenter;
import com.hititcs.dcs.view.LoadView;

public interface ScanBarcodeKrangerContract {

  interface ScanBarcodeKrangerPresenter extends LoadPresenter<ScanBarcodeKrangerView> {
    void scanBarcode(BoardWithBarcodeRequest request,
        ScanBarcodeKrangerFragment.ResponseListener<BoardingResponse> responseListener);
  }

  interface ScanBarcodeKrangerView extends LoadView {
    void showBarcodeResult(BoardingResponse data);
  }
}
