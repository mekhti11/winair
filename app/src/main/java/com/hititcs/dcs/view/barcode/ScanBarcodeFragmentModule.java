package com.hititcs.dcs.view.barcode;

import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.domain.executor.ThreadExecutor;
import com.hititcs.dcs.domain.interactor.boarding.ScanBarcodeUseCase;
import com.hititcs.dcs.domain.repository.BoardingRepository;
import com.hititcs.dcs.view.barcode.ScanBarcodeFragment;
import com.hititcs.dcs.view.barcode.ScanBarcodePresenter;
import com.hititcs.dcs.view.barcode.ScanBarcodePresenterImpl;
import com.hititcs.dcs.view.barcode.ScanBarcodeView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ScanBarcodeFragmentModule {

    @Provides
    static ScanBarcodeUseCase provideScanBarcodeUseCase(BoardingRepository boardingRepository,
                                                        PostExecutionThread postExecutionThread,
                                                        ThreadExecutor threadExecutor) {
        return new ScanBarcodeUseCase(postExecutionThread, threadExecutor, boardingRepository);
    }

    @Provides
    static ScanBarcodePresenter provideScanBarcodePresenter(ScanBarcodeView scanBarcodeView, ScanBarcodeUseCase scanBarcodeUseCase) {
        return new ScanBarcodePresenterImpl(scanBarcodeView, scanBarcodeUseCase);
    }

    @Binds
    abstract ScanBarcodeView bindScanBarcodeView(ScanBarcodeFragment scanBarcodeFragment);

}
