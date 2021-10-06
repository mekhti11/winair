package com.hititcs.dcs.util;

public enum ErrorMessage {
  UNDEFINED_ERROR,
  BOARDING_ERROR,
  FLIGHT_MISMATCH,
  SEAT_CHANGED,
  INVALID_FLIGHT,
  ALREADY_BOARDED;

  public String normalizeName() {
    return super.name().replace("_", " ");
  }
}
