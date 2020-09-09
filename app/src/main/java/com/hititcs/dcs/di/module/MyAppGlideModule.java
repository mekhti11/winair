package com.hititcs.dcs.di.module;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

import android.content.Context;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.hititcs.dcs.BuildConfig;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {

  @Override public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
    int memoryCacheSizeBytes = 1024 * 1024 * 10; // 10mb
    int diskCacheSizeBytes = 1024 * 1024 * 30; // 30mb
    builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
    builder.setDiskCache(
        new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
    builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888));
  }

  @Override public void registerComponents(@NonNull Context context, @NonNull Glide glide,
      @NonNull Registry registry) {
    HttpLoggingInterceptor loggingInterceptor =
        new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? BODY : NONE);
    OkHttpClient client = new OkHttpClient.Builder()
        .readTimeout(2, TimeUnit.MINUTES)
        .connectTimeout(2, TimeUnit.MINUTES)
        .addInterceptor(loggingInterceptor)
        .build();

    OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client);
    glide.getRegistry().replace(GlideUrl.class, InputStream.class, factory);
  }
}
