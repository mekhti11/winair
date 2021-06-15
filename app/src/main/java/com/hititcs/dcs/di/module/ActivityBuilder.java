package com.hititcs.dcs.di.module;

import com.hititcs.dcs.di.scope.FlightDetailScope;
import com.hititcs.dcs.di.scope.FlightListScope;
import com.hititcs.dcs.di.scope.LoginScope;
import com.hititcs.dcs.di.scope.ScanBarcodeScope;
import com.hititcs.dcs.view.barcode.ScanBarcodeActivity;
import com.hititcs.dcs.view.barcode.ScanBarcodeModule;
import com.hititcs.dcs.view.flight.FlightListActivity;
import com.hititcs.dcs.view.flight.FlightListModule;
import com.hititcs.dcs.view.flight.detail.FlightDetailActivity;
import com.hititcs.dcs.view.flight.detail.FlightDetailModule;
import com.hititcs.dcs.view.login.LoginActivity;
import com.hititcs.dcs.view.login.LoginModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

  @LoginScope
  @ContributesAndroidInjector(modules = LoginModule.class)
  abstract LoginActivity bindLoginActivity();

  @FlightListScope
  @ContributesAndroidInjector(modules = FlightListModule.class)
  abstract FlightListActivity bindFlightListActivity();

  @FlightDetailScope
  @ContributesAndroidInjector(modules = FlightDetailModule.class)
  abstract FlightDetailActivity bindFlightDetailActivity();

  @ScanBarcodeScope
  @ContributesAndroidInjector(modules = ScanBarcodeModule.class)
  abstract ScanBarcodeActivity bindScanBarcodeActivity();

}
