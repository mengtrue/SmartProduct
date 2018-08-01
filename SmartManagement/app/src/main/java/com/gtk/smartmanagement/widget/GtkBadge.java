package com.gtk.smartmanagement.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gtk.smartmanagement.R;

public class GtkBadge extends FrameLayout {
    private ImageView mIvBadge;

    public GtkBadge(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_badge, this, true);
        mIvBadge = (ImageView) findViewById(R.id.badge_img);
    }

    public void setBadge(boolean good) {
        mIvBadge.setImageResource(good? R.drawable.happy : R.drawable.crying);
    }
}
