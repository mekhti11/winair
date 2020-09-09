package com.hititcs.dcs.domain.repository;

import com.hititcs.dcs.domain.model.AirlineListResponse;
import io.reactivex.Single;

public interface AirlineRepository {
  Single<AirlineListResponse> getAirlines();
}
