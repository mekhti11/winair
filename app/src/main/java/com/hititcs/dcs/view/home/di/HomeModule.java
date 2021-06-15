package com.hititcs.dcs.view.home.di;

import com.hititcs.dcs.view.home.view.HomeActivity;
import com.hititcs.dcs.view.home.view.HomeDashboardModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeModule {
  @ContributesAndroidInjector(modules = HomeDashboardModule.class)
  abstract HomeActivity bindHomeActivity();
}
