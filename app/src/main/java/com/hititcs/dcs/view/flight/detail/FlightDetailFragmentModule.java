package com.hititcs.dcs.view.flight.detail;

import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.domain.executor.ThreadExecutor;
import com.hititcs.dcs.domain.interactor.flight.GetFlightDetailUseCase;
import com.hititcs.dcs.domain.repository.FlightRepository;
import com.hititcs.dcs.view.flight.detail.FlightDetailFragment;
import com.hititcs.dcs.view.flight.detail.FlightDetailPresenter;
import com.hititcs.dcs.view.flight.detail.FlightDetailPresenterImpl;
import com.hititcs.dcs.view.flight.detail.FlightDetailView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class FlightDetailFragmentModule {

    @Provides
    static GetFlightDetailUseCase provideFlightDetailUseCase(FlightRepository flightRepository,
                                                             PostExecutionThread postExecutionThread,
                                                             ThreadExecutor threadExecutor) {
        return new GetFlightDetailUseCase(postExecutionThread, threadExecutor, flightRepository);
    }

    @Provides
    static FlightDetailPresenter provideFlightDetailPresenter(FlightDetailView flightDetailView, GetFlightDetailUseCase flightDetailUseCase) {
        return new FlightDetailPresenterImpl(flightDetailView, flightDetailUseCase);
    }

    @Binds
    abstract FlightDetailView bindFlightDetailView(FlightDetailFragment flightDetailFragment);

}
