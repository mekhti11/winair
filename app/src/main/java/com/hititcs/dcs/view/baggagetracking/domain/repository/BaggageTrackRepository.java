package com.hititcs.dcs.view.baggagetracking.domain.repository;

import com.hititcs.dcs.view.baggagetracking.domain.model.GetTrackingBaggageLocationNamesOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageRequest;
import io.reactivex.Single;
import retrofit2.http.Body;

public interface BaggageTrackRepository {
  Single<GetTrackingBaggageLocationNamesOutputDto> getLocationNames();

  Single<ScanBaggageOutputDto> scanBaggageBarcode(@Body ScanBaggageRequest scanBaggageRequest);
}
