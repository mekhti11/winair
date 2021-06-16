package com.hititcs.dcs.util;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.hititcs.dcs.R;
import timber.log.Timber;

public class FragmentUtils {

  private FragmentUtils() {

  }

  public static void replaceFragment(@Nullable FragmentManager fragmentManager, Fragment fragment) {
    replaceFragment(fragmentManager, fragment, fragment.getClass().getSimpleName());
  }

  public static void replaceFragment(@Nullable FragmentManager fragmentManager, Fragment fragment,
      String tag) {
    if (fragmentManager == null) {
      return;
    }
    fragmentManager
        .beginTransaction()
        .replace(R.id.content_frame, fragment, tag)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        .commit();
  }

  public static void replaceFragmentWithStack(@Nullable FragmentManager fragmentManager,
      Fragment fragment) {
    replaceFragmentWithStack(fragmentManager, fragment, fragment.getClass().getSimpleName());
  }

  public static void replaceFragmentWithStack(@Nullable FragmentManager fragmentManager,
      Fragment fragment,
      String tag) {
    if (fragmentManager == null) {
      return;
    }
    fragmentManager
        .beginTransaction()
        .replace(R.id.content_frame, fragment, tag)
        .addToBackStack(tag)
        .commit();
  }

  @Nullable
  public static Fragment getVisibleFragment(@Nullable FragmentManager fragmentManager) {
    if (fragmentManager == null) {
      return null;
    }
    return fragmentManager.findFragmentById(R.id.content_frame);
  }

  public static void detacthFragment(FragmentManager fragmentManager, String tag) {
    if (fragmentManager == null) {
      return;
    }
    Fragment fragment = fragmentManager.findFragmentByTag(tag);
    if (fragment != null) {
      Timber.d("%s fragment remove", tag);
      fragmentManager.beginTransaction()
          .detach(fragment)
          .commit();
    }
  }

  public static void removeFragment(FragmentManager fragmentManager, String tag) {
    if (fragmentManager == null) {
      return;
    }
    Fragment fragment = fragmentManager.findFragmentByTag(tag);
    if (fragment != null) {
      Timber.d("%s fragment remove", tag);
      fragmentManager.beginTransaction()
          .remove(fragment)
          .commit();
    }
  }

  public static boolean containFragmentByTag(FragmentManager fragmentManager, String tag) {
    if (fragmentManager == null) {
      return false;
    }
    return fragmentManager.findFragmentByTag(tag) != null;
  }

  public static void popBack(FragmentManager fragmentManager) {
    if (fragmentManager == null) {
      return;
    }
    try {
      fragmentManager.popBackStackImmediate();
    } catch (IllegalStateException ignored) {
      Timber.e(ignored);
    }
  }
}
