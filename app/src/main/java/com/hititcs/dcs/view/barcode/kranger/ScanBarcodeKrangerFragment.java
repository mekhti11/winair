package com.hititcs.dcs.view.barcode.kranger;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.BindView;
import butterknife.OnClick;
import com.hititcs.dcs.R;
import com.hititcs.dcs.domain.model.BoardWithBarcodeRequest;
import com.hititcs.dcs.domain.model.BoardingResponse;
import com.hititcs.dcs.util.MessageUtils;
import com.hititcs.dcs.view.BaseFragment;
import com.hititcs.dcs.view.Presenter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import javax.inject.Inject;
import timber.log.Timber;

import static com.hititcs.dcs.view.flight.detail.FlightDetailFragment.BOARDED_COUNT_START;
import static com.hititcs.dcs.view.flight.detail.FlightDetailFragment.FLIGHT_ID;

public class ScanBarcodeKrangerFragment extends BaseFragment<ScanBarcodeKrangerFragment>
    implements
    ScanBarcodeKrangerContract.ScanBarcodeKrangerView {

  private static final String ACTION_BARCODE = "com.barcodeservice.broadcast.string";
  private static final String ACTION_START_BCR_SERVICE = "com.barcodeservice.on";
  private static final String ACTION_STOP_BCR_SERVICE = "com.barcodeservice.off";

  private static final String ACTION_TRIGGER_ON = "com.barcodeservice.start.scan";
  private static final String ACTION_TRIGGER_OFF = "com.barcodeservice.stop.scan";

  private static final String EXTRA_BARCODE = "barcodedata";

  private static final long SUCCESS_DELAY_BETWEEN_SCANS = 1000L;
  private static final long ERROR_DELAY_BETWEEN_SCANS = 1000L;

  @BindView(R.id.img_close)
  AppCompatImageView imgClose;
  @BindView(R.id.img_complete)
  AppCompatButton startScanningBtn;
  @BindView(R.id.img_barcode_success)
  AppCompatImageView imgBarcodeSuccess;
  @BindView(R.id.img_barcode_error)
  AppCompatImageView imgBarcodeError;
  @BindView(R.id.barcode_error_txt)
  TextView barcodeErrorTxt;
  @BindView(R.id.tw_boarded_count)
  TextView boardedCount;
  @Inject
  ScanBarcodeKrangerContract.ScanBarcodeKrangerPresenter presenter;
  BroadcastReceiver mReceiver;
  private String flightId;
  private String boardedCountStart;
  private MediaPlayer mediaPlayer = new MediaPlayer();
  private Uri successAudioUri;
  private Uri failAudioUri;

  public static ScanBarcodeKrangerFragment newInstance(String flightId, String boardedCountStart) {
    Bundle args = new Bundle();
    ScanBarcodeKrangerFragment
        fragment = new ScanBarcodeKrangerFragment();
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
    setupBroadcastReceiver();
  }

  private void setupBroadcastReceiver() {
    mReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BARCODE)) {
          String barcode = intent.getStringExtra(EXTRA_BARCODE);
          if (barcode != null) {
            stopBarcodeService();
            BoardWithBarcodeRequest boardWithBarcodeRequest = new BoardWithBarcodeRequest();
            boardWithBarcodeRequest.setBarcode(barcode);
            boardWithBarcodeRequest.setFlightId(flightId);
            presenter.scanBarcode(boardWithBarcodeRequest,
                new ScanBarcodeKrangerFragment.BarcodeReadKrangerListener(getFragment()));
          }
        }
      }
    };
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View fragmentView = inflater.inflate(R.layout.content_kranger_scan, container, false);
    bindView(fragmentView);
    return fragmentView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    setupAudios();
    boardedCount.setText(boardedCountStart);
  }

  private void setupAudios() {
    successAudioUri = new Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(context().getPackageName())
        .path(String.valueOf(R.raw.success))
        .build();
    failAudioUri = new Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(context().getPackageName())
        .path(String.valueOf(R.raw.error))
        .build();
  }

  @Override
  protected ScanBarcodeKrangerFragment getFragment() {
    return this;
  }

  @Override
  protected Presenter getPresenter() {
    return presenter;
  }

  @Override
  public void showBarcodeResult(BoardingResponse data) {

  }

  @Override
  public void showLoading() {

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
  public void onResume() {
    super.onResume();
    getActivity().registerReceiver(mReceiver, new IntentFilter(ACTION_BARCODE));
  }

  @Override
  public void onPause() {
    super.onPause();
    stopBarcodeService();
    showStartScanningBtn();
    getActivity().unregisterReceiver(mReceiver);
  }

  private void playSuccessAudio() {
    mediaPlayer.reset();
    try {
      mediaPlayer.setDataSource(context(), successAudioUri);
      mediaPlayer.prepare();
    } catch (IOException e) {
      Timber.e(e);
    }
    mediaPlayer.seekTo(0);
    mediaPlayer.start();
  }

  private void playFailAudio() {
    mediaPlayer.reset();
    try {
      mediaPlayer.setDataSource(context(), failAudioUri);
      mediaPlayer.prepare();
    } catch (IOException e) {
      Timber.e(e);
    }
    mediaPlayer.seekTo(0);
    mediaPlayer.start();
  }

  @Override public void onDestroy() {
    mediaPlayer.release();
    mediaPlayer = null;
    super.onDestroy();
  }

  @OnClick(R.id.img_complete)
  public void onPressedStartScanning(View view) {
    hideStartScanningBtn();
    startBarcodeService();
  }

  public void startBarcodeService() {
    getActivity().sendBroadcast(new Intent(ACTION_START_BCR_SERVICE));
  }

  public void stopBarcodeService() {
    getActivity().sendBroadcast(new Intent(ACTION_STOP_BCR_SERVICE));
  }

  private void showStartScanningBtn() {
    startScanningBtn.setVisibility(View.VISIBLE);
  }

  private void hideStartScanningBtn() {
    startScanningBtn.setVisibility(View.GONE);
  }

  @OnClick(R.id.img_close)
  public void closeOnClick() {
    getActivity().onBackPressed();
  }

  private void showResultIcon(boolean success, String errorMsg) {
    long delayTime = SUCCESS_DELAY_BETWEEN_SCANS;
    if (success) {
      playSuccessAudio();
      imgBarcodeSuccess.setVisibility(View.VISIBLE);
    } else {
      delayTime = ERROR_DELAY_BETWEEN_SCANS;
      playFailAudio();
      barcodeErrorTxt.setText(errorMsg);
      imgBarcodeError.setVisibility(View.VISIBLE);
      barcodeErrorTxt.setVisibility(View.VISIBLE);
    }
    new Handler().postDelayed(new ScanBarcodeKrangerFragment.HandleFinishKranger(this), delayTime);
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

  private static class BarcodeReadKrangerListener
      implements ScanBarcodeKrangerFragment.ResponseListener<BoardingResponse> {

    private final WeakReference<ScanBarcodeKrangerFragment>
        mBarcodeScannerProductViewWeakReference;

    public BarcodeReadKrangerListener(ScanBarcodeKrangerFragment scanBarcodeKrangerFragment) {
      mBarcodeScannerProductViewWeakReference = new WeakReference<>(scanBarcodeKrangerFragment);
    }

    @Override
    public void onResponse(@Nullable BoardingResponse response) {
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

  class HandleFinishKranger implements Runnable {

    private WeakReference<ScanBarcodeKrangerFragment> scanBarcodeFragmentWeakReference;

    public HandleFinishKranger(ScanBarcodeKrangerFragment scanBarcodeFragment) {
      scanBarcodeFragmentWeakReference = new WeakReference<>(scanBarcodeFragment);
    }

    @Override
    public void run() {
      if (scanBarcodeFragmentWeakReference.get() != null && scanBarcodeFragmentWeakReference.get()
          .isAttached()) {
        scanBarcodeFragmentWeakReference.get().imgBarcodeSuccess.setVisibility(View.GONE);
        scanBarcodeFragmentWeakReference.get().imgBarcodeError.setVisibility(View.GONE);
        scanBarcodeFragmentWeakReference.get().barcodeErrorTxt.setVisibility(View.GONE);
        startBarcodeService();
      }
    }
  }
}
