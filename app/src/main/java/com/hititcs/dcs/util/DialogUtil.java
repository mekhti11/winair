package com.hititcs.dcs.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import com.hititcs.dcs.R;

public class DialogUtil {

  private DialogUtil(){

  }

  public static void showNativeAlertDialog(Context context, int title, int message,
      int positiveText,
      DialogInterface.OnClickListener positive, int negativeText,
      DialogInterface.OnClickListener negative) {
    AlertDialog.Builder builder;

    builder = new AlertDialog.Builder(context, R.style.NativeAlertDialogStyle);
    if (title != 0) {
      builder.setTitle(
          FontUtils.createSpannableString(context, context.getString(title), R.font.poppins_semibold));
    }
    builder.setMessage(
        FontUtils.createSpannableString(context, context.getString(message), R.font.poppins_light));
    if (negative != null) {
      builder
          .setNegativeButton(FontUtils.createSpannableString(context,
              context.getString(negativeText),
              R.font.poppins_semibold), negative);
    }
    if (positive != null) {
      builder
          .setPositiveButton(FontUtils.createSpannableString(context,
              context.getString(positiveText),
              R.font.poppins_semibold), positive);
      AlertDialog dialog = builder.create();
      builder.show();
    }
  }
}
