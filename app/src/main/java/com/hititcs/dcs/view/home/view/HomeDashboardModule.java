package com.hititcs.dcs.view.home.view;

import com.hititcs.dcs.view.baggagetracking.view.BaggageTrackScanFragment;
import com.hititcs.dcs.view.baggagetracking.view.BaggageTrackScanFragmentModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeDashboardModule {

  @ContributesAndroidInjector(modules = BaggageTrackScanFragmentModule.class)
  abstract BaggageTrackScanFragment baggageTrackScanFragment();
}
