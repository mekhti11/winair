package com.hititcs.dcs.view.flight;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import com.hititcs.dcs.R;
import com.hititcs.dcs.domain.model.FlightSummary;
import com.hititcs.dcs.util.AnimUtils;
import com.hititcs.dcs.util.AppUtils;
import com.hititcs.dcs.util.DateTimeUtils;
import com.hititcs.dcs.util.ImageUtils;
import com.hititcs.dcs.util.ParcelUtils;
import com.hititcs.dcs.view.BaseFragment;
import com.hititcs.dcs.view.Presenter;
import com.hititcs.dcs.view.flight.detail.FlightDetailActivity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.threeten.bp.LocalDate;

import static com.hititcs.dcs.view.flight.detail.FlightDetailActivity.FLIGHT_SUMMARY;

public class FlightListFragment extends BaseFragment<FlightListFragment> implements FlightListView,
    FlightListAdapter.FlightOnClickListener {

  @BindView(R.id.rcw_flights)
  RecyclerView recyclerViewFlights;
  @BindView(R.id.tw_current_date)
  TextView twDate;
  @BindView(R.id.tw_prev_date)
  TextView twDateBack;
  @BindView(R.id.tw_next_date)
  TextView twDateForward;
  @BindView(R.id.tw_total_number_of_flights)
  TextView twTotalNumberOfFlights;
  @BindView(R.id.tw_current_date_today)
  TextView twTodayLabel;
  @BindView(R.id.edt_search)
  TextView edtSearch;

  @Inject
  FlightListPresenter flightListPresenter;

  private FlightListAdapter flightListAdapter;
  private LocalDate selectedDate = LocalDate.now();
  private List<FlightSummary> flightList = new ArrayList<>();

  public static FlightListFragment newInstance() {
    Bundle args = new Bundle();
    FlightListFragment fragment = new FlightListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected FlightListFragment getFragment() {
    return this;
  }

  @Override
  protected Presenter getPresenter() {
    return flightListPresenter;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View fragmentView = inflater.inflate(R.layout.content_flight_list, container, false);
    bindView(fragmentView);
    return fragmentView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    edtSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        showFlightList(flightList);
      }
    });
  }


  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    selectedDate = LocalDate.now();
    twDateBack.setText(DateTimeUtils.formatDateEnglishName(selectedDate.minusDays(1)));
    twDate.setText(DateTimeUtils.formatDateEnglishName(selectedDate));
    twTodayLabel.setVisibility(View.VISIBLE);
    twDateForward.setText(DateTimeUtils.formatDateEnglishName(selectedDate.plusDays(1)));
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onResume() {
    super.onResume();
    flightListPresenter.setView(this);
    if (edtSearch != null) {
      edtSearch.setText("");
    }
    flightListPresenter.getFlightList(selectedDate);
  }

  @Override
  public void onPause() {
    flightListPresenter.destroyView();
    super.onPause();
  }

  @Override
  public void showFlightList(List<FlightSummary> flightSummaries) {
    initRecyclerView();
    flightList = flightSummaries;
    flightListAdapter.update(filterFlightsByFlightNumber(edtSearch.getText().toString()));
    recyclerViewFlights.scrollToPosition(0);
    recyclerViewFlights.setVisibility(View.VISIBLE);
    twTotalNumberOfFlights.setText(String
        .format("%d %s", flightListAdapter.getItemCount(), getString(R.string.home_flights_found)));
    twTotalNumberOfFlights.setVisibility(View.VISIBLE);
    AnimUtils.animateShowView(recyclerViewFlights);
  }

  private List<FlightSummary> filterFlightsByFlightNumber(String number) {
    number = number.trim();
    if (number.equals("")) {
      return flightList;
    }
    List<FlightSummary> filteredList = new ArrayList<>();
    for (int i = 0; i < flightList.size(); i++) {
      if (flightList.get(i).getFlightNumber().startsWith(number)) {
        filteredList.add(flightList.get(i));
      }
    }
    return filteredList;
  }

  public void hideFlightList() {
    twTotalNumberOfFlights.setVisibility(View.INVISIBLE);
    recyclerViewFlights.setVisibility(View.GONE);
  }

  @Override
  public void showLoading() {
    hideFlightList();
    super.showLoading();
  }

  @Override
  public void hideLoading() {
    super.hideLoading();
  }

  @Override
  public void startFlightDetailActivity(FlightSummary flightSummary) {
    Intent intent = new Intent(getActivity(), FlightDetailActivity.class);
    intent.putExtra(FLIGHT_SUMMARY, ParcelUtils.wrap(flightSummary));
    startActivity(intent);
  }

  private void initRecyclerView() {
    flightListAdapter = new FlightListAdapter(this,
        ImageUtils.loadImageUriAsDrawable(getContext(), AppUtils.getCompanyLogoUri(getContext())));
    recyclerViewFlights.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerViewFlights.setAdapter(flightListAdapter);
  }

  @OnClick(R.id.ln_prev)
  public void goPreviousDay() {
    selectedDate = selectedDate.minusDays(1);
    flightListPresenter.getFlightList(selectedDate);
    updateView();
  }

  @OnClick(R.id.ln_next)
  public void goNextDay() {
    selectedDate = selectedDate.plusDays(1);
    flightListPresenter.getFlightList(selectedDate);
    updateView();
  }

  public void updateView() {
    twDate.setText(DateTimeUtils.formatDateEnglishName(selectedDate));
    twDateBack.setText(DateTimeUtils.formatDateEnglishName(selectedDate.minusDays(1)));
    twDateForward.setText(DateTimeUtils.formatDateEnglishName(selectedDate.plusDays(1)));
    if (selectedDate.getDayOfYear() == LocalDate.now().getDayOfYear()) {
      twTodayLabel.setVisibility(View.VISIBLE);
    } else {
      twTodayLabel.setVisibility(View.INVISIBLE);
    }
    flightListAdapter.notifyDataSetChanged();
  }

  @Override
  public void showError(String message) {

  }

  @Override
  public void showError(int messageId) {

  }
}
