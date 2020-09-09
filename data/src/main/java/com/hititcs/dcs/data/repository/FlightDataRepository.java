package com.hititcs.dcs.data.repository;

import com.hititcs.dcs.data.remote.FlightRemote;
import com.hititcs.dcs.domain.model.FlightDetail;
import com.hititcs.dcs.domain.model.FlightListRequest;
import com.hititcs.dcs.domain.model.GetFlightsOutputDto;
import com.hititcs.dcs.domain.model.GetSeatMapOutputDto;
import com.hititcs.dcs.domain.repository.FlightRepository;

import io.reactivex.Single;
import javax.inject.Inject;

public class FlightDataRepository implements FlightRepository {

    private FlightRemote flightRemote;

    @Inject
    public FlightDataRepository(FlightRemote flightRemote) {
        this.flightRemote = flightRemote;
    }

    @Override
    public Single<FlightDetail> getFlightDetail(String id) {
        return flightRemote.getFlightDetail(id);
    }

    @Override
    public Single<GetFlightsOutputDto> getFlights(FlightListRequest flightListRequest) {
        return flightRemote.getFlights(flightListRequest);
    }

    @Override
    public Single<GetSeatMapOutputDto> getSeatMap(String id) {
        return flightRemote.getSeatMap(id);
    }
}
