package com.yndongyong.xrefreshlayout;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ad15 on 2017/9/4.
 */

public class BasicNormalHeaderView implements XHeaderView {

    private ViewGroup parent;
    private View rootView;

    private int height;
    private ImageView iv_arrow;
    private ImageView iv_progress;

    public BasicNormalHeaderView(ViewGroup _parent) {
        this.parent = _parent;
        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.basic_normal_header_view_layout, parent, false);
        iv_arrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
        iv_progress = (ImageView) rootView.findViewById(R.id.iv_progress);
    }


    @Override
    public View getView() {
        return rootView;
    }

    /**
     * @param overscrollTop 拖动的距离
     * @param viewHeight    header 的高度
     * @param offsetTop     spinner 距离 屏幕顶部的距离 [-height,0]
     */
    @Override
    public void moveSpinner(int overscrollTop, int viewHeight, int offsetTop) {

    }


    @Override
    public void finishSpinner(float offsetTop) {

    }

    public void changeReleaseToRefresh() {
//        tv_tips.setText("释放去刷新");
    }

    public void changeToRefresh() {
    }
}
