package com.hititcs.dcs.view.flight;


import com.hititcs.dcs.domain.model.FlightSummary;
import com.hititcs.dcs.view.LoadView;

import java.util.List;

public interface FlightListView extends LoadView {
  void showFlightList(List<FlightSummary> flightSummaries);
}
