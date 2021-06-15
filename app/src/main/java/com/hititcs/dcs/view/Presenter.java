package com.hititcs.dcs.view;

import android.content.Intent;
import androidx.annotation.StringRes;
import com.hititcs.dcs.view.login.LoginActivity;

public interface Presenter<T extends BaseView> {

  T getView();

  void setView(T view);

  void dispose();

  default void destroyView() {
    setView(null);
    dispose();
  }

  default void showError(String errorMessage) {
    if (getView() != null) {
      getView().showError(errorMessage);
    }
  }

  default void showError(@StringRes int errorMessageId) {
    if (getView() != null) {
      getView().showError(errorMessageId);
    }
  }

  default void show404Error() {
    showError("Error 404");
  }

  default void showTokenErrorAndNavigateToLogin() {
    if (getView() != null) {
      Intent intent = new Intent(getView().context(), LoginActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      intent.setAction(LoginActivity.TOKEN_ERROR_ACTION);
      getView().context().startActivity(intent);
    }
  }

  default void noInternetConnection() {
    showError("No internet connection");
  }
}
