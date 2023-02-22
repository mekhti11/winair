package com.hititcs.dcs.view.baggagetracking.view.main;

import com.hititcs.dcs.subscriber.SingleSubscriber;
import com.hititcs.dcs.view.baggagetracking.domain.interceptor.GetLocationNamesUseCase;
import com.hititcs.dcs.view.baggagetracking.domain.interceptor.ScanBaggageBarcodeUseCase;
import com.hititcs.dcs.view.baggagetracking.domain.model.GetTrackingBaggageLocationNamesOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageRequest;
import com.hititcs.dcs.view.baggagetracking.domain.model.TrackBaggageLocationDto;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;

public class BaggageTrackMainPresenterImpl
    implements BaggageTrackMainContract.BaggageTrackMainPresenter {

  private final GetLocationNamesUseCase getLocationNamesUseCase;
  private final ScanBaggageBarcodeUseCase scanBaggageBarcodeUseCase;
  private BaggageTrackMainContract.BaggageTrackMainView view;

  @Inject
  public BaggageTrackMainPresenterImpl(BaggageTrackMainContract.BaggageTrackMainView view,
      GetLocationNamesUseCase getLocationNamesUseCase,
      ScanBaggageBarcodeUseCase scanBaggageBarcodeUseCase) {
    this.view = view;
    this.getLocationNamesUseCase = getLocationNamesUseCase;
    this.scanBaggageBarcodeUseCase = scanBaggageBarcodeUseCase;
  }

  @Override public void getLocationNamesAndCodes() {
    view.showProgressDialog();
    getLocationNamesUseCase.execute(
        new SingleSubscriber<GetTrackingBaggageLocationNamesOutputDto>(this) {
          @Override public void onResponse(GetTrackingBaggageLocationNamesOutputDto data) {
            view.setLocationNamesAndCodes(filterNoisyData(data.getTrackBaggageLocationDtoList()));
            view.hideProgressDialog();
          }

          @Override
          public void onError(Throwable e) {
            super.onError(e);
            view.hideProgressDialog();
          }
        });
  }

  private List<TrackBaggageLocationDto> filterNoisyData(List<TrackBaggageLocationDto> data) {
    Iterator<TrackBaggageLocationDto> it = data.iterator();
    while (it.hasNext()) {
      TrackBaggageLocationDto item = it.next();
      if (item.getLocationNameCodes().isEmpty()) {
        it.remove();
      }
    }
    return data;
  }

  @Override
  public void scanSingleBaggageTag(String tagNo, String locationCode, String locationName) {
    view.showProgressDialog();
    ScanBaggageRequest request = new ScanBaggageRequest();
    request.setLocationCode(locationCode);
    request.setLocationName(locationName);
    request.setTagNo(tagNo.substring(tagNo.length() - 6));
    scanBaggageBarcodeUseCase.execute(new SingleSubscriber<ScanBaggageOutputDto>(this) {
      @Override public void onResponse(ScanBaggageOutputDto data) {
        view.scannedBaggageTag(tagNo, true, null);
        view.hideProgressDialog();
      }

      @Override public void onError(Throwable e) {
        view.scannedBaggageTag(tagNo, false, super.getErrorMessage(e));
        view.hideProgressDialog();
      }
    }, request);
  }

  @Override
  public BaggageTrackMainContract.BaggageTrackMainView getView() {
    return view;
  }

  @Override
  public void setView(BaggageTrackMainContract.BaggageTrackMainView view) {
    this.view = view;
  }

  @Override
  public void dispose() {
    if (getLocationNamesUseCase != null) {
      getLocationNamesUseCase.dispose();
    }
    if (scanBaggageBarcodeUseCase != null) {
      scanBaggageBarcodeUseCase.dispose();
    }
  }
}
