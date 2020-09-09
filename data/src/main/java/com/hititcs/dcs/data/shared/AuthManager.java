package com.hititcs.dcs.data.shared;


import com.hititcs.dcs.domain.model.AuthModel;

public interface AuthManager {

  void saveAuthModel(AuthModel authModel);

  AuthModel getAuthModel();

  String getAccessToken();

  void clear();

  boolean isContain();

}
