package com.hititcs.dcs.domain.interactor.flight;

import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.domain.executor.ThreadExecutor;
import com.hititcs.dcs.domain.interactor.SingleWithParamUseCase;
import com.hititcs.dcs.domain.model.FlightDetail;
import com.hititcs.dcs.domain.repository.FlightRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetFlightDetailUseCase extends SingleWithParamUseCase<FlightDetail, String> {

    private final FlightRepository flightRepository;

    @Inject
    public GetFlightDetailUseCase(PostExecutionThread postExecutionThread,
                                  ThreadExecutor threadExecutor,
                                  FlightRepository flightRepository) {
        super(postExecutionThread, threadExecutor);
        this.flightRepository = flightRepository;
    }

    @Override
    protected Single<FlightDetail> buildUseCaseObservable(
            String id) {
        if (id == null) {
            return Single.error(new NullPointerException("Flight details request must have flight ID"));
        }

        return flightRepository.getFlightDetail(id);
    }
}
