package com.hititcs.dcs.view.barcode.kranger;

import com.hititcs.dcs.domain.interactor.boarding.ScanBarcodeUseCase;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ScanBarcodeKrangerFragmentModule {
  @Provides
  static ScanBarcodeKrangerContract.ScanBarcodeKrangerPresenter provideScanBarcodeKrangerPresenter(
      ScanBarcodeKrangerContract.ScanBarcodeKrangerView scanBarcodeKrangerView,
      ScanBarcodeUseCase scanBarcodeUseCase) {
    return new ScanBarcodeKrangerPresenterImpl(scanBarcodeKrangerView, scanBarcodeUseCase);
  }

  @Binds
  abstract ScanBarcodeKrangerContract.ScanBarcodeKrangerView bindScanBarcodeKrangerView(
      ScanBarcodeKrangerFragment scanBarcodeKrangerFragment);
}
