package com.hititcs.dcs.view.baggagetracking.domain.model;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

public class TrackBaggageLocationCodeDto {
  @SerializedName("locationCode")
  @Expose
  private String locationCode;

  @SerializedName("locationCodeExplanation")
  @Expose
  private String locationCodeExplanation;

  @SerializedName("locationName")
  @Expose
  private String locationName;

  public TrackBaggageLocationCodeDto() {

  }

  public String getLocationCode() {
    return locationCode;
  }

  public void setLocationCode(String locationCode) {
    this.locationCode = locationCode;
  }

  public String getLocationCodeExplanation() {
    return locationCodeExplanation;
  }

  public void setLocationCodeExplanation(String locationCodeExplanation) {
    this.locationCodeExplanation = locationCodeExplanation;
  }

  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  @NonNull @NotNull @Override public String toString() {
    return String.format("%s - %s", locationCode, locationCodeExplanation);
  }
}
