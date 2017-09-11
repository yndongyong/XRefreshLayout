package com.yndongyong.xrefreshlayout;

import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.android.internal.app.IntentForwarderActivity.TAG;

/**
 * Created by ad15 on 2017/9/4.
 */

public class BasicNormalHeaderView implements XHeaderView {

    private ViewGroup parent;
    private TextView tv_tips;
    private View rootView;

    private int height;

    public BasicNormalHeaderView(ViewGroup _parent) {
        this.parent = _parent;
        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.basic_normal_header_view_layout, parent, false);
        tv_tips = (TextView) rootView.findViewById(R.id.tv_tips);
    }


    @Override
    public View getView() {
        return rootView;
    }

    @Override
    public void moveSpinner(int overscrollTop, int viewHeight) {

    }


    @Override
    public void finishSpinner(float offsetTop) {

    }
}
