package com.hititcs.dcs.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class AndroidUtils {

  private AndroidUtils(){

  }

  public static DisplayMetrics getScreenMetrics(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics metrics = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(metrics);
    return metrics;
  }

}
