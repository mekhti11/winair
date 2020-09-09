package com.hititcs.dcs.view.flight;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import com.hititcs.dcs.R;
import com.hititcs.dcs.view.ItemViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlightListViewHolder extends ItemViewHolder {

  @BindView(R.id.tv_flight_no)
  TextView tvFlightNumber;
  @BindView(R.id.tv_dep_port)
  TextView tvDepPort;
  @BindView(R.id.tv_arr_port)
  TextView tvArrPort;
  @BindView(R.id.tv_dep_date)
  TextView tvDepDate;
  @BindView(R.id.tv_arr_date)
  TextView tvArrDate;
  @BindView(R.id.tv_boarding_time)
  TextView tvBoardingTime;
  @BindView(R.id.cv_item_flight)
  androidx.cardview.widget.CardView cvItemFlight;
  @BindView(R.id.tv_gate)
  TextView tvGate;
  @BindView(R.id.tv_flight_status)
  TextView tvFlightStatus;
  @BindView(R.id.iv_company_logo)
  ImageView ivCompanyLogo;

  public FlightListViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  public TextView getTvFlightNumber() {
    return tvFlightNumber;
  }

  public TextView getTvDepPort() {
    return tvDepPort;
  }

  public TextView getTvArrPort() {
    return tvArrPort;
  }

  public TextView getTvDepDate() {
    return tvDepDate;
  }

  public TextView getTvArrDate() {
    return tvArrDate;
  }

  public TextView getTvBoardingTime() {
    return tvBoardingTime;
  }

  public CardView getCvItemFlight() {
    return cvItemFlight;
  }

  public TextView getTvFlightStatus() {
    return tvFlightStatus;
  }

  public TextView getTvGate() {
    return tvGate;
  }

  public ImageView getIvCompanyLogo() {
    return ivCompanyLogo;
  }

  public void setCvItemFlight(CardView cvItemFlight) {
    this.cvItemFlight = cvItemFlight;
  }
}
