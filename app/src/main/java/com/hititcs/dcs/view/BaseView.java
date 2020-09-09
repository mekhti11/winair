package com.hititcs.dcs.view;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

public interface BaseView {

  Context context();

  void showError(String message);

  void showError(@StringRes int messageId);

  default void showToast(String message) {
    Toast.makeText(context(), message, Toast.LENGTH_LONG).show();
  }

  default void showToast(@StringRes int messageId) {
    showToast(context().getString(messageId));
  }
}
