package com.hititcs.dcs.view.flight.detail;

import com.hititcs.dcs.view.LoadPresenter;

public interface FlightDetailPresenter extends LoadPresenter<FlightDetailView> {
    void getFlightDetail(String flightId);
}
