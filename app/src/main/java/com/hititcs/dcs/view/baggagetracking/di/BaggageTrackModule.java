package com.hititcs.dcs.view.baggagetracking.di;

import com.hititcs.dcs.remote.ServiceCreator;
import com.hititcs.dcs.view.baggagetracking.data.remote.BaggageTrackRemote;
import com.hititcs.dcs.view.baggagetracking.data.repository.BaggageTrackDataRepository;
import com.hititcs.dcs.view.baggagetracking.domain.repository.BaggageTrackRepository;
import com.hititcs.dcs.view.baggagetracking.remote.impl.BaggageTrackRemoteImpl;
import com.hititcs.dcs.view.baggagetracking.remote.service.BaggageTrackService;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public abstract class BaggageTrackModule {

  @Binds
  public abstract BaggageTrackRepository baggageTrackRepository(
      BaggageTrackDataRepository baggageTrackDataRepository);

  @Binds
  public abstract BaggageTrackRemote baggageTrackRemote(BaggageTrackRemoteImpl baggageRemote);

  @Provides
  @Singleton
  public static BaggageTrackService provideBaggageTrackService(
      @Named("default") ServiceCreator serviceCreator) {
    return serviceCreator.createService(BaggageTrackService.class);
  }
}
