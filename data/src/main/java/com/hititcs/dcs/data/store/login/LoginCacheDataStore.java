package com.hititcs.dcs.data.store.login;

import com.hititcs.dcs.data.cache.LoginCache;
import com.hititcs.dcs.domain.model.AuthModel;
import com.hititcs.dcs.domain.model.LoginRequest;
import io.reactivex.Completable;
import io.reactivex.Single;
import javax.inject.Inject;

public class LoginCacheDataStore implements LoginDataStore {

  private final LoginCache loginCache;

  @Inject
  public LoginCacheDataStore(LoginCache loginCache) {
    this.loginCache = loginCache;
  }

  @Override
  public Single<AuthModel> login(LoginRequest loginRequest) {
    return loginCache.login(loginRequest);
  }

  @Override
  public Completable saveAuth(AuthModel authModel) {
    return loginCache.saveAuth(authModel);
  }

  @Override
  public Completable clear() {
    return loginCache.clear();
  }

  @Override
  public Single<Boolean> isCached() {
    return loginCache.isCached();
  }
}
