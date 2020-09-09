package com.hititcs.dcs.view.flight;


import com.hititcs.dcs.view.LoadPresenter;
import org.threeten.bp.LocalDate;

public interface FlightListPresenter extends LoadPresenter<FlightListView> {

  void getFlightList(LocalDate selectedDate);
}
