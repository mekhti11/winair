package com.hititcs.dcs.util;

import android.text.TextUtils;

public class StringUtils {

  private StringUtils() {
    //no instance
  }

  public static int convertStringToInt(String value) {
    int convertedValue = 0;
    if (!TextUtils.isEmpty(value)) {
      convertedValue = Integer.valueOf(value);
    }
    return convertedValue;
  }

  public static String stringNullToEmpty(String value) {
    return value == null ? "" : value;
  }

  public static String stringNullToSpecialChar(String value, String specialChar) {
    return value == null ? specialChar : value;
  }

  public static boolean isBlank(CharSequence value) {
    return isBlank(value.toString());
  }

  public static boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

  public static boolean isEmpty(String value) {
    return value == null || value.isEmpty();
  }

  public static String wordCapitalize(String words) {

    String str = "";
    boolean isCap = false;

    for (int i = 0; i < words.length(); i++) {

      if (isCap) {
        str += words.toUpperCase().charAt(i);
      } else {
        if (i == 0) {
          str += words.toUpperCase().charAt(i);
        } else {
          str += words.toLowerCase().charAt(i);
        }
      }

      isCap = words.charAt(i) == ' ';
    }
    return str;
  }

  public static String firstLetterCapital(String value) {
    String lowerValue = value.toLowerCase();
    return String.format("%s%s", lowerValue.substring(0, 1).toUpperCase(), lowerValue.substring(1));
  }
}
