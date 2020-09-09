package com.hititcs.dcs.view.flight;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hititcs.dcs.DcsApplication;
import com.hititcs.dcs.R;
import com.hititcs.dcs.domain.model.FlightSummary;
import com.hititcs.dcs.util.AppUtils;
import com.hititcs.dcs.util.DateTimeUtils;
import com.hititcs.dcs.view.BaseActivity;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class FlightListAdapter extends RecyclerView.Adapter<FlightListViewHolder> {

  private List<FlightSummary> flightSummaryList = new ArrayList<>();
  private FlightOnClickListener flightOnClickListener;
  private Drawable companyLogo;

  public FlightListAdapter(FlightOnClickListener flightOnClickListener, Drawable companyLogo) {
    this.flightOnClickListener = flightOnClickListener;
    this.companyLogo = companyLogo;
  }

  public void update(List<FlightSummary> newFlightSummaryList) {
    flightSummaryList.clear();
    flightSummaryList.addAll(newFlightSummaryList);
    notifyDataSetChanged();
  }

  public void setFlightSummaryList(List<FlightSummary> flightSummaryList) {
    this.flightSummaryList = flightSummaryList;
  }

  @NonNull
  @Override
  public FlightListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new FlightListViewHolder(
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_flight_list_card, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull FlightListViewHolder holder, int position) {
    holder.getTvFlightNumber().setText(String
        .format("%s%s", flightSummaryList.get(position).getAirlineCode(),
            flightSummaryList.get(position).getFlightNumber()));
    holder.getTvDepDate().setText(flightSummaryList.get(position).getDepTime().substring(11, 16));
    holder.getTvArrDate().setText(flightSummaryList.get(position).getArrTime().substring(11, 16));
    holder.getTvDepPort().setText(flightSummaryList.get(position).getDepPort());
    holder.getTvArrPort().setText(flightSummaryList.get(position).getArrPort());
    holder.getTvGate().setText(flightSummaryList.get(position).getBoardingGate());
    if(companyLogo != null) {
      holder.getIvCompanyLogo().setImageDrawable(companyLogo);
      holder.getIvCompanyLogo().setVisibility(View.VISIBLE);
    }else{
      holder.getIvCompanyLogo().setVisibility(View.INVISIBLE);
    }
    holder.getTvFlightStatus().setText(String.format("%s: %s",
        holder.itemView.getContext().getString(R.string.item_flight_flight_status),
        flightSummaryList.get(position).getFlightStatus()));
    holder.getTvBoardingTime().setText(String.format("%s: %s",
        holder.itemView.getContext().getString(R.string.item_flight_boarding_time),
        DateTimeUtils
            .getTimeFromZonedDateTime(flightSummaryList.get(position).getBoardingTime())));
    holder.itemView.setOnClickListener(
        v -> flightOnClickListener.startFlightDetailActivity(flightSummaryList.get(position)));
    if (flightSummaryList.get(position).isVisible()) {
      holder.getCvItemFlight().setVisibility(View.VISIBLE);
    } else {
      holder.getCvItemFlight().setVisibility(View.GONE);
    }
  }

  @Override
  public int getItemCount() {
    return flightSummaryList != null ? flightSummaryList.size() : 0;
  }

  public interface FlightOnClickListener {

    void startFlightDetailActivity(FlightSummary flightSummary);
  }

}
