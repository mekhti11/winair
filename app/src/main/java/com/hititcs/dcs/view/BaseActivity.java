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
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import com.google.android.material.appbar.AppBarLayout;
import com.hititcs.dcs.Injectable;
import com.hititcs.dcs.R;
import com.hititcs.dcs.data.shared.AuthManager;
import com.hititcs.dcs.view.home.view.HomeActivity;
import com.hititcs.dcs.view.login.LoginActivity;
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
  protected ProgressDialog progressDialog;
  protected CustomCircleLoading customCircleLoading;
  @Inject
  DispatchingAndroidInjector<Fragment> supportFragmentInjector;
  @Inject
  AuthManager authManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    inject();
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      customCircleLoading = new CustomCircleLoading(this);
    }
    setContentView(R.layout.activity_content);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

  protected AuthManager getAuthManager() {
    return authManager;
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    if (customCircleLoading != null) {
      customCircleLoading.restoreState(this, savedInstanceState);
    }
  }

  protected void setToolbar() {
    setSupportActionBar(toolbar);
    getSupportActionBar()
        .setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  protected void hideToolbar() {
    toolbar.setMinimumHeight(0);
    toolbar.setVisibility(View.GONE);
  }

  protected void hideBackButton() {
    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
  }

  public void setTitle(@StringRes int resourceId) {
    twToolbarTitle.setText(getString(resourceId));
    toolbar.setVisibility(View.VISIBLE);
  }

  protected void setTitle(String title) {
    twToolbarTitle.setText(title);
    toolbar.setVisibility(View.VISIBLE);
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

  public void navigateToHomeActivity() {
    Intent intent = new Intent(context(), HomeActivity.class);
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

  public void logout() {
    authManager.clear();
    Intent intent = new Intent(getActivity(), LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
        | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    if (getActivity() != null) {
      getActivity().finish();
    }
  }

/*  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
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
          Intent intent3 = new Intent(getActivity(), LoginActivity.class);
          intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
              | Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent3);
          finish();
          return true;
      }
      return false;
    }
  };*/
}
