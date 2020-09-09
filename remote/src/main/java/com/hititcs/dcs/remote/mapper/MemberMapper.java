package com.hititcs.dcs.remote.mapper;

import com.hititcs.dcs.domain.mapper.Mapper;
import com.hititcs.dcs.domain.model.AuthModel;
import com.hititcs.dcs.domain.model.LoginResponse;
import javax.inject.Inject;

public class MemberMapper implements Mapper<AuthModel, LoginResponse> {

  @Inject
  public MemberMapper() {
  }

  @Override
  public LoginResponse mapFromEntity(AuthModel authModel) {
    return null;
  }

  @Override
  public AuthModel mapToEntity(LoginResponse loginResponse) {
    AuthModel member = new AuthModel();
    member.setToken(loginResponse.getToken());
    return member;
  }
}
