package com.yndongyong.xrefreshlayout;

/**
 * 计算sipper 的 目标位置 的y值
 * 将sipper 的位置限定在 [sipperInit,sipperEndOffset]  固定在头部的效果
 * Created by ad15 on 2017/10/10.
 */

public class DefaultTargetOffsetCalculator implements SipperTargetOffsetCalculator {

    @Override
    public int calculateOffset(int dy, int sipperCurrentOffset, int sipperInitOffset, int sipperEndOffset, int sipperViewHeight,
                               int targetCurrentOffset, int targetInitOffset, int targetRefreshOffset) {
        int targetY = 0;
        if (targetCurrentOffset >= targetRefreshOffset) {
            //超过刷新线
            targetY = targetInitOffset;
        } else if (targetCurrentOffset <= targetInitOffset) {
            targetY = sipperInitOffset;
            //达到初始线
        } else {
            //中间过程
            targetY = dy + sipperCurrentOffset;
        }
        return targetY;
    }
}
