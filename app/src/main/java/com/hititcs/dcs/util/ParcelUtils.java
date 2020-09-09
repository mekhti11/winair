package com.hititcs.dcs.util;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import org.parceler.Parcels;

public class ParcelUtils {

  private ParcelUtils() {

  }

  public static <T> T unwrap(Bundle bundle, String key) {
    if (!bundle.containsKey(key)) {
      return null;
    }
    return unwrap(bundle.getParcelable(key));
  }

  public static <T> T unwrap(Intent intent, String key) {
    if (!intent.hasExtra(key)) {
      return null;
    }
    return unwrap(intent.getParcelableExtra(key));
  }

  public static <T, P extends Parcelable> T unwrap(P parcel) {
    return Parcels.unwrap(parcel);
  }

  public static <T> void wrap(Bundle bundle, String key, T data) {
    bundle.putParcelable(key, wrap(data));
  }

  public static <T> void wrap(Intent intent, String key, T data) {
    intent.putExtra(key, wrap(data));
  }

  public static <T> Parcelable wrap(T t) {
    return Parcels.wrap(t);
  }


}
