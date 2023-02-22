package com.hititcs.dcs;

import android.content.Context;
import android.util.Log;
import androidx.multidex.MultiDex;
import com.facebook.stetho.Stetho;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.hititcs.dcs.di.component.ApplicationComponent;
import com.hititcs.dcs.di.component.DaggerApplicationComponent;
import com.jakewharton.threetenabp.AndroidThreeTen;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.reactivex.plugins.RxJavaPlugins;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import timber.log.Timber;

public class DcsApplication extends DaggerApplication {

  @Override
  public void onCreate() {
    super.onCreate();
    AndroidThreeTen.init(this);
    Stetho.initializeWithDefaults(this);
    if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")) {
      Timber.plant(new Timber.DebugTree());
      FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false);
    }
    if (!BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")) {
      Timber.plant(new CrashReportingTree());
    }
    RxJavaPlugins.setErrorHandler(throwable -> {
          if (throwable instanceof UnknownHostException) {
            Timber.d("Unknown Host Exception");
          } else if (throwable instanceof SSLHandshakeException) {
            Timber.d("SSLHandshake Exception %s", throwable.getMessage());
          } else if (throwable instanceof ConnectException) {
            Timber.d("Connection Exception %s", throwable.getMessage());
          } else if (throwable instanceof SocketTimeoutException) {
            Timber.d("Socket Timeout Exception %s", throwable.getMessage());
          } else if (throwable instanceof SSLPeerUnverifiedException) {
            Timber.d("SSL Peer Unverified Exception %s", throwable.getMessage());
          } else if (throwable instanceof SSLException) {
            Timber.d("SSL Exception %s", throwable.getMessage());
          } else {
            Timber.e(throwable, "Rx Error Handler Non-Fatal Exception");
          }
        }
    );
  }

  @Override
  protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
    ApplicationComponent appComponent = DaggerApplicationComponent.builder().application(this)
        .build();
    appComponent.inject(this);
    return appComponent;
//    return null;
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  class CrashReportingTree extends Timber.Tree {

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
      if (priority == Log.VERBOSE || priority == Log.DEBUG) {
        return;
      }
      FirebaseCrashlytics.getInstance().log(message);
      if (t != null) {
        FirebaseCrashlytics.getInstance().recordException(t);
      }
    }
  }
}
