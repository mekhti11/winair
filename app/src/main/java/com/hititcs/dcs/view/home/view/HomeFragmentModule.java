package com.hititcs.dcs.view.home.view;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class HomeFragmentModule {

  @Provides
  static HomeContract.HomePresenter provideHomePresenter(HomeContract.HomeView view) {
    return new HomePresenterImpl(view);
  }

  @Binds
  abstract HomeContract.HomeView bindHomeView(HomeFragment homeFragment);
}
