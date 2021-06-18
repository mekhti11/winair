package com.hititcs.dcs.view.baggagetracking.domain.model;

import java.io.Serializable;

public class ScannedTag implements Serializable {
  private String tagNo;
  private Boolean isSuccess;

  public ScannedTag() {

  }

  public String getTagNo() {
    return tagNo;
  }

  public void setTagNo(String tagNo) {
    this.tagNo = tagNo;
  }

  public Boolean getSuccess() {
    return isSuccess;
  }

  public void setSuccess(Boolean success) {
    isSuccess = success;
  }
}
