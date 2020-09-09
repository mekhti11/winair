package com.hititcs.dcs.remote.interceptors;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class DefaultHeaderInterceptor implements Interceptor {

  private static final String CONTENT_TYPE_LABEL = "Content-Type";
  private static final String CONTENT_TYPE_VALUE = "application/json";
  private static final String ACCEPT_CHARSET_LABEL = "Accept-Charset";
  private static final String ACCEPT_CHARSET_VALUE = "utf-8";

  private final String deviceId;

  public DefaultHeaderInterceptor(String deviceId) {
    this.deviceId = deviceId;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    Request.Builder requestBuilder = request.newBuilder()
        .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE)
        .addHeader(ACCEPT_CHARSET_LABEL, ACCEPT_CHARSET_VALUE);
    request = requestBuilder.method(request.method(), request.body())
        .build();

    return chain.proceed(request);
  }
}
