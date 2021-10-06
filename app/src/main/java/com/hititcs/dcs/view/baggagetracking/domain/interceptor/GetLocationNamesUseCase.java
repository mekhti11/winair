package com.hititcs.dcs.view.baggagetracking.domain.interceptor;

import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.domain.executor.ThreadExecutor;
import com.hititcs.dcs.domain.interactor.SingleUseCase;
import com.hititcs.dcs.view.baggagetracking.domain.model.GetTrackingBaggageLocationNamesOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.repository.BaggageTrackRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class GetLocationNamesUseCase
    extends SingleUseCase<GetTrackingBaggageLocationNamesOutputDto> {

  private final BaggageTrackRepository baggageTrackRepository;

  @Inject
  public GetLocationNamesUseCase(PostExecutionThread postExecutionThread,
      ThreadExecutor threadExecutor,
      BaggageTrackRepository baggageTrackRepository) {
    super(postExecutionThread, threadExecutor);
    this.baggageTrackRepository = baggageTrackRepository;
  }

  @Override
  protected Single<GetTrackingBaggageLocationNamesOutputDto> buildUseCaseObservable() {
    return baggageTrackRepository.getLocationNames();
  }
}
