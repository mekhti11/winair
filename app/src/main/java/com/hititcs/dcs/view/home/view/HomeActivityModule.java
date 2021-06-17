package com.hititcs.dcs.view.home.view;

import com.hititcs.dcs.view.baggagetracking.view.scanbaggage.BaggageTrackScanFragment;
import com.hititcs.dcs.view.baggagetracking.view.scanbaggage.BaggageTrackScanFragmentModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeActivityModule {
  @ContributesAndroidInjector(modules = BaggageTrackScanFragmentModule.class)
  abstract BaggageTrackScanFragment baggageTrackScanFragment();

  @ContributesAndroidInjector(modules = HomeFragmentModule.class)
  abstract HomeFragment bindHomeFragment();
}
