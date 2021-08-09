package com.hititcs.dcs.util;

import android.os.Build;

public class DeviceUtils {

  public static boolean isManufacturerZebra() {
    return !StringUtils.isEmpty(Build.MANUFACTURER) && (Build.MANUFACTURER.contains("zebra")
        || Build.MANUFACTURER.contains("Zebra") || Build.MANUFACTURER.contains("ZEBRA"));
  }
}
