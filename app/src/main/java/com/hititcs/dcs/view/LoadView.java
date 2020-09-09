package com.hititcs.dcs.view;

import androidx.annotation.StringRes;

public interface LoadView extends BaseView {

  void showLoading();

  void showProgressDialog(@StringRes int title, @StringRes int message);

  void showProgressDialog();

  void hideProgressDialog();

  void hideLoading();
}
