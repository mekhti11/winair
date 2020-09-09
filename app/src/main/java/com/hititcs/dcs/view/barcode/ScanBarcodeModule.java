package com.hititcs.dcs.view.barcode;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ScanBarcodeModule {

  @ContributesAndroidInjector(modules = ScanBarcodeFragmentModule.class)
  abstract ScanBarcodeFragment scanBarcodeFragment();
}
