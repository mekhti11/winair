package com.hititcs.dcs.view.baggagetracking.view.main;

import com.hititcs.dcs.view.baggagetracking.domain.interceptor.GetLocationNamesUseCase;
import com.hititcs.dcs.view.baggagetracking.domain.interceptor.ScanBaggageBarcodeUseCase;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class BaggageTrackMainFragmentModule {

  @Provides
  static BaggageTrackMainContract.BaggageTrackMainPresenter provideBaggageTrackMainPresenter(
      BaggageTrackMainContract.BaggageTrackMainView view,
      GetLocationNamesUseCase getLocationNamesUseCase,
      ScanBaggageBarcodeUseCase scanBaggageBarcodeUseCase) {
    return new BaggageTrackMainPresenterImpl(view,
        getLocationNamesUseCase, scanBaggageBarcodeUseCase);
  }

  @Binds
  abstract BaggageTrackMainContract.BaggageTrackMainView bindBaggageTrackMainView(
      BaggageTrackMainFragment baggageTrackMainFragment);
}
