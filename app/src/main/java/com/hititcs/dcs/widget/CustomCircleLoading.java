package com.hititcs.dcs.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import com.hititcs.dcs.R;

public class CustomCircleLoading {
  private AlertDialog alertDialog;
  private ProgressBar progressBar;
  private Context context;

  public CustomCircleLoading(Context context) {
    this.context = context;
    AlertDialog.Builder builder =
        new AlertDialog.Builder(context, R.style.CircleProgressDialogStyle);
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.dialog_progres_bar, null);
    progressBar = view.findViewById(R.id.progress_loading);
    builder.setView(view);
    builder.setCancelable(false);
    alertDialog = builder.create();
  }

  public boolean isShowing() {
    return alertDialog != null && alertDialog.isShowing();
  }

  public AlertDialog show() {
    alertDialog.show();
    return alertDialog;
  }

  public void dismiss() {
    alertDialog.dismiss();
  }

  public Bundle saveState() {
    Bundle bundle = new Bundle();
    bundle.putParcelable("customProgressBar", progressBar.onSaveInstanceState());
    bundle.putBundle("customDialog", alertDialog.onSaveInstanceState());
    return bundle;
  }

  public void restoreState(Context context, Bundle bundle) {
    progressBar.onRestoreInstanceState(bundle.getParcelable("customProgressBar"));
    alertDialog.onRestoreInstanceState(bundle.getParcelable("customDialog"));
    this.context = context;
  }

  public void destroy() {
    progressBar.setIndeterminate(false);
    progressBar = null;
    alertDialog.dismiss();
    alertDialog = null;
    context = null;
  }
}
