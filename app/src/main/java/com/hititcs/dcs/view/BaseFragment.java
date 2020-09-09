package com.hititcs.dcs.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.hititcs.dcs.Injectable;
import com.hititcs.dcs.R;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

public abstract class BaseFragment<T extends Fragment> extends Fragment implements Injectable,
    HasSupportFragmentInjector {

  @Inject
  DispatchingAndroidInjector<Fragment> childFragmentInjector;
  private Unbinder unbinder;
  private ProgressDialog progressDialog;
  private boolean mAttached;

  @Override
  public void onAttach(Context context) {
    inject();
    super.onAttach(context);
    mAttached = true;
  }

  protected void bindView(View view) {
    unbinder = ButterKnife.bind(getFragment(), view);
  }

  @Override
  public void inject() {
    AndroidSupportInjection.inject(this);
  }

  @Override
  public void onDestroy() {
    destroyPresenter();
    super.onDestroy();
    if (unbinder != null) {
      unbinder.unbind();
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mAttached = false;
  }

  public boolean isAttached() {
    return mAttached;
  }

  @Override
  public AndroidInjector<Fragment> supportFragmentInjector() {
    return childFragmentInjector;
  }

  protected abstract T getFragment();

  protected abstract Presenter getPresenter();

  public Context context() {
    return getActivity();
  }

  protected void destroyPresenter() {
    if (getPresenter() != null) {
      getPresenter().destroyView();
    }
  }

  public void showLoading() {
    ((BaseActivity)getActivity()).showLoading();
  }

  public void hideLoading() {
    ((BaseActivity)getActivity()).hideLoading();
  }

  public void showProgressDialog(@StringRes int title, @StringRes int message) {
    if (progressDialog != null && progressDialog.isShowing()) {
      return;
    }
    progressDialog = ProgressDialog.show(getContext(), getString(title),
        getString(message), true, false);
  }

  public void showProgressDialog() {
    showProgressDialog(R.string.please_wait, R.string.progress_login);
  }

  public void hideProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

}
