package com.hititcs.dcs.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import com.hititcs.dcs.model.EditTextConsumer;
import com.hititcs.dcs.model.EditTextFormatter;
import com.hititcs.dcs.util.StringUtils;
import java.lang.ref.WeakReference;
import java.util.function.Consumer;

public class GeneralTextWatcher implements TextWatcher {

  private final WeakReference<TextView> mWeakReference;
  private final Consumer<String> mConsumer;
  private final EditTextConsumer<String> editTextConsumer;
  private final EditTextFormatter editTextFormatter;
  private boolean deleting;

  public GeneralTextWatcher(TextView editText, Consumer<String> consumer) {
    this(editText, consumer, null);
  }

  public GeneralTextWatcher(TextView editText, Consumer<String> consumer,
      EditTextConsumer<String> editTextConsumer) {
    this(editText, consumer, editTextConsumer, null);
  }

  public GeneralTextWatcher(TextView editText, Consumer<String> consumer,
      EditTextConsumer<String> editTextConsumer, EditTextFormatter formatter) {
    if (editText == null) {
      throw new IllegalStateException("EditText is not null");
    }
    mWeakReference = new WeakReference<>(editText);
    mConsumer = consumer;
    this.editTextConsumer = editTextConsumer;
    this.editTextFormatter = formatter;
  }

  public boolean isDeleting() {
    return deleting;
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    if (mWeakReference.get() != null) {
      mWeakReference.get().setError(null);
    }
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    deleting = before > count;
  }

  @Override
  public void afterTextChanged(Editable s) {
    if (mWeakReference.get() == null) {
      return;
    }
    if (editTextConsumer != null) {
      if (editTextConsumer.validate(s.toString())) {
        mConsumer.accept(s.toString());
      }
    } else {
      if (StringUtils.isBlank(s)) {
        mWeakReference.get().removeTextChangedListener(this);
        s.clear();
        mWeakReference.get().setText("");
        mWeakReference.get().addTextChangedListener(this);
      } else {
        if (editTextFormatter != null && !isDeleting()) {
          mWeakReference.get().removeTextChangedListener(this);
          String format = editTextFormatter.format(s.toString());
          mWeakReference.get().setText(format);
          int length = format.length();
          if (length > mWeakReference.get().getText().length()) {
            length = mWeakReference.get().getText().length();
          }
          ((EditText) mWeakReference.get()).setSelection(length);
          mWeakReference.get().addTextChangedListener(this);
        }
      }
      if (mConsumer != null) {
        mConsumer.accept(s.toString());
      }
    }
  }
}

