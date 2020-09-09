package com.hititcs.dcs.view.barcode;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.hititcs.dcs.R;
import com.hititcs.dcs.view.BaseActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import static com.hititcs.dcs.view.flight.detail.FlightDetailFragment.BOARDED_COUNT_START;
import static com.hititcs.dcs.view.flight.detail.FlightDetailFragment.FLIGHT_ID;

public class ScanBarcodeActivity extends BaseActivity<ScanBarcodeActivity> {

    String flightId;
    String boardedCountStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        flightId = getIntent().getStringExtra(FLIGHT_ID);
        boardedCountStart = getIntent().getStringExtra(BOARDED_COUNT_START);
        bindView();


        setUpFragment();
    }

    private void setUpFragment() {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.content_frame,
                ScanBarcodeFragment.newInstance(flightId, boardedCountStart),
                ScanBarcodeFragment.class.getSimpleName())
            .commit();
    }
    @Override
    protected ScanBarcodeActivity getActivity() {
        return this;
    }
}
