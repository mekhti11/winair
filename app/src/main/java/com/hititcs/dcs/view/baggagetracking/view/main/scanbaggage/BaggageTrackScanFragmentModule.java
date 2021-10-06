package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage;

import com.hititcs.dcs.view.baggagetracking.domain.interceptor.ScanBaggageBarcodeUseCase;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class BaggageTrackScanFragmentModule {

  @Provides
  static BaggageTrackScanContract.BaggageTrackScanPresenter provideBaggageTrackScanPresenter(
      BaggageTrackScanContract.BaggageTrackScanView view,
      ScanBaggageBarcodeUseCase scanBaggageBarcodeUseCase) {
    return new BaggageTrackScanPresenterImpl(view, scanBaggageBarcodeUseCase);
  }

  @Binds
  abstract BaggageTrackScanContract.BaggageTrackScanView bindBaggageTrackScanView(
      BaggageTrackScanFragment baggageTrackScanFragment);
}
