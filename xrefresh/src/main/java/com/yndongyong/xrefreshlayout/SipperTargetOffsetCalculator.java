package com.yndongyong.xrefreshlayout;

/**
 *
 * 计算当前sipper 的目标位置 y方向上
 * Created by ad15 on 2017/10/10.
 */

public interface SipperTargetOffsetCalculator {

    /**
     *
     * @param dy  手指滑动的距离
     * @param sipperCurrentOffset
     * @param sipperInitOffset
     * @param sipperEndOffset
     * @param sipperViewHeight
     * @param targetCurrentOffset
     * @param targetInitOffset
     * @param targetRefreshOffset
     * @return
     */
    int calculateOffset(int dy,int sipperCurrentOffset,int sipperInitOffset, int sipperEndOffset, int sipperViewHeight,
                        int targetCurrentOffset, int targetInitOffset, int targetRefreshOffset);
}
