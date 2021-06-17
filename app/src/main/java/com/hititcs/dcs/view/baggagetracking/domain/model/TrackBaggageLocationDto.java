package com.hititcs.dcs.view.baggagetracking.domain.model;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.jetbrains.annotations.NotNull;

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

  @NonNull @NotNull @Override public String toString() {
    return locationNameCodes.get(0).getLocationName();
  }
}
