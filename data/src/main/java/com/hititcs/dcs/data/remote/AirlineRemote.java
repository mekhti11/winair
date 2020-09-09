package com.hititcs.dcs.data.remote;

import com.hititcs.dcs.domain.model.AirlineListResponse;
import io.reactivex.Single;

public interface AirlineRemote {
  Single<AirlineListResponse> getAirlines();
}
