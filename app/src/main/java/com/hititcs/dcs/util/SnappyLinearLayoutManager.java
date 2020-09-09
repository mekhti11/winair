package com.hititcs.dcs.util;

import android.content.Context;
import android.graphics.PointF;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class SnappyLinearLayoutManager extends LinearLayoutManager {

  public SnappyLinearLayoutManager(Context context) {
    super(context);
  }

  @Override
  public void smoothScrollToPosition(RecyclerView recyclerView,
                                     RecyclerView.State state,
                                     int position) {
    RecyclerView.SmoothScroller smoothScroller =
        new LinearSmoothScroller(recyclerView.getContext()) {
          @Override
          public PointF computeScrollVectorForPosition(int targetPosition) {
            return SnappyLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
          }

          @Override
          protected int getVerticalSnapPreference() {
            return SNAP_TO_START; // override base class behavior
          }
        };
    smoothScroller.setTargetPosition(position);
    startSmoothScroll(smoothScroller);
  }

  @Override
  public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
    try {
      super.onLayoutChildren(recycler, state);
    } catch (Exception e) {
      Timber.e(e);
    }
  }

}
