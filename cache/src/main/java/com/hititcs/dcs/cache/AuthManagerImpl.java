package com.hititcs.dcs.cache;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.hititcs.dcs.data.shared.AuthManager;
import com.hititcs.dcs.data.shared.PreferenceHelper;
import com.hititcs.dcs.domain.model.AuthModel;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthManagerImpl implements AuthManager {

  private final PreferenceHelper preferenceHelper;
  private final Gson gson;
  private final String PREF_AUTH_MODEL = "auth_model";

  @Inject
  public AuthManagerImpl(PreferenceHelper preferenceHelper, Gson gson) {
    this.preferenceHelper = preferenceHelper;
    this.gson = gson;
  }


  @Override
  public void saveAuthModel(AuthModel authModel) {
    preferenceHelper.putString(PREF_AUTH_MODEL, gson.toJson(authModel));
  }

  @Override
  public AuthModel getAuthModel() {
    if (!preferenceHelper.isContain(PREF_AUTH_MODEL)) {
      return null;
    }
    String authString = preferenceHelper.getString(PREF_AUTH_MODEL);
    if (TextUtils.isEmpty(authString)) {
      return new AuthModel();
    }
    return gson.fromJson(authString, AuthModel.class);
  }

  @Override
  public String getAccessToken() {
    return getAuthModel() != null ? getAuthModel().getToken() : null;
  }

  @Override
  public void clear() {
    preferenceHelper.clear(PREF_AUTH_MODEL);
  }

  @Override
  public boolean isContain() {
    return preferenceHelper.isContain(PREF_AUTH_MODEL);
  }
}
