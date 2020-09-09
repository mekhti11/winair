package com.hititcs.dcs.util;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {

  public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
  public static final int MY_PERMISSIONS_REQUEST_CAMERA = 124;

  public static boolean checkPermission(AppCompatActivity activity, int requestId,
                                        String permission) {
    if (!isPermissionGranted(activity, permission)) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestId);
      } else {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestId);
      }
      return false;
    } else {
      return true;
    }
  }

  public static boolean isPermissionGranted(Context context, String permission) {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
  }

}