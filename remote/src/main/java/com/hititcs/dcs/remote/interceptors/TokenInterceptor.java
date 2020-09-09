package com.hititcs.dcs.remote.interceptors;

import com.hititcs.dcs.data.shared.AuthManager;
import java.io.IOException;
import javax.inject.Inject;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

  private final AuthManager authManager;

  @Inject
  public TokenInterceptor(AuthManager authManager) {
    this.authManager = authManager;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request originalRequest = chain.request();
    String token = authManager.getAccessToken();
    if (token != null && !token.isEmpty()) {
      Request.Builder builder = originalRequest.newBuilder();
      builder.addHeader("Authorization", token);
      return chain.proceed(builder.build());
    }
    return chain.proceed(chain.request());
  }
}
