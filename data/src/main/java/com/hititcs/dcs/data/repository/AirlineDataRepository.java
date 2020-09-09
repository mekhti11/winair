package com.hititcs.dcs.data.repository;

import com.hititcs.dcs.data.remote.AirlineRemote;
import com.hititcs.dcs.domain.model.AirlineListResponse;
import com.hititcs.dcs.domain.repository.AirlineRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class AirlineDataRepository implements AirlineRepository {

  private AirlineRemote airlineRemote;

  @Inject
  public AirlineDataRepository(AirlineRemote airlineRemote) {
    this.airlineRemote = airlineRemote;
  }

  @Override
  public Single<AirlineListResponse> getAirlines() {
    return airlineRemote.getAirlines();
  }
}
