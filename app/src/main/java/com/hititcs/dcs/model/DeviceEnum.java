package com.hititcs.dcs.model;

public enum DeviceEnum {
  ZEBRA("ZEBRA"),
  CAMERA("CAMERA"),
  K_RANGER("K_RANGER");

  private String value;

  DeviceEnum(String value) {
    this.value = value;
  }

  public static DeviceEnum fromValue(String text) {
    for (DeviceEnum b : DeviceEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
