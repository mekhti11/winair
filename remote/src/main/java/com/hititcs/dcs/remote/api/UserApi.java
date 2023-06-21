package com.hititcs.dcs.remote.api;

import com.hititcs.dcs.domain.model.LoginRequest;
import com.hititcs.dcs.domain.model.LoginResponse;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {

  @POST("public/users/login")
  Single<LoginResponse> loginRx(@Body LoginRequest request);

  @POST("public/users/login")
  Call<LoginResponse> login(@Body LoginRequest request);

}
