package com.hititcs.dcs.util;

import android.os.Build;

public class DeviceUtils {

  public static boolean isManufacturerZebra() {
    return !StringUtils.isEmpty(Build.MANUFACTURER) && (Build.MANUFACTURER.contains("zebra")
        || Build.MANUFACTURER.contains("Zebra") || Build.MANUFACTURER.contains("ZEBRA"));
  }

  public static boolean isManufacturerKranger() {
    return !StringUtils.isEmpty(Build.MANUFACTURER) && (Build.MANUFACTURER.contains("kranger")
        || Build.MANUFACTURER.contains("Kranger")
        || Build.MANUFACTURER.contains("KRANGER")
        || Build.MANUFACTURER.contains("k-ranger")
        || Build.MANUFACTURER.contains("K-ranger")
        || Build.MANUFACTURER.contains("K-RANGER"));
  }
}
