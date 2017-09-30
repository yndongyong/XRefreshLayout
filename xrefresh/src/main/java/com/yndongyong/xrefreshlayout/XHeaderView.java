package com.yndongyong.xrefreshlayout;

import android.view.View;

/**
 * Created by ad15 on 2017/9/4.
 */

interface XHeaderView {

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
     *  下拉过程中触发
     * @param targetCurrentOffset target当前的位置，
     * @param targetInitOffset target的初始位置
     * @param targetRefreshOffset  target 触发刷新的位置
     */
    void onPull(int targetCurrentOffset ,int targetInitOffset,int targetRefreshOffset);

    /**
     *  刷新
     */
    void changeToRefresh();

    /**
     * 空闲 /初始
     */
    void changeToIdle();

    /**
     * 通知当前的状态
     * @param state
     */
    void changeStatus(int state);


}
