package com.hititcs.dcs.view.barcode;

import android.content.ContentResolver;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKManager.FEATURE_TYPE;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.BarcodeManager.ConnectionState;
import com.symbol.emdk.barcode.BarcodeManager.ScannerConnectionListener;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.ScanDataCollection.ScanData;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.Scanner.DataListener;
import com.symbol.emdk.barcode.Scanner.StatusListener;
import com.symbol.emdk.barcode.Scanner.TriggerType;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerInfo;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;
import com.symbol.emdk.barcode.StatusData.ScannerStates;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

import static com.hititcs.dcs.view.flight.detail.FlightDetailFragment.BOARDED_COUNT_START;
import static com.hititcs.dcs.view.flight.detail.FlightDetailFragment.FLIGHT_ID;

public class ScanBarcodeZebraFragment extends BaseFragment<ScanBarcodeZebraFragment> implements
    ScanBarcodeView, EMDKListener, ScannerConnectionListener, DataListener, StatusListener {

  private static final long SUCCESS_DELAY_BETWEEN_SCANS = 1000L;
  private static final long ERROR_DELAY_BETWEEN_SCANS = 1000L;

  @BindView(R.id.img_close)
  AppCompatImageView imgClose;
  @BindView(R.id.img_complete)
  AppCompatButton imgComplete;
  @BindView(R.id.img_barcode_success)
  AppCompatImageView imgBarcodeSuccess;
  @BindView(R.id.img_barcode_error)
  AppCompatImageView imgBarcodeError;
  @BindView(R.id.barcode_error_txt)
  TextView barcodeErrorTxt;
  @BindView(R.id.tw_boarded_count)
  TextView boardedCount;
  @Inject
  ScanBarcodePresenter presenter;
  private String flightId;
  private String boardedCountStart;
  private MediaPlayer mediaPlayer = new MediaPlayer();
  private Uri successAudioUri;
  private Uri failAudioUri;
  private EMDKManager emdkManager;
  private BarcodeManager barcodeManager;
  private Scanner scanner;
  private List<ScannerInfo> deviceList = null;
  private int defaultIndex;
  private boolean bSoftTriggerSelected = false;
  private boolean bDecoderSettingsChanged = false;
  private boolean bExtScannerDisconnected = false;
  private Object lock = new Object();

  public static ScanBarcodeZebraFragment newInstance(String flightId, String boardedCountStart) {
    Bundle args = new Bundle();
    ScanBarcodeZebraFragment fragment = new ScanBarcodeZebraFragment();
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

    EMDKResults results = EMDKManager.getEMDKManager(getActivity().getApplicationContext(), this);
    if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
      Timber.d("EMDKManager object request failed!");
      return;
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View fragmentView = inflater.inflate(R.layout.content_zebra_scan, container, false);
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
  protected ScanBarcodeZebraFragment getFragment() {
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
    // The application is in foreground
    if (emdkManager != null) {
      // Acquire the barcode manager resources
      initBarcodeManager();
      enumerateScannerDevices();
      bSoftTriggerSelected = false;
      bExtScannerDisconnected = false;
      deInitScanner();
      initScanner();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    // The application is in background
    // Release the barcode manager resources
    deInitScanner();
    deInitBarcodeManager();
  }

  @Override
  public void onOpened(EMDKManager emdkManager) {
    this.emdkManager = emdkManager;
    initBarcodeManager();
    enumerateScannerDevices();

    bSoftTriggerSelected = false;
    bExtScannerDisconnected = false;
    deInitScanner();
    initScanner();
  }

  private void initBarcodeManager() {
    barcodeManager = (BarcodeManager) emdkManager.getInstance(FEATURE_TYPE.BARCODE);
    // Add connection listener
    if (barcodeManager != null) {
      barcodeManager.addConnectionListener(this);
    }
  }

  private void deInitBarcodeManager() {
    if (emdkManager != null) {
      emdkManager.release(FEATURE_TYPE.BARCODE);
    }
  }

  private void initScanner() {
    if (scanner == null) {
      if ((deviceList != null) && (deviceList.size() != 0)) {
        if (barcodeManager != null) {
          scanner = barcodeManager.getDevice(deviceList.get(0));
        }
      } else {
        Timber
            .w("Failed to get the specified scanner device! Please close and restart the application.");
        return;
      }
      if (scanner != null) {
        scanner.addDataListener(this);
        scanner.addStatusListener(this);
        try {
          scanner.enable();
        } catch (ScannerException e) {
          Timber.e(e);
          deInitScanner();
        }
      } else {
        Timber.w("Failed to initialize the scanner device.");
      }
    }
  }

  private void enumerateScannerDevices() {
    if (barcodeManager != null) {
      List<String> friendlyNameList = new ArrayList<String>();
      int spinnerIndex = 0;
      deviceList = barcodeManager.getSupportedDevicesInfo();
      if ((deviceList != null) && (deviceList.size() != 0)) {
        Iterator<ScannerInfo> it = deviceList.iterator();
        while (it.hasNext()) {
          ScannerInfo scnInfo = it.next();
          friendlyNameList.add(scnInfo.getFriendlyName());
          if (scnInfo.isDefaultScanner()) {
            defaultIndex = spinnerIndex;
          }
          ++spinnerIndex;
        }
      } else {
        Timber
            .w("Failed to get the list of supported scanner devices! Please close and restart the application.");
      }
    }
  }

  private void deInitScanner() {
    if (scanner != null) {
      try {
        scanner.disable();
      } catch (Exception e) {
        Timber.e(e.getMessage());
      }

      try {
        scanner.removeDataListener(this);
        scanner.removeStatusListener(this);
      } catch (Exception e) {
        Timber.e(e.getMessage());
      }

      try {
        scanner.release();
      } catch (Exception e) {
        Timber.e(e.getMessage());
      }
      scanner = null;
    }
  }

  @Override
  public void onClosed() {
    if (emdkManager != null) {
      emdkManager.release();
      emdkManager = null;
    }
  }

  @Override
  public void onConnectionChange(ScannerInfo scannerInfo, ConnectionState connectionState) {
    String status;
    String scannerName = "";
    String statusExtScanner = connectionState.toString();
    String scannerNameExtScanner = scannerInfo.getFriendlyName();
    if (deviceList.size() != 0) {
      scannerName = deviceList.get(0).getFriendlyName();
    }
    if (scannerName.equalsIgnoreCase(scannerNameExtScanner)) {
      switch (connectionState) {
        case CONNECTED:
          bSoftTriggerSelected = false;
          synchronized (lock) {
            initScanner();
            bExtScannerDisconnected = false;
          }
          break;
        case DISCONNECTED:
          bExtScannerDisconnected = true;
          synchronized (lock) {
            deInitScanner();
          }
          break;
      }
      status = scannerNameExtScanner + ":" + statusExtScanner;
      Timber.d(status);
    } else {
      bExtScannerDisconnected = false;
      Timber.d("scannerNameExtScanner + \":\" + statusExtScanner;");
    }
  }

  @Override
  public void onData(ScanDataCollection scanDataCollection) {
    if ((scanDataCollection != null) && (scanDataCollection.getResult()
        == ScannerResults.SUCCESS)) {
      ArrayList<ScanData> scanData = scanDataCollection.getScanData();
      for (ScanData data : scanData) {
        if (!TextUtils.isEmpty(data.getData())) {
          Timber.e("<font color='gray'>" + data.getLabelType() + "</font> : " + data.getData());
          BoardWithBarcodeRequest boardWithBarcodeRequest = new BoardWithBarcodeRequest();
          boardWithBarcodeRequest.setBarcode(data.getData());
          boardWithBarcodeRequest.setFlightId(flightId);
          presenter.scanBarcode(boardWithBarcodeRequest, new BarcodeReadListener(this));
          break;
        }
      }
    }
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

  @Override
  public void onStatus(StatusData statusData) {
    ScannerStates state = statusData.getState();
    String statusString = "";
    switch (state) {
      case IDLE:
        statusString = statusData.getFriendlyName() + " is enabled and idle...";
        Timber.d(statusString);
        // set trigger type
        if (bSoftTriggerSelected) {
          scanner.triggerType = TriggerType.SOFT_ONCE;
          bSoftTriggerSelected = false;
        } else {
          scanner.triggerType = TriggerType.HARD;
        }
        // set decoders
        if (bDecoderSettingsChanged) {
          setDecoders();
          bDecoderSettingsChanged = false;
        }
        // submit read
        if (!scanner.isReadPending() && !bExtScannerDisconnected) {
          try {
            scanner.read();
          } catch (ScannerException e) {
            Timber.e(e.getMessage());
          }
        }
        break;
      case WAITING:
        statusString = "Scanner is waiting for trigger press...";
        Timber.d(statusString);
        break;
      case SCANNING:
        statusString = "Scanning...";
        Timber.d(statusString);
        break;
      case DISABLED:
        statusString = statusData.getFriendlyName() + " is disabled.";
        Timber.d(statusString);
        break;
      case ERROR:
        statusString = "An error has occurred.";
        Timber.d(statusString);
        break;
      default:
        break;
    }
  }

  private void setDecoders() {
    if (scanner != null) {
      try {
        ScannerConfig config = scanner.getConfig();

        // Set EAN8
        config.decoderParams.ean8.enabled = true;
        // Set EAN13
        config.decoderParams.ean13.enabled = true;
        // Set Code39
        config.decoderParams.code39.enabled = true;
        //Set Code128
        config.decoderParams.code128.enabled = true;
        scanner.setConfig(config);
      } catch (ScannerException e) {
        Timber.e(e.getMessage());
      }
    }
  }

  @OnClick(R.id.img_complete)
  public void softScan(View view) {
    bSoftTriggerSelected = true;
    cancelRead();
  }

  @OnClick(R.id.img_close)
  public void closeOnClick() {
    getActivity().onBackPressed();
  }

  private void cancelRead() {
    if (scanner != null) {
      if (scanner.isReadPending()) {
        try {
          scanner.cancelRead();
        } catch (ScannerException e) {
          Timber.e(e.getMessage());
        }
      }
    }
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
    new Handler().postDelayed(new ScanBarcodeZebraFragment.HandleFinish(this), delayTime);
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

  private static class BarcodeReadListener implements
      ScanBarcodeFragment.ResponseListener<BoardingResponse> {

    private final WeakReference<ScanBarcodeZebraFragment>
        mBarcodeScannerProductViewWeakReference;

    public BarcodeReadListener(
        ScanBarcodeZebraFragment scanBarcodeZebraFragment) {
      mBarcodeScannerProductViewWeakReference = new WeakReference<>(scanBarcodeZebraFragment);
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

  class HandleFinish implements Runnable {

    private WeakReference<ScanBarcodeZebraFragment> scanBarcodeFragmentWeakReference;

    public HandleFinish(ScanBarcodeZebraFragment scanBarcodeFragment) {
      scanBarcodeFragmentWeakReference = new WeakReference<>(scanBarcodeFragment);
    }

    @Override
    public void run() {
      if (scanBarcodeFragmentWeakReference.get() != null && scanBarcodeFragmentWeakReference.get()
          .isAttached()) {
        scanBarcodeFragmentWeakReference.get().imgBarcodeSuccess.setVisibility(View.GONE);
        scanBarcodeFragmentWeakReference.get().imgBarcodeError.setVisibility(View.GONE);
        scanBarcodeFragmentWeakReference.get().barcodeErrorTxt.setVisibility(View.GONE);
      }
    }
  }
}

