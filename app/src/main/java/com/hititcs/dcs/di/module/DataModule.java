package com.hititcs.dcs.di.module;

import com.hititcs.dcs.data.repository.AirlineDataRepository;
import com.hititcs.dcs.data.repository.BoardingDataRepository;
import com.hititcs.dcs.data.repository.FlightDataRepository;
import com.hititcs.dcs.data.repository.LoginDataRepository;
import com.hititcs.dcs.domain.repository.AirlineRepository;
import com.hititcs.dcs.domain.repository.BoardingRepository;
import com.hititcs.dcs.domain.repository.FlightRepository;
import com.hititcs.dcs.domain.repository.LoginRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DataModule {

  @Binds
  public abstract LoginRepository loginRepository(LoginDataRepository loginDataRepository);

  @Binds
  public abstract FlightRepository flightRepository(FlightDataRepository flightDataRepository);

  @Binds
  public abstract BoardingRepository boardingRepository(BoardingDataRepository boardingDataRepository);

  @Binds
  public abstract AirlineRepository airlineRepository(AirlineDataRepository airlineDataRepository);
}
