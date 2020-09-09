package com.hititcs.dcs.subscriber;

import com.google.gson.Gson;
import com.hititcs.dcs.view.LoadPresenter;

import java.io.IOException;

import io.reactivex.subscribers.DisposableSubscriber;
import retrofit2.HttpException;
import timber.log.Timber;

public abstract class DefaultSubscriber<T> extends DisposableSubscriber<T> implements MySubscriber<T> {

  private LoadPresenter presenter;

  protected DefaultSubscriber(LoadPresenter presenter) {
    this.presenter = presenter;
  }


  @Override
  public void onNext(T tData) {
    if (tData != null) {
      Timber.i("Response ------------>>>>>> " + new Gson().toJson(tData));
      onResponse(tData);
    } else {
      onError(new Exception("Service response null"));
    }
  }

  @Override
  public void onError(Throwable e) {
    Timber.e(e, e.getMessage());
    if (presenter == null) {
      return;
    }
    presenter.hideViewLoading();
    if (e instanceof HttpException) {
      // We had non-2XX http error
      switch (((HttpException) e).code()) {
        case 404:
          presenter.show404Error();
          break;
        default:
          presenter.showError(e.getMessage());
      }
    } else if (e instanceof IOException) {
      presenter.noInternetConnection();
    } else {
      // A network or conversion error happened
      presenter.showError(e.getMessage());
    }
  }

  @Override
  public void onComplete() {
    if (presenter != null) {
      presenter.hideViewLoading();
    }
  }
}
