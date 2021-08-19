package com.hititcs.dcs.view.barcode;

import com.hititcs.dcs.domain.interactor.boarding.ScanBarcodeUseCase;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ScanBarcodeFragmentModule {

    @Provides
    static ScanBarcodePresenter provideScanBarcodePresenter(ScanBarcodeView scanBarcodeView, ScanBarcodeUseCase scanBarcodeUseCase) {
        return new ScanBarcodePresenterImpl(scanBarcodeView, scanBarcodeUseCase);
    }

    @Binds
    abstract ScanBarcodeView bindScanBarcodeView(ScanBarcodeFragment scanBarcodeFragment);

}
