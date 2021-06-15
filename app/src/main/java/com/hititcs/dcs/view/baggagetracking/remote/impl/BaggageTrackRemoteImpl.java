package com.hititcs.dcs.view.baggagetracking.remote.impl;

import com.hititcs.dcs.view.baggagetracking.data.remote.BaggageTrackRemote;
import com.hititcs.dcs.view.baggagetracking.domain.model.GetTrackingBaggageLocationNamesOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageRequest;
import com.hititcs.dcs.view.baggagetracking.remote.service.BaggageTrackService;
import io.reactivex.Single;
import javax.inject.Inject;

public class BaggageTrackRemoteImpl implements BaggageTrackRemote {

  private final BaggageTrackService service;

  @Inject
  public BaggageTrackRemoteImpl(BaggageTrackService service) {
    this.service = service;
  }

  @Override public Single<GetTrackingBaggageLocationNamesOutputDto> getLocationNames() {
    return service.getLocationNames();
  }

  @Override
  public Single<ScanBaggageOutputDto> scanBaggageBarcode(ScanBaggageRequest scanBaggageRequest) {
    return service.scanBaggageBarcode(scanBaggageRequest);
  }
}
