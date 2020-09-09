package com.hititcs.dcs.remote.service;

import com.hititcs.dcs.data.remote.BoardingRemote;
import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.domain.model.ResponseEntity;
import com.hititcs.dcs.remote.api.BoardingControllerApi;

import javax.inject.Inject;

import io.reactivex.Single;

public class BoardingService implements BoardingRemote {

  private BoardingControllerApi boardingControllerApi;

  @Inject
  public BoardingService(BoardingControllerApi boardingControllerApi) {
    this.boardingControllerApi = boardingControllerApi;
  }

  @Override
  public Single<BoardingResponse> withBarcode(BoardWithBarcodeRequest request) {
    return boardingControllerApi.withBarcode(request);
  }
}
