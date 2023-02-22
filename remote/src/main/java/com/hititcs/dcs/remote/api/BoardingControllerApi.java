package com.hititcs.dcs.remote.api;

import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BoardingControllerApi {

  @POST("airport/api/v1/boarding/withBarcode")
  Single<BoardingResponse> withBarcode(@Body BoardWithBarcodeRequest request);
}
