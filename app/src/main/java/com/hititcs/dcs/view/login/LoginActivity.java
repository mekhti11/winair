package com.hititcs.dcs.view.login;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hititcs.dcs.R;
import com.hititcs.dcs.domain.interactor.airline.GetCompaniesUseCase;
import com.hititcs.dcs.domain.interactor.login.LoginUseCase;
import com.hititcs.dcs.domain.model.Airline;
import com.hititcs.dcs.domain.model.AirlineListResponse;
import com.hititcs.dcs.domain.model.AuthModel;
import com.hititcs.dcs.domain.model.LoginRequest;
import com.hititcs.dcs.util.AppUtils;
import com.hititcs.dcs.util.DialogUtil;
import com.hititcs.dcs.util.FontUtils;
import com.hititcs.dcs.util.StringUtils;
import com.hititcs.dcs.view.BaseActivity;
import com.hititcs.dcs.view.home.view.HomeActivity;
import com.hititcs.dcs.widget.AutoCompleteDropDown;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.io.File;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class LoginActivity extends BaseActivity<LoginActivity> {

  public static final String TOKEN_ERROR_ACTION = "tokenErrorAction";

  @BindView(R.id.btn_login)
  AppCompatButton loginButton;
  @BindView(R.id.edt_username)
  TextInputEditText twUsername;
  @BindView(R.id.edt_password)
  TextInputEditText twPassword;
  @BindView(R.id.auto_drop)
  AutoCompleteDropDown dropDown;
  @BindView(R.id.txt_input_company)
  TextInputLayout txtInputCompany;
  @BindView(R.id.txt_input_username)
  TextInputLayout tilInputUsername;
  @BindView(R.id.txt_input_password)
  TextInputLayout tilInputPassword;

  @Inject
  LoginUseCase loginUseCase;
  @Inject
  GetCompaniesUseCase getCompaniesUseCase;

  private CompanyArrayAdapter adapter;
  private CompositeDisposable compositeDisposable;
  private FirebaseStorage firebaseStorage;
  private StorageReference firebaseStorageRef;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    bindView();
    if (getIntent().getAction() != null && getIntent().getAction().equals(TOKEN_ERROR_ACTION)) {
      getAuthManager().clear();
      showTokenErrorDialog();
    }
    if (getAuthManager().isContain()) {
      navigateToHomeActivity();
    } else {
      getAuthManager().clear();
    }
    firebaseStorage = FirebaseStorage.getInstance();
    firebaseStorageRef = firebaseStorage.getReference();
    dropDown.setOnClickListener(v -> {
          txtInputCompany.setErrorEnabled(false);
          if (adapter == null) {
            getCompanies();
          }
        }
    );
  }

  private void showTokenErrorDialog() {
    DialogUtil.showNativeAlertDialog(context(), R.string.general_token_error_popup_title,
        R.string.general_token_error_popup_message, R.string.general_popup_ok,
        (OnClickListener) (dialog, which) -> {
          if (dialog != null) {
            dialog.dismiss();
          }
        }, R.string.general_popup_cancel, null);
  }

  private boolean validate() {
    return dropDown.getPosition() >= 0 && !StringUtils.isEmpty(twUsername.getText().toString())
        && !StringUtils.isEmpty(twPassword.getText().toString());
  }

  @OnClick(R.id.btn_login)
  public void onClickLoginButton() {
    if (!validate()) {
      return;
    }
    this.showProgressDialog();
    LoginRequest request = new LoginRequest();
    request.setAirline("test");
    request.setUsername(twUsername.getText().toString());
    request.setPassword(twPassword.getText().toString());
    request.setAirlineCode(adapter.getItem(dropDown.getPosition()).getAirlineCode());
    loginUseCase.execute(new SingleObserver<AuthModel>() {
      @Override
      public void onSubscribe(Disposable d) {
        compositeDisposable.add(d);
      }

      @Override
      public void onSuccess(AuthModel authModel) {
        hideProgressDialog();
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content))
            .getChildAt(0);
        AppUtils.hideKeyboardFrom(context(), viewGroup);
        downloadCompanyLogoFromStorage();
      }

      @Override
      public void onError(Throwable e) {
        Toast.makeText(LoginActivity.this, "INVALID USERNAME OR PASSWORD", Toast.LENGTH_LONG)
            .show();
        hideProgressDialog();
      }
    }, request);
  }

  private void downloadCompanyLogoFromStorage() {
    this.showProgressDialog();
    File rootPath = new File(context().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "");
    File localFile = new File(rootPath, "companyLogo.png");
    if (localFile != null && localFile.exists()) {
      localFile.delete();
      localFile = new File(rootPath, "companyLogo.png");
    }
    StorageReference tempStorageReference;
    tempStorageReference = firebaseStorageRef
        .child(String.format("%s.png", adapter.getItem(dropDown.getPosition()).getAirlineCode()));
    File finalLocalFile = localFile;
    tempStorageReference.getFile(localFile).addOnSuccessListener(uri -> {
      hideProgressDialog();
      jump();
    }).addOnFailureListener(
        e -> {
          Timber.e("Error getting companyLogo from firebase");
          Toast.makeText(context(), "Something went wrong, please try again.", Toast.LENGTH_LONG)
              .show();
          hideProgressDialog();
        });
  }

  private void getCompanies() {
    showProgressDialog();
    getCompaniesUseCase.execute(new SingleObserver<AirlineListResponse>() {
      @Override
      public void onSubscribe(Disposable d) {
        compositeDisposable.add(d);
      }

      @Override
      public void onSuccess(AirlineListResponse airlineListResponse) {
        showCompanies(airlineListResponse.getAirlineList());
        hideProgressDialog();
      }

      @Override
      public void onError(Throwable e) {
        hideProgressDialog();
      }
    });
  }

  private void showCompanies(List<Airline> activeCompanies) {
    adapter = new CompanyArrayAdapter(this, R.layout.item_select_company,
        activeCompanies);
    dropDown.setThreshold(1);
    dropDown.setAdapter(adapter);
    dropDown.setOnTouchListener((v, event) -> {
      adapter.setSelectedItemPosition(dropDown.getPosition());
      return false;
    });
    dropDown.showDropDown();
  }

  private void setupTextInputLayoutsTitles() {
    FontUtils.setTextInputLayoutHintBold(
        context(),
        R.font.poppins_bold,
        tilInputUsername,
        tilInputPassword
    );
  }

  @Override
  protected LoginActivity getActivity() {
    return this;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (compositeDisposable != null) {
      compositeDisposable.dispose();
    }
  }

  private void jump() {
    startActivity(new Intent(this, HomeActivity.class));
    finish();
  }

  static class CompanyArrayAdapter extends ArrayAdapter<Airline> {

    private int selectedItemPosition = -1;
    private Typeface extraLight;
    private Typeface bold;

    CompanyArrayAdapter(@NonNull Context context, int resource,
        @NonNull List<Airline> objects) {
      super(context, resource, objects);
      extraLight = ResourcesCompat.getFont(context, R.font.poppins_extra_light);
      bold = ResourcesCompat.getFont(context, R.font.poppins_bold);
    }

    void setSelectedItemPosition(int selectedItemPosition) {
      this.selectedItemPosition = selectedItemPosition;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      final View view = super.getView(position, convertView, parent);
      CheckedTextView textView = view.findViewById(android.R.id.text1);
      if (selectedItemPosition > -1 && position == selectedItemPosition) {
        textView.setChecked(true);
        textView.setTypeface(bold);
      } else {
        textView.setChecked(false);
        textView.setTypeface(extraLight);
      }
      return view;
    }
  }

  @Override
  public void onBackPressed() {
    if (isTaskRoot()) {
      moveTaskToBack(true);
    } else {
      super.onBackPressed();
    }
  }
}
