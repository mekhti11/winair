package com.hititcs.dcs.view.flight;

import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.domain.executor.ThreadExecutor;
import com.hititcs.dcs.domain.interactor.flight.GetFlightListUseCase;
import com.hititcs.dcs.domain.repository.FlightRepository;
import com.hititcs.dcs.view.flight.FlightListFragment;
import com.hititcs.dcs.view.flight.FlightListPresenter;
import com.hititcs.dcs.view.flight.FlightListPresenterImpl;
import com.hititcs.dcs.view.flight.FlightListView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class FlightListFragmentModule {

  @Provides
  static GetFlightListUseCase provideFlightListUseCase(FlightRepository flightRepository,
                                                       PostExecutionThread postExecutionThread,
                                                       ThreadExecutor threadExecutor) {
    return new GetFlightListUseCase(postExecutionThread, threadExecutor, flightRepository);
  }

  @Provides
  static FlightListPresenter provideFlightListPresenter(FlightListView flightListView, GetFlightListUseCase flightListUseCase) {
    return new FlightListPresenterImpl(flightListView, flightListUseCase);
  }

  @Binds
  abstract FlightListView bindFlightListView(FlightListFragment flightListFragment);

}
