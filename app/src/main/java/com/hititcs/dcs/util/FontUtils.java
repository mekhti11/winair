package com.hititcs.dcs.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import androidx.annotation.FontRes;
import androidx.core.content.res.ResourcesCompat;
import com.hititcs.dcs.widget.CustomTypeFaceSpan;
import timber.log.Timber;

public class FontUtils {

  private FontUtils() {

  }

  public static SpannableString createSpannableString(Context context, String message,
      @FontRes int font) {
    Typeface typeface = null;
    try {
      typeface = ResourcesCompat.getFont(context, font);
    } catch (Exception e) {
      Timber.e(e);
    }
    SpannableString spannableString = new SpannableString(message);
    if (typeface != null) {
      CustomTypeFaceSpan customTypeFaceSpan = new CustomTypeFaceSpan(typeface);
      spannableString.setSpan(customTypeFaceSpan, 0, spannableString.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    return spannableString;
  }
}