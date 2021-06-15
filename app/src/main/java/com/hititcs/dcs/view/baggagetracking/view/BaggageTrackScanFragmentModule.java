package com.hititcs.dcs.view.baggagetracking.view;

import com.hititcs.dcs.view.baggagetracking.domain.interceptor.GetLocationNamesUseCase;
import com.hititcs.dcs.view.baggagetracking.domain.interceptor.ScanBaggageBarcodeUseCase;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class BaggageTrackScanFragmentModule {

  @Provides
  static BaggageTrackScanContract.BaggageTrackPresenter provideBaggageTrackPresenter(
      BaggageTrackScanContract.BaggageTrackView view,
      ScanBaggageBarcodeUseCase scanBaggageBarcodeUseCase,
      GetLocationNamesUseCase getLocationNamesUseCase) {
    return new BaggageTrackScanPresenterImpl(view, scanBaggageBarcodeUseCase,
        getLocationNamesUseCase);
  }

  @Binds
  abstract BaggageTrackScanContract.BaggageTrackView bindBaggageTrackView(
      BaggageTrackScanFragment baggageTrackScanFragment);
}
