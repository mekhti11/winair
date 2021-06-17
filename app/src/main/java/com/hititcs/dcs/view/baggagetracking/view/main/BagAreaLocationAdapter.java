package com.hititcs.dcs.view.baggagetracking.view.main;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import com.hititcs.dcs.R;
import com.hititcs.dcs.view.baggagetracking.domain.model.TrackBaggageLocationDto;
import java.util.List;

public class BagAreaLocationAdapter extends ArrayAdapter<TrackBaggageLocationDto> {

  private int selectedItemPosition = -1;
  private Typeface extraLight;
  private Typeface bold;

  BagAreaLocationAdapter(@NonNull Context context, int resource,
      @NonNull List<TrackBaggageLocationDto> objects) {
    super(context, resource, objects);
    extraLight = ResourcesCompat.getFont(context, R.font.poppins_extra_light);
    bold = ResourcesCompat.getFont(context, R.font.poppins_bold);
  }

  void setSelectedItemPosition(int selectedItemPosition) {
    this.selectedItemPosition = selectedItemPosition;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    final View view = super.getView(position, convertView, parent);
    CheckedTextView textView = view.findViewById(android.R.id.text1);
    if (selectedItemPosition > -1 && position == selectedItemPosition) {
      textView.setChecked(true);
      textView.setTypeface(bold);
      textView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_secondary));
    } else {
      textView.setChecked(false);
      textView.setTypeface(extraLight);
      textView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_primary));
    }
    return view;
  }
}
