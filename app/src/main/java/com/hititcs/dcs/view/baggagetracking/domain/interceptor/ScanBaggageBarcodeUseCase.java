package com.hititcs.dcs.view.baggagetracking.domain.interceptor;

import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.domain.executor.ThreadExecutor;
import com.hititcs.dcs.domain.interactor.SingleWithParamUseCase;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageOutputDto;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScanBaggageRequest;
import com.hititcs.dcs.view.baggagetracking.domain.repository.BaggageTrackRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class ScanBaggageBarcodeUseCase extends
    SingleWithParamUseCase<ScanBaggageOutputDto, ScanBaggageRequest> {

  private final BaggageTrackRepository baggageTrackRepository;

  @Inject
  public ScanBaggageBarcodeUseCase(PostExecutionThread postExecutionThread,
      ThreadExecutor threadExecutor,
      BaggageTrackRepository baggageTrackRepository) {
    super(postExecutionThread, threadExecutor);
    this.baggageTrackRepository = baggageTrackRepository;
  }

  @Override
  protected Single<ScanBaggageOutputDto> buildUseCaseObservable(
      ScanBaggageRequest request) {
    if (request == null) {
      return Single.error(new NullPointerException("Scan Baggage request has empty body"));
    }
    return baggageTrackRepository.scanBaggageBarcode(request);
  }
}
