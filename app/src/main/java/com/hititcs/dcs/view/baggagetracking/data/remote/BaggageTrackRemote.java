package com.hititcs.dcs.view.baggagetracking.data.remote;

import com.hititcs.dcs.view.baggagetracking.domain.model.GetTrackingBaggageLocationNamesOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageRequest;
import io.reactivex.Single;

public interface BaggageTrackRemote {
  Single<GetTrackingBaggageLocationNamesOutputDto> getLocationNames();

  Single<ScanBaggageOutputDto> scanBaggageBarcode(ScanBaggageRequest scanBaggageRequest);
}
