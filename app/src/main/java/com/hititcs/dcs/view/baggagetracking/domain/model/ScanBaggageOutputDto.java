package com.hititcs.dcs.view.baggagetracking.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScanBaggageOutputDto {
  @SerializedName("message")
  @Expose
  private String message;
  @SerializedName("result")
  @Expose
  private String result;

  public ScanBaggageOutputDto() {

  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }
}
