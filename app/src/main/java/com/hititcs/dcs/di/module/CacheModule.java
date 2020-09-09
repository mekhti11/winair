package com.hititcs.dcs.di.module;

import com.google.gson.Gson;
import com.hititcs.dcs.cache.AuthManagerImpl;
import com.hititcs.dcs.cache.LoginCacheImpl;
import com.hititcs.dcs.data.cache.LoginCache;
import com.hititcs.dcs.data.shared.AuthManager;
import com.hititcs.dcs.data.shared.PreferenceHelper;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class CacheModule {

  @Provides
  static AuthManager provideAuthManager(PreferenceHelper preferenceHelper, Gson gson) {
    return new AuthManagerImpl(preferenceHelper, gson);
  }

  @Binds
  public abstract LoginCache crewCache(LoginCacheImpl loginCache);


}
