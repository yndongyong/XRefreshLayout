package com.yndongyong.xrefreshlayout;

import android.view.View;

/**
 * Created by ad15 on 2017/9/4.
 */

interface XHeaderView {

    View getView();

    /**
     * 滑动中
     * @param overscrollTop 拖动的距离
     * @param viewHeight  header 的高度
     */
    void moveSpinner(int overscrollTop, int viewHeight ,int offsetTop);

    /**
     * 手机释放
     * @param offsetTop
     */
    void finishSpinner(float offsetTop);
}
