package com.hititcs.dcs.view.barcode;

import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.view.LoadView;

public interface ScanBarcodeView extends LoadView {

    void showBarcodeResult(BoardingResponse data);

}
