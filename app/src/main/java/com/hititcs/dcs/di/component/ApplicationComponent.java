package com.hititcs.dcs.di.component;

import android.app.Application;
import com.hititcs.dcs.DcsApplication;
import com.hititcs.dcs.data.executor.JobExecutor;
import com.hititcs.dcs.data.shared.PreferenceHelper;
import com.hititcs.dcs.di.module.ActivityBuilder;
import com.hititcs.dcs.di.module.ApplicationModule;
import com.hititcs.dcs.di.module.CacheModule;
import com.hititcs.dcs.di.module.DataModule;
import com.hititcs.dcs.di.module.RemoteModule;
import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.view.baggagetracking.di.BaggageTrackModule;
import com.hititcs.dcs.view.home.di.HomeModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

@Singleton
@Component(modules = {
    AndroidSupportInjectionModule.class, ApplicationModule.class, ActivityBuilder.class,
    CacheModule.class, DataModule.class, RemoteModule.class, BaggageTrackModule.class,
    HomeModule.class
})
public interface ApplicationComponent extends AndroidInjector<DaggerApplication> {

  @Override
  void inject(DaggerApplication instance);

  void inject(DcsApplication application);

  PostExecutionThread postExecutionThread();

  JobExecutor jobExecutor();

  PreferenceHelper preferenceHelper();

  @Component.Builder
  interface Builder {

    @BindsInstance
    ApplicationComponent.Builder application(Application application);

    ApplicationComponent build();
  }
}
