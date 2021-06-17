package com.hititcs.dcs.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public abstract class BaseListAdapter<T, V extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<V> {

  protected List<T> itemList;
  protected RecyclerView mRecyclerView;

  @Override
  public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    this.mRecyclerView = recyclerView;
  }

  @Override
  public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
    super.onDetachedFromRecyclerView(recyclerView);
    mRecyclerView = recyclerView;
  }

  @NonNull
  public T getItem(int position) {
    return itemList.get(position);
  }

  public List<T> getItemList() {
    return itemList;
  }

  public void setItemList(List<T> itemList) {
    this.itemList = itemList;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return itemList == null ? 0 : itemList.size();
  }

  public void addItem(T item) {
    if (itemList != null) {
      itemList.add(item);
      notifyItemInserted(itemList.size() - 1);
    }
  }

  public void deleteItem(int position) {
    itemList.remove(position);
    notifyItemRemoved(position);
  }
}

