package com.hititcs.dcs.remote.api;

import com.hititcs.dcs.domain.model.FlightDetail;
import com.hititcs.dcs.domain.model.GetFlightsOutputDto;
import com.hititcs.dcs.domain.model.GetSeatMapOutputDto;
import io.reactivex.Single;
import java.time.LocalDate;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FlightResourceApi {

  @GET("airport/api/v1/flights/{id}")
  Single<FlightDetail> getFlightDetail(@Path("id") String id);

  @GET("airport/api/v1/flights")
  Single<GetFlightsOutputDto> getFlights(@Query("endDate") String endDate, @Query("startDate") String startDate);

  @GET("airport/api/v1/flights/{id}/seatMap")
  Single<GetSeatMapOutputDto> getSeatMap(@Path("id") String id);

}
