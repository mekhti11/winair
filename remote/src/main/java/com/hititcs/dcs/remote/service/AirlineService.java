package com.hititcs.dcs.remote.service;

import com.hititcs.dcs.data.remote.AirlineRemote;
import com.hititcs.dcs.domain.model.AirlineListResponse;
import com.hititcs.dcs.remote.api.AirlineApi;
import io.reactivex.Single;
import javax.inject.Inject;

  public class AirlineService implements AirlineRemote {

    private AirlineApi airlineApi;

    @Inject
    public AirlineService(AirlineApi airlineApi) {
      this.airlineApi = airlineApi;
    }

    @Override
    public Single<AirlineListResponse> getAirlines() {
      return airlineApi.getAirlines();
    }
  }
