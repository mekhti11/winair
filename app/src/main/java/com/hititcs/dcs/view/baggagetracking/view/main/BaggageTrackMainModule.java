package com.hititcs.dcs.view.baggagetracking.view.main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BaggageTrackMainModule {
  @ContributesAndroidInjector(modules = BaggageTrackMainFragmentModule.class)
  abstract BaggageTrackMainFragment baggageTrackMainFragment();
}
