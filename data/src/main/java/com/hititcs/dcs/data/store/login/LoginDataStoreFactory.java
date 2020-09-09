package com.hititcs.dcs.data.store.login;

import javax.inject.Inject;

public class LoginDataStoreFactory {

  private final LoginRemoteDataStore crewRemoteDataStore;
  private final LoginCacheDataStore crewCacheDataStore;

  @Inject
  public LoginDataStoreFactory(LoginRemoteDataStore crewRemoteDataStore,
      LoginCacheDataStore crewCacheDataStore) {
    this.crewRemoteDataStore = crewRemoteDataStore;
    this.crewCacheDataStore = crewCacheDataStore;
  }

  public LoginDataStore getDataStore(boolean isCached) {
    if (isCached) {
      return getCrewCacheDataStore();
    }
    return getCrewRemoteDataStore();
  }

  public LoginCacheDataStore getCrewCacheDataStore() {
    return crewCacheDataStore;
  }

  public LoginRemoteDataStore getCrewRemoteDataStore() {
    return crewRemoteDataStore;
  }
}
