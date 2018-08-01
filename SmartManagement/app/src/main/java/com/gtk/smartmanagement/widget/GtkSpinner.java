package com.gtk.smartmanagement.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blankj.utilcode.util.LogUtils;

import java.util.Calendar;

public class GtkSpinner extends AppCompatAutoCompleteTextView implements AdapterView.OnItemClickListener{

    private static final String TAG = GtkSpinner.class.getSimpleName();
    private static final int MAX_CLICK_DURATION = 200;
    private long startClickTime;
    private boolean isPopup;
    private int mPosition = ListView.INVALID_POSITION;

    public GtkSpinner(Context context) {
        super(context);
        setOnItemClickListener(this);
    }

    public GtkSpinner(Context context, AttributeSet attr) {
        super(context, attr);
        setOnItemClickListener(this);
    }

    public GtkSpinner(Context context, AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);
        setOnItemClickListener(this);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect preFocusedRect) {
        super.onFocusChanged(focused, direction, preFocusedRect);

        if (focused) {
            performFiltering("", 0);
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
            setKeyListener(null);
            dismissDropDown();
        } else {
            isPopup = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled())
            return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startClickTime = Calendar.getInstance().getTimeInMillis();
                break;
            case MotionEvent.ACTION_UP:
                long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                if (clickDuration < MAX_CLICK_DURATION) {
                    if (isPopup) {
                        dismissDropDown();
                        isPopup = false;
                    } else {
                        requestFocus();
                        showDropDown();
                        isPopup = true;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        LogUtils.dTag(TAG, "onItemClick position = " + position);
        mPosition = position;
        isPopup = false;
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top,
                                                        Drawable right, Drawable bottom) {
        Drawable dropdownIcon = ContextCompat.getDrawable(getContext(),
                android.R.drawable.arrow_down_float);
        if (dropdownIcon != null) {
            right = dropdownIcon;
            right.mutate().setAlpha(128);
        }
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }

    public int getPosition() {
        return mPosition;
    }

}
