package com.hititcs.dcs.view.barcode.zebra;

import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.view.LoadView;

public interface ScanBarcodeZebraView extends LoadView {
  void showBarcodeResult(BoardingResponse data);
}
