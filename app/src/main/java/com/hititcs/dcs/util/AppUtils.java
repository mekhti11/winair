package com.hititcs.dcs.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.DrawableRes;
import com.hititcs.dcs.view.main.MainActivity;
import timber.log.Timber;

public class AppUtils {

  private AppUtils() {
  }

  public static int getDisplayHeight(Context context) {
    Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
        .getDefaultDisplay();
    Point point = new Point();
    display.getSize(point);
    return point.y;
  }

  public static void hideKeyboardFrom(Context context, View view) {
    try {
      InputMethodManager imm =
          (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    } catch (Exception e) {
      Timber.e(e);
    }
  }

  public static void openHomeActivity(Activity activity) {
    Intent intent = new Intent(activity, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    activity.startActivity(intent);
    activity.finish();
  }

  public static void setFullScreenBackground(Window window, @DrawableRes int background) {
    window.setBackgroundDrawableResource(background);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      window.setStatusBarColor(Color.TRANSPARENT);
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      int flags = window.getDecorView().getSystemUiVisibility();
      flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
      window.getDecorView().setSystemUiVisibility(flags);
    }
  }

  public static int convertDpToPixel(float dp, Context context) {
    return (int) (dp * ((float) context.getResources().getDisplayMetrics().densityDpi
        / DisplayMetrics.DENSITY_DEFAULT));
  }

  public static void enableDisabledView(View view, boolean enabled) {
    view.setEnabled(enabled);
    if (view instanceof ViewGroup) {
      ViewGroup group = (ViewGroup) view;
      for (int idx = 0; idx < group.getChildCount(); idx++) {
        enableDisabledView(group.getChildAt(idx), enabled);
      }
    }
  }

  public static Uri getCompanyLogoUri(Context context){
    return Uri.parse("file://"
        + context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString()
        + "/companyLogo.png");
  }
}
