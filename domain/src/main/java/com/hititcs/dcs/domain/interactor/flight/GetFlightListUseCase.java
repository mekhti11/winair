package com.hititcs.dcs.domain.interactor.flight;

import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.domain.executor.ThreadExecutor;
import com.hititcs.dcs.domain.interactor.SingleWithParamUseCase;
import com.hititcs.dcs.domain.model.FlightListRequest;
import com.hititcs.dcs.domain.model.GetFlightsOutputDto;
import com.hititcs.dcs.domain.repository.FlightRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class GetFlightListUseCase extends SingleWithParamUseCase<GetFlightsOutputDto, FlightListRequest> {

  private final FlightRepository flightRepository;

  @Inject
  public GetFlightListUseCase(PostExecutionThread postExecutionThread,
      ThreadExecutor threadExecutor,
      FlightRepository flightRepository) {
    super(postExecutionThread, threadExecutor);
    this.flightRepository = flightRepository;
  }

  @Override
  protected Single<GetFlightsOutputDto> buildUseCaseObservable(
      FlightListRequest param) {
    if (param == null) {
      return Single.error(new NullPointerException("Flight list request is null"));
    }
    return flightRepository.getFlights(param);
  }
}
