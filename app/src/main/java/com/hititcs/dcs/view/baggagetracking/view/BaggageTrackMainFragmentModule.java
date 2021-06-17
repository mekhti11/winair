package com.hititcs.dcs.view.baggagetracking.view;

import com.hititcs.dcs.view.baggagetracking.domain.interceptor.GetLocationNamesUseCase;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class BaggageTrackMainFragmentModule {

  @Provides
  static BaggageTrackMainContract.BaggageTrackMainPresenter provideBaggageTrackMainPresenter(
      BaggageTrackMainContract.BaggageTrackMainView view,
      GetLocationNamesUseCase getLocationNamesUseCase) {
    return new BaggageTrackMainPresenterImpl(view,
        getLocationNamesUseCase);
  }

  @Binds
  abstract BaggageTrackMainContract.BaggageTrackMainView bindBaggageTrackMainView(
      BaggageTrackMainMainFragment baggageTrackMainFragment);
}
