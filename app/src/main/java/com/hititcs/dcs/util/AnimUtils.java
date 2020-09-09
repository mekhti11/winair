package com.hititcs.dcs.util;

import android.view.View;

public class AnimUtils {

  public static final long SHOW_HIDE_ANIM_DURATION = 350;

  public static void animateShowView(final View view) {
    animateShowView(view, SHOW_HIDE_ANIM_DURATION);
  }

  public static void animateShowView(final View view, long duration) {
    view.setAlpha(0.0f);
    view.animate().alpha(1.0f).setDuration(duration);
  }
  public static void animateHideView(final View view, long duration) {
    view.setAlpha(1.0f);
    view.animate().alpha(0.0f).setDuration(duration);
  }
}
