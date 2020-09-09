package com.hititcs.dcs.di.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.hititcs.dcs.data.remote.AirlineRemote;
import com.hititcs.dcs.data.remote.BoardingRemote;
import com.hititcs.dcs.data.remote.FlightRemote;
import com.hititcs.dcs.data.remote.LoginRemote;
import com.hititcs.dcs.data.shared.AuthManager;
import com.hititcs.dcs.remote.LoginRemoteImpl;
import com.hititcs.dcs.remote.ServiceCreator;
import com.hititcs.dcs.remote.api.AirlineApi;
import com.hititcs.dcs.remote.api.BoardingControllerApi;
import com.hititcs.dcs.remote.api.FlightResourceApi;
import com.hititcs.dcs.remote.api.UserApi;
import com.hititcs.dcs.remote.interceptors.TokenInterceptor;
import com.hititcs.dcs.remote.service.AirlineService;
import com.hititcs.dcs.remote.service.BoardingService;
import com.hititcs.dcs.remote.service.FlightService;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public abstract class RemoteModule {

  @Provides
  public static UserApi provideUserApi(@Named("default") ServiceCreator serviceCreator) {
    return serviceCreator.createService(UserApi.class);
  }

  @Provides
  @Named("login")
  public static UserApi provideLoginServiceRx(@Named("token") ServiceCreator serviceCreator) {
    return serviceCreator.createService(UserApi.class);
  }

  @Provides
  @Singleton
  public static FlightResourceApi provideFlightResourceApi(
      @Named("default") ServiceCreator serviceCreator) {
    return serviceCreator.createService(FlightResourceApi.class);
  }

  @Provides
  @Singleton
  public static BoardingControllerApi provideBoardingControllerApi(
      @Named("default") ServiceCreator serviceCreator) {
    return serviceCreator.createService(BoardingControllerApi.class);
  }

  @Provides
  @Singleton
  public static AirlineApi provideAirlineApi(@Named("default") ServiceCreator serviceCreator) {
    return serviceCreator.createService(AirlineApi.class);
  }

  @Provides
  @Singleton
  public static TokenInterceptor provideTokenInterceptor(AuthManager authManager) {
    return new TokenInterceptor(authManager);
  }

  @Provides
  @Singleton
  @Named("default")
  public static ServiceCreator serviceCreator(String apiUrl, @Named("deviceId") String deviceId,
      boolean debug,
      TokenInterceptor tokenInterceptor) {
    return new ServiceCreator(apiUrl, debug, deviceId, tokenInterceptor,
        new StethoInterceptor(), true);
  }

  @Provides
  @Singleton
  @Named("token")
  public static ServiceCreator provideTokenServiceCreator(String apiUrl,
      @Named("deviceId") String deviceId,
      boolean debug) {
    return new ServiceCreator(apiUrl, debug, deviceId, null, new StethoInterceptor(), false);
  }

  @Binds
  public abstract LoginRemote crewRemote(LoginRemoteImpl loginRemote);

  @Binds
  public abstract FlightRemote flightRemote(FlightService flightService);

  @Binds
  public abstract BoardingRemote boardingRemote(BoardingService boardingService);

  @Binds
  public abstract AirlineRemote airlineRemote(AirlineService airlineService);

}
