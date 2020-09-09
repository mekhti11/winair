package com.hititcs.dcs.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CompressUtil {


  public static Bitmap deCompressBitmap(String base64Image) throws IOException {
    Bitmap decodedBitmap = null;
    if (base64Image != null) {
      ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.decode(base64Image, Base64.DEFAULT));
      decodedBitmap = BitmapFactory.decodeStream(inputStream);
      inputStream.close();
    }
    return decodedBitmap;
  }


}
