package com.hititcs.dcs.data.store.login;

import com.hititcs.dcs.domain.model.AuthModel;
import com.hititcs.dcs.domain.model.LoginRequest;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface LoginDataStore {

  Single<AuthModel> login(LoginRequest loginRequest);

  Completable saveAuth(AuthModel authModel);

  Completable clear();

  Single<Boolean> isCached();
}
