package com.hititcs.dcs.data.repository;

import com.hititcs.dcs.data.remote.BoardingRemote;
import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.domain.repository.BoardingRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class BoardingDataRepository implements BoardingRepository {

    private BoardingRemote boardingRemote;

    @Inject
    public BoardingDataRepository(BoardingRemote boardingRemote) {
        this.boardingRemote = boardingRemote;
    }

    @Override
    public Single<BoardingResponse> withBarcode(BoardWithBarcodeRequest request) {
        return boardingRemote.withBarcode(request);
    }
}
