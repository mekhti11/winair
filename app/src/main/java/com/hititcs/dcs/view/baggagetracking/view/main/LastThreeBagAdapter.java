package com.hititcs.dcs.view.baggagetracking.view.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hititcs.dcs.R;
import com.hititcs.dcs.util.StringUtils;
import com.hititcs.dcs.view.BaseListAdapter;
import com.hititcs.dcs.view.ItemViewHolder;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScannedTag;
import org.jetbrains.annotations.NotNull;

public class LastThreeBagAdapter
    extends BaseListAdapter<ScannedTag, LastThreeBagAdapter.ScannedTagViewHolder> {

  private final int SHOW_NUMBER_OF_BAGTAG_LIMIT = 3;

  private boolean isFromMain;

  public LastThreeBagAdapter(Boolean isFromMain) {
    this.isFromMain = isFromMain;
  }

  @NonNull
  @Override
  public ScannedTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ScannedTagViewHolder(
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_last_three_bagtag, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull @NotNull ScannedTagViewHolder holder, int position) {
    ScannedTag scannedTag = getItem(position);
    holder.fillData(scannedTag, position);
  }

  @Override
  public int getItemCount() {
    if (getItemList().size() > SHOW_NUMBER_OF_BAGTAG_LIMIT) {
      return SHOW_NUMBER_OF_BAGTAG_LIMIT;
    } else {
      return getItemList().size();
    }
  }

  public class ScannedTagViewHolder extends ItemViewHolder {

    @BindView(R.id.text1)
    TextView textView;

    public ScannedTagViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    private void fillData(ScannedTag scannedTag, int position) {
      if (scannedTag.getSuccess()) {
        textView.setTextColor(
            ContextCompat.getColor(itemView.getContext(), R.color.barcode_success));
        if (isFromMain) {
          textView.setText(String.format("%s - %s", scannedTag.getTagNo(),
              itemView.getContext().getString(R.string.baggage_track_main_success)));
        } else {
          textView.setText(scannedTag.getTagNo());
        }
      } else {
        textView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.barcode_fail));
        if (isFromMain) {
          textView.setText(String.format("%s - %s", scannedTag.getTagNo(), !StringUtils.isEmpty(
              scannedTag.getErrorMessage()) ? scannedTag.getErrorMessage() : ""));
        } else {
          textView.setText(scannedTag.getTagNo());
        }
      }
    }
  }
}
