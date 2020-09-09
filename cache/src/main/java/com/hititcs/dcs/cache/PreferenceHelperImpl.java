package com.hititcs.dcs.cache;

import android.content.Context;
import android.content.SharedPreferences;
import com.hititcs.dcs.data.shared.PreferenceHelper;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferenceHelperImpl implements PreferenceHelper {

  private static final String PREF_BUFFER_PACKAGE_NAME = "com.hititcs.boarding.cache";
  private final SharedPreferences sharedPreferences;

  @Inject
  public PreferenceHelperImpl(Context context) {
    sharedPreferences = context.getSharedPreferences(PREF_BUFFER_PACKAGE_NAME, Context.MODE_PRIVATE);
  }

  @Override
  public void putString(String key, String value) {
    sharedPreferences.edit().putString(key, value).apply();
  }

  @Override
  public String getString(String key) {
    return sharedPreferences.getString(key, "");
  }

  @Override
  public void putBoolean(String key, boolean value) {
    sharedPreferences.edit().putBoolean(key, value).apply();
  }

  @Override
  public boolean getBoolean(String key) {
    return sharedPreferences.getBoolean(key, false);
  }

  @Override
  public boolean clear(String key) {
    return sharedPreferences.edit().remove(key).commit();
  }

  @Override
  public boolean isContain(String key) {
    return sharedPreferences.contains(key);
  }

}
