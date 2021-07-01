package com.hititcs.dcs.util;

import android.text.TextUtils;
import com.hititcs.dcs.domain.model.KeyValue;
import java.util.List;

public class MessageUtils {

  private MessageUtils(){

  }

  public static String getMessage(String httpResponse) {

    if (!TextUtils.isEmpty(httpResponse)) {
      if (httpResponse.contains("VT-20313")) {
        return ErrorMessage.BOARDING_ERROR.normalizeName();
      } else if (httpResponse.contains("VT-20315")) {
        return ErrorMessage.ALREADY_BOARDED.normalizeName();
      } else if (httpResponse.contains("FLIGHT_MATCH_EXCEPTION")) {
        return ErrorMessage.FLIGHT_MISMATCH.normalizeName();
      } else if (httpResponse.contains("Your seat number has been changed")) {
        return ErrorMessage.SEAT_CHANGED.normalizeName();
      } else if (httpResponse.contains("Boarding error has already borded")) {
        return ErrorMessage.ALREADY_BOARDED.normalizeName();
      } else if (httpResponse.contains("Boarding error invalid flight")) {
        return ErrorMessage.INVALID_FLIGHT.normalizeName();
      }
    }
    return ErrorMessage.UNDEFINED_ERROR.normalizeName();
  }

  public static String getStringFromList(List<KeyValue> keyValues) {

    if (keyValues == null || keyValues.isEmpty()) {
      return "";
    }
    StringBuilder result = new StringBuilder();
    int counter = 0;
    for (KeyValue keyValue : keyValues) {
      counter++;
      result.append(keyValue.getKey()).append(keyValue.getValue());
      if (counter < keyValues.size()) {
        result.append(" / ");
      }
    }
    if (TextUtils.isEmpty(result)) {
      return "";
    }
    return result.toString();
  }
}
