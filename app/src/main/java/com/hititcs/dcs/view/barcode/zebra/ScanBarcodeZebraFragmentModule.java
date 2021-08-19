package com.hititcs.dcs.view.barcode.zebra;

import com.hititcs.dcs.domain.interactor.boarding.ScanBarcodeUseCase;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ScanBarcodeZebraFragmentModule {
  @Provides
  static ScanBarcodeZebraPresenter provideScanBarcodeZebraPresenter(
      ScanBarcodeZebraView scanBarcodeZebraView, ScanBarcodeUseCase scanBarcodeUseCase) {
    return new ScanBarcodeZebraPresenterImpl(scanBarcodeZebraView, scanBarcodeUseCase);
  }

  @Binds
  abstract ScanBarcodeZebraView bindScanBarcodeZebraView(
      ScanBarcodeZebraFragment scanBarcodeZebraFragment);
}
