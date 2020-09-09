package com.hititcs.dcs.domain.interactor.airline;

import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.domain.executor.ThreadExecutor;
import com.hititcs.dcs.domain.interactor.SingleUseCase;
import com.hititcs.dcs.domain.model.AirlineListResponse;
import com.hititcs.dcs.domain.repository.AirlineRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class GetCompaniesUseCase extends SingleUseCase<AirlineListResponse> {

  private final AirlineRepository airlineRepository;

  @Inject
  public GetCompaniesUseCase(PostExecutionThread postExecutionThread,
      ThreadExecutor threadExecutor,
      AirlineRepository airlineRepository) {
    super(postExecutionThread, threadExecutor);
    this.airlineRepository = airlineRepository;
  }

  @Override
  protected Single<AirlineListResponse> buildUseCaseObservable() {
    return airlineRepository.getAirlines();
  }
}