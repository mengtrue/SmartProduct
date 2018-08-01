package com.gtk.smartmanagement.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.gtk.smartmanagement.R;

public class DrawableTextView extends AppCompatTextView {
    private Context mContext;

    private int drawableLeft, drawableRight, drawableTop, drawableBottom;
    private float drawableWidth, drawableHeight;

    public DrawableTextView(Context context) {
        this(context, null);
    }

    public DrawableTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.drawableText);
            try {
                drawableLeft = typedArray.getResourceId(R.styleable.drawableText_leftDrawable, 0);
                drawableRight = typedArray.getResourceId(R.styleable.drawableText_rightDrawable, 0);
                drawableTop = typedArray.getResourceId(R.styleable.drawableText_topDrawable, 0);
                drawableBottom = typedArray.getResourceId(R.styleable.drawableText_bottomDrawable, 0);

                drawableWidth = typedArray.getDimensionPixelSize(R.styleable.drawableText_drawableWidth, 30);
                drawableHeight = typedArray.getDimensionPixelSize(R.styleable.drawableText_drawableHeight, 30);
            } finally {
                if (typedArray != null)
                    typedArray.recycle();
            }
        }

        initView();
    }

    private void initView() {
        Drawable leftDrawable = null;
        if (drawableLeft != 0) {
            leftDrawable = ContextCompat.getDrawable(mContext, drawableLeft);
            leftDrawable.setBounds(0, 0, (int)drawableWidth, (int)drawableHeight);
        }

        Drawable rightDrawble = null;
        if (drawableRight != 0) {
            rightDrawble = ContextCompat.getDrawable(mContext, drawableRight);
            rightDrawble.setBounds(0, 0, (int)drawableWidth, (int)drawableHeight);
        }

        Drawable topDrawble = null;
        if (drawableTop != 0) {
            topDrawble = ContextCompat.getDrawable(mContext, drawableTop);
            topDrawble.setBounds(0, 0, (int)drawableWidth, (int)drawableHeight);
        }

        Drawable bottomDrawable = null;
        if (drawableBottom != 0) {
            bottomDrawable = ContextCompat.getDrawable(mContext, drawableBottom);
            bottomDrawable.setBounds(0, 0, (int)drawableWidth, (int)drawableHeight);
        }

        this.setCompoundDrawables(leftDrawable, topDrawble, rightDrawble, bottomDrawable);
    }
}
