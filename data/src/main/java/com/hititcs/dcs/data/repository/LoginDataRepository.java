package com.hititcs.dcs.data.repository;

import com.hititcs.dcs.data.store.login.LoginDataStoreFactory;
import com.hititcs.dcs.domain.model.AuthModel;
import com.hititcs.dcs.domain.model.LoginRequest;
import com.hititcs.dcs.domain.repository.LoginRepository;
import io.reactivex.Completable;
import io.reactivex.Single;
import javax.inject.Inject;

public class LoginDataRepository implements LoginRepository {

  LoginDataStoreFactory factory;

  @Inject
  public LoginDataRepository(LoginDataStoreFactory factory) {
    this.factory = factory;
  }

  @Override
  public Single<AuthModel> login(LoginRequest loginRequest) {
    return factory.getCrewCacheDataStore()
        .isCached()
        .flatMap(aBoolean -> factory.getDataStore(aBoolean).login(loginRequest))
        .filter(authModel -> authModel != null)
        .flatMapSingle(authModel -> saveAuth(authModel).toSingle(() -> authModel));
  }

  @Override
  public Completable saveAuth(AuthModel authModel) {
    return factory.getCrewCacheDataStore().saveAuth(authModel);
  }

  @Override
  public Completable clear() {
    return factory.getCrewCacheDataStore().clear();
  }
}
