package com.hititcs.dcs.view.baggagetracking.view.scanbaggage;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BaggageTrackScanModule {
  @ContributesAndroidInjector(modules = BaggageTrackScanFragmentModule.class)
  abstract BaggageTrackScanFragment baggageTrackScanFragment();
}