package com.hititcs.dcs.data.remote;

import com.hititcs.dcs.domain.model.AuthModel;
import com.hititcs.dcs.domain.model.LoginRequest;
import io.reactivex.Single;

public interface LoginRemote {

  Single<AuthModel> login(LoginRequest loginRequest);
}
