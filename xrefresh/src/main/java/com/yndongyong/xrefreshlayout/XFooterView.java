package com.yndongyong.xrefreshlayout;

import android.view.View;

/**
 * Created by ad15 on 2017/9/4.
 */

interface XFooterView {

    /**
     * 得到内容view
     * @return
     */
    View getView();

    /**
     * 下拉过程中触发
     * @param offset  currentOffset - InitOffset,当前位置距离与初始位置的距离
     * @param total  refreshOffset- InitOffset ,刷新位置与 初始位置的距离
     * @param overPull currentOffset - freshOffset ,  当前位置与刷新位置的距离
     */
//    void onPull(int offset ,int total,int overPull);

    /**
     * 下拉过程中触发
     * @param offset  sipper 偏移量
     * @param sipperCurrentOffset  sipper 当前的位置
     * @param sipperInitOffset  sipper 初始位置
     * @param sipperRefreshOffset  sipper 触发刷新的位置
     */
    void onPull(int offset, int sipperCurrentOffset, int sipperInitOffset, int sipperRefreshOffset);

    /**
     * 通知当前的状态
     * @param state
     */
    void changeStatus(int state);


}
