package com.yndongyong.xrefreshlayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    /**
     *
     * @param overscrollTop 拖动的距离
     * @param viewHeight  header 的高度
     * @param offsetTop  spinner 距离 屏幕顶部的距离 [-height,0]
     */
    @Override
    public void moveSpinner(int overscrollTop, int viewHeight, int offsetTop) {
        offsetTop = Math.abs(offsetTop);
        if (offsetTop / viewHeight < 1/3 ) {
            tv_tips.setText("释放去刷新");
        } else if (offsetTop / viewHeight > 1 / 3) {
            tv_tips.setText("下拉刷新");
        } else {
            tv_tips.setText("正在刷新");
        }
    }


    @Override
    public void finishSpinner(float offsetTop) {

    }
}
