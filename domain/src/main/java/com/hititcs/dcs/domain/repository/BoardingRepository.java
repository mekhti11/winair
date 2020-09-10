package com.hititcs.dcs.domain.repository;

import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;

import io.reactivex.Single;

public interface BoardingRepository {

    Single<BoardingResponse> withBarcode(BoardWithBarcodeRequest request);
}
