package com.hititcs.dcs.di.module;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import com.google.gson.Gson;
import com.hititcs.dcs.BuildConfig;
import com.hititcs.dcs.UIThread;
import com.hititcs.dcs.cache.PreferenceHelperImpl;
import com.hititcs.dcs.data.executor.JobExecutor;
import com.hititcs.dcs.data.shared.PreferenceHelper;
import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.domain.executor.ThreadExecutor;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public abstract class ApplicationModule {

  @Provides
  @Singleton
  static PostExecutionThread providePostExecutionThread(UIThread uiThread) {
    return uiThread;
  }

  @Provides
  @Singleton
  static ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
    return jobExecutor;
  }

  @Provides
  @Singleton
  static boolean provideDebug() {
    return BuildConfig.DEBUG;
  }

  @Provides
  @Singleton
  static String provideApiUrl() {
    return BuildConfig.API_BASE_URL;
  }

  @Provides
  @Singleton
  @Named("companyCode")
  static String provideCompanyCode() {
    return BuildConfig.COMPANY_CODE;
  }

  @Provides
  @Singleton
  static Gson provideGson() {
    return new Gson();
  }

  @Provides
  @Singleton
  static PreferenceHelper providePreferenceHelper(Context context) {
    return new PreferenceHelperImpl(context);
  }

  @Provides
  @Singleton
  @Named("deviceId")
  static String provideDeviceId(Context context) {
    return Settings.Secure.getString(context.getContentResolver(),
        Settings.Secure.ANDROID_ID);
  }

  @Binds
  abstract Context bindContext(Application application);
}
