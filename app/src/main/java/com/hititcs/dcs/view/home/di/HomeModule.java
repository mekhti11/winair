package com.hititcs.dcs.view.home.di;

import com.hititcs.dcs.view.baggagetracking.view.BaggageTrackScanFragment;
import com.hititcs.dcs.view.baggagetracking.view.BaggageTrackScanFragmentModule;
import com.hititcs.dcs.view.home.view.HomeActivity;
import com.hititcs.dcs.view.home.view.HomeFragment;
import com.hititcs.dcs.view.home.view.HomeFragmentModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeModule {
  @ContributesAndroidInjector(modules = HomeFragmentModule.class)
  abstract HomeActivity bindHomeActivity();

  @ContributesAndroidInjector(modules = BaggageTrackScanFragmentModule.class)
  abstract BaggageTrackScanFragment baggageTrackScanFragment();

  @ContributesAndroidInjector(modules = HomeFragmentModule.class)
  abstract HomeFragment homeFragment();
}
