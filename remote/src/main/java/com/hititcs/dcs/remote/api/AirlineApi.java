package com.hititcs.dcs.remote.api;

import com.hititcs.dcs.domain.model.AirlineListResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AirlineApi {

  @GET("airport/public/airlines")
  Single<AirlineListResponse> getAirlines();

}
