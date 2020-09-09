package com.hititcs.dcs.view.flight;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FlightListModule {

  @ContributesAndroidInjector(modules = FlightListFragmentModule.class)
  abstract FlightListFragment flightListFragment();
}
