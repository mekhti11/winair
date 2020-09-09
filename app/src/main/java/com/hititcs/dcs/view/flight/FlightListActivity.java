package com.hititcs.dcs.view.flight;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.hititcs.dcs.R;
import com.hititcs.dcs.view.BaseActivity;


public class FlightListActivity extends BaseActivity<FlightListActivity> {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    bindView();
    hideToolbar();
    if (savedInstanceState == null) {
      setUpFragment();
    }
  }

  private void setUpFragment() {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.content_frame_bottom_nav,
            FlightListFragment.newInstance(),
            FlightListFragment.class.getSimpleName())
        .commit();
  }


  @Override
  protected FlightListActivity getActivity() {
    return this;
  }

}
