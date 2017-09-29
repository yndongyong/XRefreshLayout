package com.yndongyong.demo.xrefreshlayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by ad15 on 2017/9/20.
 */

public class StatusImageView extends AppCompatImageView {

    private final static float[] MATRIX = new float[]{
            0.8f, 0, 0, 0, 0,
            0, 0.8f, 0, 0, 0,
            0, 0, 0.8f, 0, 0,
            0, 0, 0, 1, 0};

    public StatusImageView(Context context) {
        this(context, null);
    }

    public StatusImageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public StatusImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_image1);
//        stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, drawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, getResources().getDrawable(R.mipmap.ic_launcher));
//        stateListDrawable.addState(new int[]{android.R.attr.state_pressed},drawable);
        stateListDrawable.addState(new int[]{}, tintDrawable(drawable));
        setBackground(stateListDrawable);

    }

    private Drawable tintDrawable(Drawable orignalDrawable) {
        Drawable drawable = DrawableCompat.wrap(orignalDrawable).mutate();
//        ColorFilter colorFilter = new ColorMatrixColorFilter(MATRIX);
//        drawable.setColorFilter(colorFilter);
        drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_OVER);

        return drawable;
    }
}
