package com.hititcs.dcs.view.baggagetracking.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetTrackingBaggageLocationNamesOutputDto {

  @SerializedName("trackBaggageLocationDtoList")
  @Expose
  public List<TrackBaggageLocationDto> trackBaggageLocationDtoList;

  public GetTrackingBaggageLocationNamesOutputDto() {

  }

  public List<TrackBaggageLocationDto> getTrackBaggageLocationDtoList() {
    return trackBaggageLocationDtoList;
  }

  public void setTrackBaggageLocationDtoList(
      List<TrackBaggageLocationDto> trackBaggageLocationDtoList) {
    this.trackBaggageLocationDtoList = trackBaggageLocationDtoList;
  }
}
