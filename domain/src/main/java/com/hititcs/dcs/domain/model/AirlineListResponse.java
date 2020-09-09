package com.hititcs.dcs.domain.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AirlineListResponse {

  @SerializedName("airlineList")
  private List<Airline> airlineList = null;

  public List<Airline> getAirlineList() {
    return airlineList;
  }

  public void setAirlineList(List<Airline> airlineList) {
    this.airlineList = airlineList;
  }
}
