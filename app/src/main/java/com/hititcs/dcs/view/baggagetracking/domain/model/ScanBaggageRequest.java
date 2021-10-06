package com.hititcs.dcs.view.baggagetracking.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScanBaggageRequest {
  @SerializedName("locationCode")
  @Expose
  private String locationCode;
  @SerializedName("locationName")
  @Expose
  private String locationName;
  @SerializedName("tagNo")
  @Expose
  private String tagNo;

  public ScanBaggageRequest() {

  }

  public String getLocationCode() {
    return locationCode;
  }

  public void setLocationCode(String locationCode) {
    this.locationCode = locationCode;
  }

  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  public String getTagNo() {
    return tagNo;
  }

  public void setTagNo(String tagNo) {
    this.tagNo = tagNo;
  }
}
