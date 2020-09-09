package com.hititcs.dcs.remote.service;

import com.hititcs.dcs.data.remote.FlightRemote;
import com.hititcs.dcs.domain.model.FlightDetail;
import com.hititcs.dcs.domain.model.FlightListRequest;
import com.hititcs.dcs.domain.model.GetFlightsOutputDto;
import com.hititcs.dcs.domain.model.GetSeatMapOutputDto;
import com.hititcs.dcs.remote.api.FlightResourceApi;
import io.reactivex.Single;
import javax.inject.Inject;

public class FlightService implements FlightRemote {

  private FlightResourceApi flightResourceApi;

  @Inject
  public FlightService(FlightResourceApi flightResourceApi) {
    this.flightResourceApi = flightResourceApi;
  }

  @Override
  public Single<FlightDetail> getFlightDetail(String id) {
    return flightResourceApi.getFlightDetail(id);
  }

  @Override
  public Single<GetFlightsOutputDto> getFlights(FlightListRequest flightListRequest) {
    return flightResourceApi.getFlights(flightListRequest.getEndDate(), flightListRequest.getStartDate());
  }

  @Override
  public Single<GetSeatMapOutputDto> getSeatMap(String id) {
    return flightResourceApi.getSeatMap(id);
  }
}
