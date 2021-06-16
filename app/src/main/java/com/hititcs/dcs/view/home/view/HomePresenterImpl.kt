package com.hititcs.dcs.view.home.view

import javax.inject.Inject

class HomePresenterImpl @Inject constructor(private var view: HomeContract.HomeView) :
  HomeContract.HomePresenter {

  override fun getView(): HomeContract.HomeView {
    return view
  }

  override fun setView(view: HomeContract.HomeView) {
    this.view = view
  }

  override fun dispose() {
  }
}