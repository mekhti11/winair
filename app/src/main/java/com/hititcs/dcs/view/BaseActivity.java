package com.hititcs.dcs.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hititcs.dcs.Injectable;
import com.hititcs.dcs.R;
import com.hititcs.dcs.data.shared.AuthManager;
import com.hititcs.dcs.view.flight.FlightListActivity;
import com.hititcs.dcs.view.main.MainActivity;
import com.hititcs.dcs.widget.CustomCircleLoading;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

public abstract class BaseActivity<T extends Activity> extends AppCompatActivity implements
    Injectable, BindView,
    HasSupportFragmentInjector {

  @Nullable
  @butterknife.BindView(R.id.toolbar)
  public Toolbar toolbar;
  @Nullable
  @butterknife.BindView(R.id.toolbar_title)
  public TextView twToolbarTitle;
  @Nullable
  @butterknife.BindView(R.id.app_bar)
  public AppBarLayout appBarLayout;

  @Inject
  DispatchingAndroidInjector<Fragment> supportFragmentInjector;

  @Inject
  AuthManager authManager;

  protected ProgressDialog progressDialog;
  protected CustomCircleLoading customCircleLoading;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    inject();
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      customCircleLoading = new CustomCircleLoading(this);
    }
    setContentView(R.layout.activity_content);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }

  protected abstract T getActivity();

  @Override
  public void bindView() {
    ButterKnife.bind(getActivity());
  }

  @Override
  public void inject() {
    AndroidInjection.inject(this);
  }

  @Override
  public AndroidInjector<Fragment> supportFragmentInjector() {
    return supportFragmentInjector;
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    if (customCircleLoading != null) {
      outState.putAll(customCircleLoading.saveState());
    }
    super.onSaveInstanceState(outState);
  }

  protected AuthManager getAuthManager(){
    return authManager;
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    if (customCircleLoading != null) {
      customCircleLoading.restoreState(this, savedInstanceState);
    }
  }

  protected void hideToolbar() {
    toolbar.setMinimumHeight(0);
    toolbar.setVisibility(View.GONE);
  }

  protected void setTitle(String title) {
    toolbar.setVisibility(View.VISIBLE);
    twToolbarTitle.setText(title);
  }

  public void showProgressDialog(@StringRes int title, @StringRes int message) {
    if (progressDialog != null && progressDialog.isShowing()) {
      return;
    }
    progressDialog = ProgressDialog.show(this, getString(title),
        getString(message), true, false);
  }

  public void showProgressDialog() {
    showProgressDialog(R.string.please_wait, R.string.progress_login);
  }

  public void hideProgressDialog() {
    if (progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  public void showLoading() {
    if (customCircleLoading == null) {
      return;
    }
    if (!customCircleLoading.isShowing()) {
      customCircleLoading.show();
    }
  }

  public void hideLoading() {
    if (customCircleLoading != null && customCircleLoading.isShowing()) {
      customCircleLoading.dismiss();
    }
  }

  public void navigateToFlightListActivity() {
    Intent intent = new Intent(context(), FlightListActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
        | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    if (getActivity() != null) {
      getActivity().finish();
    }
  }

  public Context context() {
    return getActivity();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_home:
          Intent intent = new Intent(getActivity(), FlightListActivity.class);
          startActivity(intent);
          return true;
        case R.id.navigation_settings:
          //Intent intent2 = new Intent(getActivity(), SettingsActivity.class);
          //startActivity(intent2);
          return true;
        case R.id.navigation_sign_out:
          authManager.clear();
          Intent intent3 = new Intent(getActivity(), MainActivity.class);
          intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
              | Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent3);
          finish();
          return true;
        default:
          return true;
      }
    }
  };

}
