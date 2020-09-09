package com.hititcs.dcs.domain.model;

import com.google.gson.annotations.SerializedName;

public class Airline {

  @SerializedName("airlineName")
  private String airlineName = null;

  @SerializedName("airlineCode")
  private String airlineCode = null;

  public String getAirlineName() {
    return airlineName;
  }

  public void setAirlineName(String airlineName) {
    this.airlineName = airlineName;
  }

  public String getAirlineCode() {
    return airlineCode;
  }

  public void setAirlineCode(String airlineCode) {
    this.airlineCode = airlineCode;
  }

  @Override public String toString() {
    return airlineName;
  }
}
