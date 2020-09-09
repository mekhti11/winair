package com.hititcs.dcs.view;

public interface LoadPresenter<T extends LoadView> extends Presenter<T> {

  default void showViewLoading() {
    if (getView() != null) {
      getView().showLoading();
    }
  }

  default void hideViewLoading() {
    if (getView() != null) {
      getView().hideLoading();
    }
  }
}
