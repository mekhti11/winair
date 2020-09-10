package com.hititcs.dcs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.content.ContextCompat;
import com.hititcs.dcs.R;

public class AutoCompleteDropDown extends AppCompatAutoCompleteTextView {

  boolean showDropDownMenu;
  int dropDownMenuColor;
  private boolean isPopup;
  private int mPosition = ListView.INVALID_POSITION;

  public AutoCompleteDropDown(Context context) {
    this(context, null);
  }

  public AutoCompleteDropDown(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public AutoCompleteDropDown(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray attributes = context.obtainStyledAttributes(attrs,
        R.styleable.AutoCompleteDropDown, defStyleAttr, 0);
    showDropDownMenu =
        attributes.getBoolean(R.styleable.AutoCompleteDropDown_showDropDownIcon, true);
    dropDownMenuColor = attributes.getColor(R.styleable.AutoCompleteDropDown_dropDownIconColor,
        ContextCompat.getColor(context, R.color.white));
    setDrawable();
    attributes.recycle();
    setClickListener();
  }

  void setClickListener() {
    setDropDownBackgroundDrawable(
        ContextCompat.getDrawable(getContext(), R.drawable.bg_login_popup));
    setOnItemClickListener((parent, view, position, id) -> mPosition = position);
  }

  @Override
  public boolean enoughToFilter() {
    return true;
  }


  @Override
  protected void onFocusChanged(boolean focused, int direction,
      Rect previouslyFocusedRect) {
    super.onFocusChanged(focused, direction, previouslyFocusedRect);
    if (focused) {
      InputMethodManager imm =
          (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(getWindowToken(), 0);
      setKeyListener(null);
      dismissDropDown();
    } else {
      isPopup = false;
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    switch (event.getAction()) {
      case MotionEvent.ACTION_UP: {
        if (isPopup) {
          dismissDropDown();
        } else {
          requestFocus();
          showDropDown();
        }
        break;
      }
      default:
    }

    return super.onTouchEvent(event);
  }

  @Override
  public void showDropDown() {
    super.showDropDown();
    isPopup = true;
  }

  @Override
  public void dismissDropDown() {
    super.dismissDropDown();
    isPopup = false;
  }

  public boolean isShowing() {
    return isPopup;
  }

  private void setDrawable() {
    if (!showDropDownMenu) {
      return;
    }
    Drawable dropdownIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_down_arrow);
    if (dropdownIcon != null) {
      dropdownIcon.mutate().setTint(dropDownMenuColor);
    }
    setCompoundDrawablesWithIntrinsicBounds(null, null, dropdownIcon, null);
  }

  public int getPosition() {
    return mPosition;
  }

  public void setTextAdapter(String text) {
    mPosition=ListView.INVALID_POSITION;
    setText(text,false);
  }
}