package com.hititcs.dcs.view.baggagetracking.data.repository;

import com.hititcs.dcs.view.baggagetracking.data.remote.BaggageTrackRemote;
import com.hititcs.dcs.view.baggagetracking.domain.model.GetTrackingBaggageLocationNamesOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageRequest;
import com.hititcs.dcs.view.baggagetracking.domain.repository.BaggageTrackRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class BaggageTrackDataRepository implements BaggageTrackRepository {

  private BaggageTrackRemote remote;

  @Inject
  public BaggageTrackDataRepository(BaggageTrackRemote remote) {
    this.remote = remote;
  }

  @Override public Single<GetTrackingBaggageLocationNamesOutputDto> getLocationNames() {
    return remote.getLocationNames();
  }

  @Override
  public Single<ScanBaggageOutputDto> scanBaggageBarcode(ScanBaggageRequest scanBaggageRequest) {
    return remote.scanBaggageBarcode(scanBaggageRequest);
  }
}
