package com.hititcs.dcs.view.flight.detail;

import com.hititcs.dcs.domain.model.FlightDetail;
import com.hititcs.dcs.view.LoadView;

public interface FlightDetailView extends LoadView {
    void showFlightDetail(FlightDetail flightsOutputDto);
}
