package com.hititcs.dcs.view.flight.detail;

import com.hititcs.dcs.di.scope.FlightDetailScope;
import com.hititcs.dcs.domain.interactor.flight.GetFlightDetailUseCase;
import com.hititcs.dcs.domain.model.FlightDetail;

import com.hititcs.dcs.subscriber.SingleSubscriber;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

@FlightDetailScope
public class FlightDetailPresenterImpl implements FlightDetailPresenter {

    private FlightDetailView flightDetailView;
    private final GetFlightDetailUseCase flightDetailUseCase;

    public FlightDetailPresenterImpl(FlightDetailView flightDetailView, GetFlightDetailUseCase flightDetailUseCase) {
        this.flightDetailView = flightDetailView;
        this.flightDetailUseCase = flightDetailUseCase;
    }

    @Override
    public FlightDetailView getView() {
        return flightDetailView;
    }

    @Override
    public void setView(FlightDetailView view) {
        this.flightDetailView = view;
    }

    @Override
    public void dispose() {
        if (flightDetailUseCase != null) {
            flightDetailUseCase.dispose();
        }
    }

    @Override
    public void getFlightDetail(String flightId) {
        showViewLoading();
        flightDetailUseCase.execute(new SingleSubscriber<FlightDetail>(this) {
            @Override
            public void onResponse(FlightDetail data) {
                flightDetailView.showFlightDetail(data);
                hideViewLoading();
            }
        }, flightId);
    }
}
