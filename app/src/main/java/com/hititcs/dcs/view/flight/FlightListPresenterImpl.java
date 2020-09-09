package com.hititcs.dcs.view.flight;

import com.hititcs.dcs.di.scope.FlightListScope;
import com.hititcs.dcs.domain.interactor.flight.GetFlightListUseCase;
import com.hititcs.dcs.domain.model.FlightListRequest;
import com.hititcs.dcs.domain.model.GetFlightsOutputDto;

import com.hititcs.dcs.subscriber.SingleSubscriber;
import com.hititcs.dcs.util.DateTimeUtils;

import java.util.Calendar;
import java.util.Date;
import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import org.threeten.bp.LocalDate;

@FlightListScope
public class FlightListPresenterImpl implements FlightListPresenter {

    private FlightListView flightListView;
    private final GetFlightListUseCase flightListUseCase;

    @Inject
    public FlightListPresenterImpl(FlightListView flightListView, GetFlightListUseCase flightListUseCase) {
        this.flightListView = flightListView;
        this.flightListUseCase = flightListUseCase;
    }

    @Override
    public void getFlightList(LocalDate selectedDate) {
        FlightListRequest flightListRequest = new FlightListRequest();
        flightListRequest.setStartDate(DateTimeUtils.formatDateToRequestFormat(selectedDate));
        flightListRequest.setEndDate(DateTimeUtils.formatDateToRequestFormat(selectedDate));

        showViewLoading();
        flightListUseCase.execute(new SingleSubscriber<GetFlightsOutputDto>(this) {
            @Override
            public void onResponse(GetFlightsOutputDto data) {
                hideViewLoading();
                getView().showFlightList(data.getFlights());
            }
        }, flightListRequest);

    }

    @Override
    public FlightListView getView() {
        return flightListView;
    }

    @Override
    public void setView(FlightListView view) {
        this.flightListView = view;
    }

    @Override
    public void dispose() {
        if (flightListUseCase != null) {
            flightListUseCase.dispose();
        }
    }
}
