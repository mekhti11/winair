package com.hititcs.dcs.view;

import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ItemViewHolder extends RecyclerView.ViewHolder {

  public final CardView mCardView;

  public ItemViewHolder(View itemView) {
    super(itemView);
    mCardView = (CardView) itemView;
  }
}