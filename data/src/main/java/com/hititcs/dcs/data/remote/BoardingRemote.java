package com.hititcs.dcs.data.remote;

import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.domain.model.ResponseEntity;

import io.reactivex.Single;

public interface BoardingRemote {

  Single<BoardingResponse> withBarcode(BoardWithBarcodeRequest request);
}
