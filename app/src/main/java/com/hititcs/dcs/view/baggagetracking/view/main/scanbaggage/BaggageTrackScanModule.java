package com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage;

import com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.kranger.BaggageTrackScanKrangerFragment;
import com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.kranger.BaggageTrackScanKrangerFragmentModule;
import com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.zebra.BaggageTrackScanZebraFragment;
import com.hititcs.dcs.view.baggagetracking.view.main.scanbaggage.zebra.BaggageTrackScanZebraFragmentModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BaggageTrackScanModule {
  @ContributesAndroidInjector(modules = BaggageTrackScanFragmentModule.class)
  abstract BaggageTrackScanFragment baggageTrackScanFragment();

  @ContributesAndroidInjector(modules = BaggageTrackScanZebraFragmentModule.class)
  abstract BaggageTrackScanZebraFragment baggageTrackScanZebraFragment();

  @ContributesAndroidInjector(modules = BaggageTrackScanKrangerFragmentModule.class)
  abstract BaggageTrackScanKrangerFragment baggageTrackScanKrangerFragment();
}