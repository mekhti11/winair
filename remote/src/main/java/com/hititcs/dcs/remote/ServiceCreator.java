package com.hititcs.dcs.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hititcs.dcs.remote.interceptors.DefaultHeaderInterceptor;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.X509TrustManager;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceCreator {

  private OkHttpClient client;
  private Retrofit.Builder retrofitBuilder;

  public ServiceCreator(String baseUrl, boolean debug, String deviceId,
      Interceptor tokenInterceptor, Interceptor stethoInterceptor, boolean rx) {

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    OkHttpClient.Builder builder = new OkHttpClient.Builder()
        .writeTimeout(3, TimeUnit.MINUTES)
        .readTimeout(3, TimeUnit.MINUTES)
        .connectTimeout(3, TimeUnit.MINUTES)
        .addNetworkInterceptor(stethoInterceptor)
        .addInterceptor(new DefaultHeaderInterceptor(deviceId))
        .addInterceptor(
            logging.setLevel(
                debug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE));

    if (tokenInterceptor != null) {
      builder.addInterceptor(tokenInterceptor);
    }

    client = builder.build();
    retrofitBuilder = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(buildGson()));
    if (rx) {
      retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }
  }

  public <S> S createService(Class<S> serviceClass) {
    Retrofit retrofit = retrofitBuilder.client(client).build();
    return retrofit.create(serviceClass);
  }

  private Gson buildGson() {
    GsonBuilder gsonBuilder =
        new GsonBuilder();
//    gsonBuilder.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter());
//    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
//    gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
//    gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter());
    return gsonBuilder.create();
  }


}
