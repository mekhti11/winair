package com.hititcs.dcs.cache;

import com.hititcs.dcs.data.cache.LoginCache;
import com.hititcs.dcs.data.shared.AuthManager;
import com.hititcs.dcs.domain.model.AuthModel;
import com.hititcs.dcs.domain.model.LoginRequest;
import io.reactivex.Completable;
import io.reactivex.Single;
import javax.inject.Inject;

public class LoginCacheImpl implements LoginCache {

  private final AuthManager authManager;

  @Inject
  public LoginCacheImpl(AuthManager authManager) {
    this.authManager = authManager;
  }

  @Override
  public Single<AuthModel> login(LoginRequest loginRequest) {
    return Single.defer(() -> Single.just(authManager.getAuthModel()))
        .filter(authModel -> authModel != null)
        .toSingle();
  }

  @Override
  public Completable saveAuth(AuthModel authModel) {
    return Completable.defer(() -> {
      authManager.saveAuthModel(authModel);
      return Completable.complete();
    });
  }

  @Override
  public Completable clear() {
    return Completable.defer(() -> {
      authManager.clear();
      return Completable.complete();
    });
  }

  @Override
  public Single<Boolean> isCached() {
    return Single
        .defer(() -> Single.just(authManager.isContain()));
  }
}
