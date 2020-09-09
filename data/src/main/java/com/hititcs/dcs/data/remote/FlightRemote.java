package com.hititcs.dcs.data.remote;

import com.hititcs.dcs.domain.model.FlightDetail;
import com.hititcs.dcs.domain.model.FlightListRequest;
import com.hititcs.dcs.domain.model.GetFlightsOutputDto;
import com.hititcs.dcs.domain.model.GetSeatMapOutputDto;
import io.reactivex.Single;

public interface FlightRemote {

  Single<FlightDetail> getFlightDetail(String id);

  Single<GetFlightsOutputDto> getFlights(FlightListRequest FlightListRequest);

  Single<GetSeatMapOutputDto> getSeatMap(String id);
}
