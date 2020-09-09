package com.hititcs.dcs.data.shared;

public interface PreferenceHelper {

  void putString(String key, String value);

  String getString(String key);

  void putBoolean(String key, boolean value);

  boolean getBoolean(String key);

  boolean clear(String key);

  boolean isContain(String key);

}
