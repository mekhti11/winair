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
import com.hititcs.dcs.view.BaseListAdapter;
import com.hititcs.dcs.view.ItemViewHolder;
import com.hititcs.dcs.view.baggagetracking.domain.model.ScannedTag;
import org.jetbrains.annotations.NotNull;

public class LastThreeBagAdapter
    extends BaseListAdapter<ScannedTag, LastThreeBagAdapter.ScannedTagViewHolder> {

  public LastThreeBagAdapter() {
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
    holder.fillData(scannedTag);
  }

  public class ScannedTagViewHolder extends ItemViewHolder {

    @BindView(R.id.text1)
    TextView textView;

    public ScannedTagViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    private void fillData(ScannedTag scannedTag) {
      textView.setText(scannedTag.getTagNo());
      if (scannedTag.getSuccess()) {
        textView.setTextColor(
            ContextCompat.getColor(itemView.getContext(), R.color.barcode_success));
      } else {
        textView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.barcode_fail));
      }
    }
  }
}
