package com.hititcs.dcs.widget;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class CustomTypeFaceSpan extends TypefaceSpan {
  private Typeface typeface;

  public CustomTypeFaceSpan(Typeface typeface) {
    super("");
    this.typeface = typeface;
  }

  private static void applyTypeFace(Paint paint, Typeface tf) {
    paint.setTypeface(tf);
  }

  @Override
  public void updateDrawState(TextPaint ds) {
    applyTypeFace(ds, typeface);
  }

  @Override
  public void updateMeasureState(TextPaint paint) {
    applyTypeFace(paint, typeface);
  }
}
