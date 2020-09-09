package com.hititcs.dcs.remote;

import com.hititcs.dcs.data.remote.LoginRemote;
import com.hititcs.dcs.domain.model.AuthModel;
import com.hititcs.dcs.domain.model.LoginRequest;
import com.hititcs.dcs.remote.api.UserApi;
import com.hititcs.dcs.remote.mapper.MemberMapper;
import io.reactivex.Single;
import javax.inject.Inject;

public class LoginRemoteImpl implements LoginRemote {

  private final UserApi userControllerApi;
  private final MemberMapper memberMapper;

  @Inject
  public LoginRemoteImpl(UserApi userControllerApi, MemberMapper memberMapper) {
    this.userControllerApi = userControllerApi;
    this.memberMapper = memberMapper;
  }

  @Override
  public Single<AuthModel> login(LoginRequest loginRequest) {
    return userControllerApi.loginRx(loginRequest)
        .filter(loginResponse -> loginResponse != null)
        .toSingle()
        .map(memberMapper::mapToEntity);
  }
}
