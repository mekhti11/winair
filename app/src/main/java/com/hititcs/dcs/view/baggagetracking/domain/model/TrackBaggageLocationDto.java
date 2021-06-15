package com.hititcs.dcs.view.baggagetracking.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TrackBaggageLocationDto {
  @SerializedName("locationNameCodes")
  @Expose
  private List<TrackBaggageLocationCodeDto> locationNameCodes;

  @SerializedName("locationOrder")
  @Expose
  private Long locationOrder;

  public TrackBaggageLocationDto() {

  }

  public List<TrackBaggageLocationCodeDto> getLocationNameCodes() {
    return locationNameCodes;
  }

  public void setLocationNameCodes(List<TrackBaggageLocationCodeDto> locationNameCodes) {
    this.locationNameCodes = locationNameCodes;
  }

  public Long getLocationOrder() {
    return locationOrder;
  }

  public void setLocationOrder(Long locationOrder) {
    this.locationOrder = locationOrder;
  }
}
