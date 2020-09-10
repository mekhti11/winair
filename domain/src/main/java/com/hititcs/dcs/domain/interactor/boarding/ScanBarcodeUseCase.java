package com.hititcs.dcs.domain.interactor.boarding;

import com.hititcs.dcs.domain.executor.PostExecutionThread;
import com.hititcs.dcs.domain.executor.ThreadExecutor;
import com.hititcs.dcs.domain.interactor.SingleWithParamUseCase;
import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.domain.repository.BoardingRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class ScanBarcodeUseCase extends SingleWithParamUseCase<BoardingResponse, BoardWithBarcodeRequest> {

    private final BoardingRepository boardingRepository;

    @Inject
    public ScanBarcodeUseCase(PostExecutionThread postExecutionThread,
                                  ThreadExecutor threadExecutor,
                              BoardingRepository boardingRepository) {
        super(postExecutionThread, threadExecutor);
        this.boardingRepository = boardingRepository;
    }

    @Override
    protected Single<BoardingResponse> buildUseCaseObservable(
            BoardWithBarcodeRequest request) {
        if (request == null) {
            return Single.error(new NullPointerException("Scan barcode request has empty body"));
        }

        return boardingRepository.withBarcode(request);
    }
}
