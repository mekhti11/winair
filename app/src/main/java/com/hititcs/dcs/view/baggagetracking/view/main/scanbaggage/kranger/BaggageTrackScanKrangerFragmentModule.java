package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.kranger;

import com.hititcs.dcs.view.baggagetracking.domain.interceptor.ScanBaggageBarcodeUseCase;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class BaggageTrackScanKrangerFragmentModule {

  @Provides
  static BaggageTrackScanKrangerContract.BaggageTrackScanKrangerPresenter provideBaggageTrackScanKrangerPresenter(
      BaggageTrackScanKrangerContract.BaggageTrackScanKrangerView view,
      ScanBaggageBarcodeUseCase scanBaggageBarcodeUseCase) {
    return new BaggageTrackScanKrangerPresenterImpl(view, scanBaggageBarcodeUseCase);
  }

  @Binds
  abstract BaggageTrackScanKrangerContract.BaggageTrackScanKrangerView bindBaggageTrackScanKrangerView(
      BaggageTrackScanKrangerFragment baggageTrackScanKrangerFragment);
}
