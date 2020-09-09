package com.hititcs.dcs.domain.repository;

import com.hititcs.dcs.domain.model.FlightDetail;
import com.hititcs.dcs.domain.model.FlightListRequest;
import com.hititcs.dcs.domain.model.GetFlightsOutputDto;
import com.hititcs.dcs.domain.model.GetSeatMapOutputDto;

import io.reactivex.Single;

public interface FlightRepository {

    Single<FlightDetail> getFlightDetail(String id);

    Single<GetFlightsOutputDto> getFlights(FlightListRequest flightListRequest);

    Single<GetSeatMapOutputDto> getSeatMap(String id);
}
