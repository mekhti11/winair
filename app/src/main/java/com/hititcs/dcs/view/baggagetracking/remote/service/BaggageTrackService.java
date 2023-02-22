package com.hititcs.dcs.view.baggagetracking.remote.service;

import com.hititcs.dcs.view.baggagetracking.domain.model.GetTrackingBaggageLocationNamesOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageRequest;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BaggageTrackService {

  @GET("airport/api/v1/trackingBaggage/getLocationNames")
  Single<GetTrackingBaggageLocationNamesOutputDto> getLocationNames();

  @POST("airport/api/v1/trackingBaggage/scan")
  Single<ScanBaggageOutputDto> scanBaggageBarcode(@Body ScanBaggageRequest scanBaggageRequest);
}
