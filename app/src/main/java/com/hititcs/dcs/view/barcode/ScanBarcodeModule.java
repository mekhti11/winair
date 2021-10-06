package com.hititcs.dcs.view.barcode;

import com.hititcs.dcs.view.barcode.kranger.ScanBarcodeKrangerFragment;
import com.hititcs.dcs.view.barcode.kranger.ScanBarcodeKrangerFragmentModule;
import com.hititcs.dcs.view.barcode.zebra.ScanBarcodeZebraFragment;
import com.hititcs.dcs.view.barcode.zebra.ScanBarcodeZebraFragmentModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ScanBarcodeModule {

  @ContributesAndroidInjector(modules = ScanBarcodeFragmentModule.class)
  abstract ScanBarcodeFragment scanBarcodeFragment();

  @ContributesAndroidInjector(modules = ScanBarcodeZebraFragmentModule.class)
  abstract ScanBarcodeZebraFragment scanBarcodeZebraFragment();

  @ContributesAndroidInjector(modules = ScanBarcodeKrangerFragmentModule.class)
  abstract ScanBarcodeKrangerFragment scanBarcodeKrangerFragment();
}
