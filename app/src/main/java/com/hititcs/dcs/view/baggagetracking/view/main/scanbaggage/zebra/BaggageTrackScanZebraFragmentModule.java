package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.zebra;

import com.hititcs.dcs.view.baggagetracking.domain.interceptor.ScanBaggageBarcodeUseCase;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class BaggageTrackScanZebraFragmentModule {

  @Provides
  static BaggageTrackScanZebraContract.BaggageTrackScanZebraPresenter provideBaggageTrackScanZebraPresenter(
      BaggageTrackScanZebraContract.BaggageTrackScanZebraView view,
      ScanBaggageBarcodeUseCase scanBaggageBarcodeUseCase) {
    return new BaggageTrackScanZebraPresenterImpl(view, scanBaggageBarcodeUseCase);
  }

  @Binds
  abstract BaggageTrackScanZebraContract.BaggageTrackScanZebraView bindBaggageTrackScanZebraView(
      BaggageTrackScanZebraFragment baggageTrackScanZebraFragment);
}
