/*
 * Airport REST API
 * Airport Mobile Middleware
 *
 * OpenAPI spec version: v1
 * Contact: onur.yardimci@pinsoft.ist
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.hititcs.dcs.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * AircraftInfo
 */

public class AircraftInfo {

  @SerializedName("airEquipType")
  private String airEquipType = null;

  @SerializedName("airEquipTypeModel")
  private String airEquipTypeModel = null;

  @SerializedName("aircraftTailNumber")
  private String aircraftTailNumber = null;

  @SerializedName("string")
  private String string = null;

  public AircraftInfo airEquipType(String airEquipType) {
    this.airEquipType = airEquipType;
    return this;
  }

  public String getAirEquipType() {
    return airEquipType;
  }

  public void setAirEquipType(String airEquipType) {
    this.airEquipType = airEquipType;
  }

  public AircraftInfo airEquipTypeModel(String airEquipTypeModel) {
    this.airEquipTypeModel = airEquipTypeModel;
    return this;
  }

  public String getAirEquipTypeModel() {
    return airEquipTypeModel;
  }

  public void setAirEquipTypeModel(String airEquipTypeModel) {
    this.airEquipTypeModel = airEquipTypeModel;
  }

  public AircraftInfo aircraftTailNumber(String aircraftTailNumber) {
    this.aircraftTailNumber = aircraftTailNumber;
    return this;
  }

  public String getAircraftTailNumber() {
    return aircraftTailNumber;
  }

  public void setAircraftTailNumber(String aircraftTailNumber) {
    this.aircraftTailNumber = aircraftTailNumber;
  }

  public AircraftInfo string(String string) {
    this.string = string;
    return this;
  }

  public String getString() {
    return string;
  }

  public void setString(String string) {
    this.string = string;
  }
}

