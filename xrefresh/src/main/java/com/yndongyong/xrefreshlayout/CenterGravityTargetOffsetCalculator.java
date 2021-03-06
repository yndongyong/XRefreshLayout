package com.yndongyong.xrefreshlayout;

/**
 * 计算sipper 的 目标位置 的y值
 * 居中效果
 * 当targetCurrentOffset < sipperViewHeight 时，跟随手指滑动
 * 当targetCurrentOffset < sipperViewHeight时，固定在[targetInit,targetCurrent]
 * Created by ad15 on 2017/10/10.
 */

public class CenterGravityTargetOffsetCalculator implements SipperTargetOffsetCalculator {

    @Override
    public int calculateOffset(int dy, int sipperCurrentOffset, int sipperInitOffset, int sipperEndOffset, int sipperViewHeight,
                               int targetCurrentOffset, int targetInitOffset, int targetRefreshOffset) {
        int targetY = 0;
        if (targetCurrentOffset >= targetRefreshOffset) {
            //超过刷新线
            targetY = targetCurrentOffset/2 - sipperViewHeight/2;
        } else if (targetCurrentOffset <= targetInitOffset) {
            targetY = sipperInitOffset;
            //达到初始线
        } else {
            //中间过程
            targetY = targetCurrentOffset - sipperViewHeight;
        }

        return targetY;
    }
}
