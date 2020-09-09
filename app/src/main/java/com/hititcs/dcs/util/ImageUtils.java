package com.hititcs.dcs.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hititcs.dcs.R;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import timber.log.Timber;

public class ImageUtils {

  public static Drawable loadImageUriAsDrawable(Context context, Uri uri){
    try {
      InputStream inputStream = context.getContentResolver().openInputStream(uri);
      return Drawable.createFromStream(inputStream, uri.toString());
    } catch (FileNotFoundException e) {
      Timber.e(e);
      return context.getResources().getDrawable(R.drawable.ic_logo_login);
    }
  }

}
