package com.hititcs.dcs.view.flight.detail;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import com.hititcs.dcs.R;
import com.hititcs.dcs.domain.model.FlightDetail;
import com.hititcs.dcs.domain.model.FlightSummary;
import com.hititcs.dcs.model.DeviceEnum;
import com.hititcs.dcs.util.AnimUtils;
import com.hititcs.dcs.util.AppUtils;
import com.hititcs.dcs.util.DateTimeUtils;
import com.hititcs.dcs.util.DeviceUtils;
import com.hititcs.dcs.util.ImageUtils;
import com.hititcs.dcs.util.MessageUtils;
import com.hititcs.dcs.util.ParcelUtils;
import com.hititcs.dcs.view.BaseFragment;
import com.hititcs.dcs.view.Presenter;
import com.hititcs.dcs.view.barcode.ScanBarcodeActivity;
import javax.inject.Inject;

public class FlightDetailFragment extends BaseFragment<FlightDetailFragment> implements
    FlightDetailView {

  public static final String FLIGHT_ID = "FLIGHT_ID";
  public static final String BOARDED_COUNT_START = "BOARDED_COUNT_START";
  public static final String EXTRA_FLIGHT = "extra:flight";
  @Inject
  FlightDetailPresenter flightDetailPresenter;

  @BindView(R.id.scan_barcode)
  LinearLayout scanBarcodeButton;

  @BindView(R.id.tv_dep_port)
  TextView tvDepPort;
  @BindView(R.id.tv_arr_port)
  TextView tvArrPort;
  @BindView(R.id.tv_flight_type)
  TextView tvFlightType;
  @BindView(R.id.tv_dep_date)
  TextView tvDepDate;
  @BindView(R.id.tv_arr_date)
  TextView tvArrDate;
  @BindView(R.id.tv_boarding_time)
  TextView tvBoardingTime;
  @BindView(R.id.tv_gate)
  TextView tvGate;
  @BindView(R.id.tv_flight_no)
  TextView tvFlightNo;
  @BindView(R.id.tv_aircraft)
  TextView tvAircraft;
  @BindView(R.id.tv_reg)
  TextView tvReg;
  @BindView(R.id.tv_gate_detail)
  TextView tvGateDetail;
  @BindView(R.id.tv_check_in)
  TextView tvCheckIn;
  @BindView(R.id.tv_unboarded)
  TextView tvUnboarded;
  @BindView(R.id.tv_boarded)
  TextView tvBoarded;
  @BindView(R.id.tv_boarding_gate)
  TextView tvBoardingGate;
  @BindView(R.id.tv_delay_time)
  TextView tvDelayTime;
  @BindView(R.id.rlt_flight_detail)
  RelativeLayout rltFlightDetail;
  @BindView(R.id.tv_flight_status)
  TextView tvTopFlightStatus;
  @BindView(R.id.tv_flight_status_data)
  TextView tvFlightStatus;
  @BindView(R.id.iv_company_logo)
  ImageView ivCompanyLogo;
  private String boardedCount;
  private FlightSummary flightSummary;
  private Drawable companyLogo;

  public static FlightDetailFragment newInstance(FlightSummary flightSummary) {
    Bundle args = new Bundle();
    ParcelUtils.wrap(args, EXTRA_FLIGHT, flightSummary);
    FlightDetailFragment fragment = new FlightDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    flightSummary = ParcelUtils.unwrap(getArguments(), EXTRA_FLIGHT);
  }

  @Override
  protected FlightDetailFragment getFragment() {
    return this;
  }

  @Override
  protected Presenter getPresenter() {
    return flightDetailPresenter;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View fragmentView = inflater.inflate(R.layout.content_flight_detail, container, false);
    bindView(fragmentView);
    return fragmentView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  public void hideBoarding() {
    rltFlightDetail.setVisibility(View.GONE);
  }

  @Override
  public void onResume() {
    super.onResume();
    flightDetailPresenter.setView(this);
    flightDetailPresenter.getFlightDetail(flightSummary.getFlightId());
  }

  @Override
  public void onPause() {
    flightDetailPresenter.destroyView();
    super.onPause();
  }

  @Override
  public void showFlightDetail(FlightDetail flightsOutputDto) {
    tvDepPort.setText(flightsOutputDto.getFlightSummary().getDepPort());
    tvArrPort.setText(flightsOutputDto.getFlightSummary().getArrPort());
    tvFlightNo.setText(String.format("%s%s", flightsOutputDto.getFlightSummary().getAirlineCode(),
        flightsOutputDto.getFlightSummary().getFlightNumber()));
    tvFlightType.setText(flightsOutputDto.getFlightSummary().getAirlineCode());
    tvGate.setText(flightsOutputDto.getFlightSummary().getBoardingGate());
    tvBoardingTime.setText(String.format("%s: %s", getString(R.string.item_flight_boarding_time),
        DateTimeUtils
            .getTimeFromZonedDateTime(flightsOutputDto.getFlightSummary().getBoardingTime())));
    tvDepDate.setText(
        DateTimeUtils.getTimeFromZonedDateTime(flightsOutputDto.getFlightSummary().getDepTime()));
    tvArrDate.setText(
        DateTimeUtils.getTimeFromZonedDateTime(flightsOutputDto.getFlightSummary().getArrTime()));
    tvAircraft.setText(flightsOutputDto.getAircraftInfo().getAirEquipTypeModel());
    tvReg.setText(flightsOutputDto.getAircraftInfo().getString());
    tvGateDetail.setText(flightsOutputDto.getPortInfo().getGate());
    companyLogo = ImageUtils
        .loadImageUriAsDrawable(getContext(), AppUtils.getCompanyLogoUri(getContext()));
    if (companyLogo != null) {
      ivCompanyLogo.setImageDrawable(companyLogo);
      ivCompanyLogo.setVisibility(View.VISIBLE);
    } else {
      ivCompanyLogo.setVisibility(View.INVISIBLE);
    }
    if (!MessageUtils.getStringFromList(flightsOutputDto.getBoarded()).isEmpty()) {
      tvBoarded.setText(MessageUtils.getStringFromList(flightsOutputDto.getBoarded()));
    } else {
      tvBoarded.setText("0");
    }
    boardedCount = MessageUtils.getStringFromList(flightsOutputDto.getBoarded());
    if (!MessageUtils.getStringFromList(flightsOutputDto.getCheckedIn()).isEmpty()) {
      tvCheckIn.setText(MessageUtils.getStringFromList(flightsOutputDto.getCheckedIn()));
    } else {
      tvCheckIn.setText("0");
    }
    if (!MessageUtils.getStringFromList(flightsOutputDto.getUnboarded()).isEmpty()) {
      tvUnboarded.setText(MessageUtils.getStringFromList(flightsOutputDto.getUnboarded()));
    } else {
      tvUnboarded.setText("0");
    }

    tvBoardingGate.setText(flightsOutputDto.getFlightSummary().getBoardingGate());
    tvDelayTime.setText(
        DateTimeUtils.normalizeZonedDateTime(flightsOutputDto.getFlightSummary().getDelayTime()));
    tvTopFlightStatus.setText(String.format("%s: %s", getString(R.string.item_flight_flight_status),
        flightsOutputDto.getFlightSummary().getFlightStatus()));
    tvFlightStatus.setText(flightsOutputDto.getFlightSummary().getFlightStatus());
    rltFlightDetail.setVisibility(View.VISIBLE);
    AnimUtils.animateShowView(rltFlightDetail);
  }

  @Override
  public void showLoading() {
    hideBoarding();
    super.showLoading();
  }

  @Override
  public void hideLoading() {
    super.hideLoading();
  }

  @Override
  public void showError(String message) {

  }

  @Override
  public void showError(int messageId) {

  }

  private void openScanBarcodeZebra() {
    Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
    intent.putExtra(FLIGHT_ID, flightSummary.getFlightId());
    intent.putExtra(BOARDED_COUNT_START, boardedCount);
    intent.putExtra(ScanBarcodeActivity.SELECTED_DEVICE, DeviceEnum.ZEBRA.getValue());
    startActivity(intent);
  }

  private void openScanBarcodeKranger() {
    Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
    intent.putExtra(FLIGHT_ID, flightSummary.getFlightId());
    intent.putExtra(BOARDED_COUNT_START, boardedCount);
    intent.putExtra(ScanBarcodeActivity.SELECTED_DEVICE, DeviceEnum.K_RANGER.getValue());
    startActivity(intent);
  }

  private void openScanBarcodeCamera() {
    Intent intent = new Intent(getActivity(), ScanBarcodeActivity.class);
    intent.putExtra(FLIGHT_ID, flightSummary.getFlightId());
    intent.putExtra(BOARDED_COUNT_START, boardedCount);
    intent.putExtra(ScanBarcodeActivity.SELECTED_DEVICE, DeviceEnum.CAMERA.getValue());
    startActivity(intent);
  }

  private void showCameraAndZebraDeviceSelectionDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.dialog_title_select_a_device)
        .setItems(R.array.barcode_devices_array_zebra, (dialog, selectedPosition) -> {
          if (selectedPosition == 0) {
            openScanBarcodeZebra();
          } else if (selectedPosition == 1) {
            openScanBarcodeCamera();
          }
        });
    builder.create();
    builder.show();
  }

  private void showCameraAndKrangerDeviceSelectionDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.dialog_title_select_a_device)
        .setItems(R.array.barcode_devices_array_kranger, (dialog, selectedPosition) -> {
          if (selectedPosition == 0) {
            openScanBarcodeKranger();
          } else if (selectedPosition == 1) {
            openScanBarcodeCamera();
          }
        });
    builder.create();
    builder.show();
  }

  @OnClick(R.id.scan_barcode)
  public void onScanBarcodeClicked() {
    if (DeviceUtils.isManufacturerZebra()) {
      showCameraAndZebraDeviceSelectionDialog();
    } else if (DeviceUtils.isModelKrangerRow()) {
      showCameraAndKrangerDeviceSelectionDialog();
    } else {
      openScanBarcodeCamera();
    }
  }
}
