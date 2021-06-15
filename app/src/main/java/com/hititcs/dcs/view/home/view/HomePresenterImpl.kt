package com.hititcs.dcs.view.home.view

import com.hititcs.dcs.view.home.view.HomeContract.HomePresenter
import com.hititcs.dcs.view.home.view.HomeContract.HomeView
import javax.inject.Inject

class HomePresenterImpl @Inject constructor(private var view: HomeView) : HomePresenter {

  override fun getView(): HomeView {
    return view
  }

  override fun setView(view: HomeView) {
    this.view = view
  }

  override fun dispose() {
  }
}