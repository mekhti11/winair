package com.hititcs.dcs.data.store.login;

import com.hititcs.dcs.data.remote.LoginRemote;
import com.hititcs.dcs.domain.model.AuthModel;
import com.hititcs.dcs.domain.model.LoginRequest;
import io.reactivex.Completable;
import io.reactivex.Single;
import javax.inject.Inject;

public class LoginRemoteDataStore implements LoginDataStore {

  private final LoginRemote loginRemote;

  @Inject
  public LoginRemoteDataStore(LoginRemote loginRemote) {
    this.loginRemote = loginRemote;
  }

  @Override
  public Single<AuthModel> login(LoginRequest loginRequest) {
    return loginRemote.login(loginRequest);
  }

  @Override
  public Completable saveAuth(AuthModel authModel) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Completable clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Single<Boolean> isCached() {
    throw new UnsupportedOperationException();
  }
}
