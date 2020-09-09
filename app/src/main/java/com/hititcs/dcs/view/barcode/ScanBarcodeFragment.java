package com.hititcs.dcs.view.barcode;

import static com.hititcs.dcs.view.flight.detail.FlightDetailFragment.BOARDED_COUNT_START;
import static com.hititcs.dcs.view.flight.detail.FlightDetailFragment.FLIGHT_ID;

import android.Manifest;
import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.zxing.ResultPoint;
import com.hititcs.dcs.R;
import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.util.MessageUtils;
import com.hititcs.dcs.view.BaseFragment;
import com.hititcs.dcs.view.Presenter;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class ScanBarcodeFragment extends BaseFragment<ScanBarcodeFragment> implements
    ScanBarcodeView,
    DecoratedBarcodeView.TorchListener {

  private static final String STATE_FLASH_OPEN = "state:flashOpen";
  private static final String STATE_CAMERA_PAUSE = "state:cameraPause";

  public static final int MY_PERMISSIONS_REQUEST_CAMERA = 124;

  private static final long SUCCESS_DELAY_BETWEEN_SCANS = 1000L;
  private static final long ERROR_DELAY_BETWEEN_SCANS = 3000L;

  @BindView(R.id.img_switch_flashlight)
  AppCompatImageView imgFlash;
  @BindView(R.id.img_close)
  AppCompatImageView imgClose;
  @BindView(R.id.img_pause)
  AppCompatImageView imgCameraPause;
  @BindView(R.id.barcodePreview)
  AppCompatImageView imgLastBarcode;
  @BindView(R.id.img_complete)
  AppCompatImageView imgComplete;
  @BindView(R.id.img_barcode_success)
  AppCompatImageView imgBarcodeSuccess;
  @BindView(R.id.img_barcode_error)
  AppCompatImageView imgBarcodeError;
  @BindView(R.id.zxing_barcode_scanner)
  DecoratedBarcodeView barcodeScannerView;
  @BindView(R.id.barcode_error_txt)
  TextView barcodeErrorTxt;
  @BindView(R.id.tw_boarded_count)
  TextView boardedCount;

  private String flightId;
  private String boardedCountStart;
  private MediaPlayer successSound;
  private MediaPlayer failSound;
  boolean mFlashOpen = false;
  boolean mCameraPause = false;

  Drawable mOpenFlashDrawable;
  Drawable mCloseFlashDrawable;
  Drawable mPauseDrawable;
  Drawable mPlayDrawable;

  @Inject
  ScanBarcodePresenter scanBarcodePresenter;

  public ScanBarcodeFragment() {
  }

  public static ScanBarcodeFragment newInstance(String flightId, String boardedCountStart) {
    Bundle args = new Bundle();
    ScanBarcodeFragment fragment = new ScanBarcodeFragment();
    args.putString(FLIGHT_ID, flightId);
    args.putString(BOARDED_COUNT_START, boardedCountStart);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    flightId = getArguments().getString(FLIGHT_ID);
    boardedCountStart = getArguments().getString(BOARDED_COUNT_START);

    successSound = MediaPlayer.create(getContext(), R.raw.success);
    failSound = MediaPlayer.create(getContext(), R.raw.error);
  }

  @Override
  protected ScanBarcodeFragment getFragment() {
    return this;
  }

  @Override
  protected Presenter getPresenter() {
    return scanBarcodePresenter;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View fragmentView = inflater.inflate(R.layout.content_scan_barcode, container, false);
    bindView(fragmentView);

    return fragmentView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (ContextCompat.checkSelfPermission(getActivity(), permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
      // Permission is not granted
      ActivityCompat
          .requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
              MY_PERMISSIONS_REQUEST_CAMERA);
    }

    //    scanBarcodePresenter.scanBarcode();
    if (savedInstanceState != null) {
      mFlashOpen = savedInstanceState.getBoolean(STATE_FLASH_OPEN, false);
      mCameraPause = savedInstanceState.getBoolean(STATE_CAMERA_PAUSE, false);
    } else {
      barcodeScannerView.decodeContinuous(new BarcodeCallbackImpl(this));
    }

    initFlash();

    boardedCount.setText(boardedCountStart);
  }

  private boolean hasFlash() {
    return getActivity().getApplicationContext().getPackageManager()
        .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
  }

  public void switchFlashlight() {
    if (mFlashOpen) {
      barcodeScannerView.setTorchOff();
      mFlashOpen = false;
    } else {
      barcodeScannerView.setTorchOn();
      mFlashOpen = true;
    }
  }

  private void toggleCamera() {
    if (mCameraPause) {
      toggleBarcodeView(false);
    } else {
      toggleBarcodeView(true);
    }
  }

  private void initFlash() {
    if (!hasFlash()) {
      imgFlash.setVisibility(View.GONE);
    } else {
      imgFlash.setOnClickListener(view -> {
        switchFlashlight();
      });
    }
  }

  private void toggleBarcodeView(boolean pause) {
    if (barcodeScannerView != null) {
      mCameraPause = pause;
      if (pause) {
        barcodeScannerView.pause();

      } else {
        barcodeScannerView.resume();
      }
    }
  }

  @Override
  public void onTorchOn() {
    //    imgFlash.setImageDrawable(mCloseFlashDrawable);
  }

  @Override
  public void onTorchOff() {
    //    imgFlash.setImageDrawable(mOpenFlashDrawable);
  }

  @OnClick(R.id.img_pause)
  void onClickPause() {
    toggleCamera();
  }

  @OnClick(R.id.img_close)
  void onClickClose() {
    getActivity().onBackPressed();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putBoolean(STATE_FLASH_OPEN, mFlashOpen);
    outState.putBoolean(STATE_CAMERA_PAUSE, mCameraPause);
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onResume() {
    super.onResume();
    barcodeScannerView.resume();
  }

  @Override
  public void onPause() {
    barcodeScannerView.pause();
    scanBarcodePresenter.destroyView();

    super.onPause();
  }

  @Override
  public void onDestroy() {
    if (barcodeScannerView != null) {
      barcodeScannerView.getBarcodeView().stopDecoding();
      barcodeScannerView = null;
    }
    super.onDestroy();
  }

  @Override
  public void showLoading() {

  }

  @Override
  public void showProgressDialog(int title, int message) {

  }

  @Override
  public void showProgressDialog() {

  }

  @Override
  public void hideProgressDialog() {

  }

  @Override
  public void hideLoading() {

  }

  @Override
  public void showError(String message) {

  }

  @Override
  public void showError(int messageId) {

  }

  @Override
  public void showBarcodeResult(BoardingResponse data) {
    boardedCount.setText(MessageUtils.getStringFromList(data.getFlightDetail().getBoarded()));
    showResultIcon(true, null);
  }

  private void showResultIcon(boolean success, String errorMsg) {
    long delayTime = SUCCESS_DELAY_BETWEEN_SCANS;
    if (success) {
      successSound.start();
      imgBarcodeSuccess.setVisibility(View.VISIBLE);
    } else {
      delayTime = ERROR_DELAY_BETWEEN_SCANS;
      failSound.start();
      barcodeErrorTxt.setText(errorMsg);
      imgBarcodeError.setVisibility(View.VISIBLE);
      barcodeErrorTxt.setVisibility(View.VISIBLE);
    }
    new Handler().postDelayed(new HandleFinish(this), delayTime);
  }

  private void onError(String errorMessage) {
    Timber.d("Service response error %s", errorMessage);
    showResultIcon(false, errorMessage);
  }

  private void onBarcodeResult(BoardingResponse response) {
    Timber.d("Service response %s", response);
    try {
      boardedCount.setText(MessageUtils.getStringFromList(response.getFlightDetail().getBoarded()));
    } catch (Exception e) {
      Timber.e(e);
    }
    showResultIcon(true, null);

  }

  static class BarcodeCallbackImpl implements BarcodeCallback {

    private WeakReference<ScanBarcodeFragment> scanBarcodeFragmentWeakReference;

    public BarcodeCallbackImpl(ScanBarcodeFragment scanBarcodeFragment) {
      scanBarcodeFragmentWeakReference = new WeakReference<>(scanBarcodeFragment);
    }

    @Override
    public void barcodeResult(BarcodeResult result) {
      if (scanBarcodeFragmentWeakReference.get() == null || !scanBarcodeFragmentWeakReference.get()
          .isAttached()) {
        return;
      }
      if (scanBarcodeFragmentWeakReference.get().mCameraPause) {
        return;
      }
      String resultText = result.getText() == null ? "" : result.getText();
      if (TextUtils.isEmpty(resultText)) {
        Timber.d("Barcode okunamadi!"); //TODO unread
        return;
      } else {
        Timber.d("Barcode %s", resultText); //TODO read
        scanBarcodeFragmentWeakReference.get().toggleBarcodeView(true);
        BoardWithBarcodeRequest boardWithBarcodeRequest = new BoardWithBarcodeRequest();
        boardWithBarcodeRequest.setBarcode(resultText);
        boardWithBarcodeRequest.setFlightId(scanBarcodeFragmentWeakReference.get().flightId);
        scanBarcodeFragmentWeakReference.get().scanBarcodePresenter
            .scanBarcode(boardWithBarcodeRequest,
                new BarcodeReadListener(scanBarcodeFragmentWeakReference.get()));
      }
    }

    @Override
    public void possibleResultPoints(List<ResultPoint> resultPoints) {
    }
  }

  private static class BarcodeReadListener implements ResponseListener<BoardingResponse> {

    private final WeakReference<ScanBarcodeFragment>
        mBarcodeScannerProductViewWeakReference;

    public BarcodeReadListener(
        ScanBarcodeFragment scanBarcodeFragment) {
      mBarcodeScannerProductViewWeakReference = new WeakReference<>(scanBarcodeFragment);
    }

    @Override
    public void onResponse(BoardingResponse response) {
      if (mBarcodeScannerProductViewWeakReference.get() != null
          && mBarcodeScannerProductViewWeakReference.get().isAttached()) {
        mBarcodeScannerProductViewWeakReference.get().onBarcodeResult(response);
      }
    }

    @Override
    public void onError(String errorMessage) {
      Timber.d("onError: " + errorMessage);
      if (mBarcodeScannerProductViewWeakReference.get() != null
          && mBarcodeScannerProductViewWeakReference.get().isAttached()) {
        mBarcodeScannerProductViewWeakReference.get().onError(errorMessage);
      }
    }
  }

  public interface ResponseListener<T> {

    /**
     * Fired when request is successful
     *
     * @param response result
     */
    void onResponse(@Nullable T response);

    /**
     * Fired when request is failed
     *
     * @param errorMessage error message or null
     */
    void onError(String errorMessage);
  }

  class HandleFinish implements Runnable {

    private WeakReference<ScanBarcodeFragment> scanBarcodeFragmentWeakReference;

    public HandleFinish(ScanBarcodeFragment scanBarcodeFragment) {
      scanBarcodeFragmentWeakReference = new WeakReference<>(scanBarcodeFragment);
    }

    @Override
    public void run() {
      if (scanBarcodeFragmentWeakReference.get() != null && scanBarcodeFragmentWeakReference.get()
          .isAttached()) {
        scanBarcodeFragmentWeakReference.get().imgBarcodeSuccess.setVisibility(View.GONE);
        scanBarcodeFragmentWeakReference.get().imgBarcodeError.setVisibility(View.GONE);
        scanBarcodeFragmentWeakReference.get().barcodeErrorTxt.setVisibility(View.GONE);
        toggleBarcodeView(false);
      }
    }
  }
}
