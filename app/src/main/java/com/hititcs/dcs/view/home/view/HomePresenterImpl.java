package com.hititcs.dcs.view.home.view;

import javax.inject.Inject;

public class HomePresenterImpl implements HomeContract.HomePresenter {

  private HomeContract.HomeView view;

  @Inject
  public HomePresenterImpl(HomeContract.HomeView view) {
    this.view = view;
  }

  @Override public HomeContract.HomeView getView() {
    return view;
  }

  @Override public void setView(HomeContract.HomeView view) {
    this.view = view;
  }

  @Override public void dispose() {

  }
}
