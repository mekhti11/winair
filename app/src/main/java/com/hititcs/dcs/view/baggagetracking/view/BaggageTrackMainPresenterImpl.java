package com.hititcs.dcs.view.baggagetracking.view;

import com.hititcs.dcs.subscriber.SingleSubscriber;
import com.hititcs.dcs.view.baggagetracking.domain.interceptor.GetLocationNamesUseCase;
import com.hititcs.dcs.view.baggagetracking.domain.model.GetTrackingBaggageLocationNamesOutputDto;
import javax.inject.Inject;

public class BaggageTrackMainPresenterImpl
    implements BaggageTrackMainContract.BaggageTrackMainPresenter {

  private final GetLocationNamesUseCase getLocationNamesUseCase;
  private BaggageTrackMainContract.BaggageTrackMainView view;

  @Inject
  public BaggageTrackMainPresenterImpl(BaggageTrackMainContract.BaggageTrackMainView view,
      GetLocationNamesUseCase getLocationNamesUseCase) {
    this.view = view;
    this.getLocationNamesUseCase = getLocationNamesUseCase;
  }

  @Override public void getLocationNamesAndCodes() {
    showViewLoading();
    getLocationNamesUseCase.execute(
        new SingleSubscriber<GetTrackingBaggageLocationNamesOutputDto>(this) {
          @Override public void onResponse(GetTrackingBaggageLocationNamesOutputDto data) {
            view.setLocationNamesAndCodes(data);
          }
        });
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
  }
}
