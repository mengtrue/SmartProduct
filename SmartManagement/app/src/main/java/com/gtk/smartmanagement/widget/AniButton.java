package com.gtk.smartmanagement.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.blankj.utilcode.util.LogUtils;
import com.gtk.smartmanagement.R;

public class AniButton extends AppCompatButton {
    private int width, height;

    private GradientDrawable backDrawable;
    private ValueAnimator arcValueAnimator;
    private Paint paint;

    private boolean isMorphing;
    private int startAngle;
    private String buttonText;

    public AniButton(Context context) {
        super(context);
        init();
    }

    public AniButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AniButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        isMorphing = false;
        backDrawable = new GradientDrawable();
        backDrawable.setCornerRadius(120);
        backDrawable.setColor(getResources().getColor(R.color.cutePink));
        setBackground(backDrawable);

        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.white));
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(2);

        buttonText = getText().toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY)
            width = widthSize;
        if (heightMode == MeasureSpec.EXACTLY)
            height = heightSize;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if (isMorphing) {
            final RectF rectF = new RectF(getWidth() * 5/11,
                    getHeight()/7, getWidth() * 6/11, getHeight() * 6/7);
            canvas.drawArc(rectF, startAngle, 270, false, paint);
        }
    }

    public void startAnim() {
        setClickable(false);
        isMorphing = true;
        setText("");

        ValueAnimator valueAnimator = ValueAnimator.ofInt(width, height);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                int leftOffset = (width - value) / 2;
                int rightOffset = width - leftOffset;
                LogUtils.dTag("BUTTON", "onAnimationUpdate," +
                        "leftOffset = " + leftOffset + ", rightOffset = " + rightOffset);
                backDrawable.setBounds(leftOffset, 0, rightOffset, height);
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(backDrawable,
                "cornerRadius", 120, height / 2);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(300);
        animatorSet.playTogether(valueAnimator, objectAnimator);
        animatorSet.start();

        showArc();
    }

    private void showArc() {
        arcValueAnimator = ValueAnimator.ofInt(0, 1080);
        arcValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startAngle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        arcValueAnimator.setInterpolator(new LinearInterpolator());
        arcValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        arcValueAnimator.setDuration(3000);
        arcValueAnimator.start();
    }

    public void finishAnimator() {
        isMorphing = false;
        arcValueAnimator.cancel();
        setVisibility(GONE);
    }

    public void cancelAnimator() {
        setVisibility(VISIBLE);
        backDrawable.setBounds(0, 0, width, height);
        backDrawable.setCornerRadius(24);
        setBackground(backDrawable);
        setText(buttonText);
        isMorphing = false;
        setClickable(true);
    }
}
