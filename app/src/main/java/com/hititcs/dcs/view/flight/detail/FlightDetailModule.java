package com.hititcs.dcs.view.flight.detail;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FlightDetailModule {

  @ContributesAndroidInjector(modules = FlightDetailFragmentModule.class)
  abstract FlightDetailFragment flightDetailFragment();
}
