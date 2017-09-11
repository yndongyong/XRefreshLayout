package com.yndongyong.xrefreshlayout;

import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;

/**
 * Created by ad15 on 2017/9/4.
 */

public interface RefreshLayout {

    int STATUS_IDLE = 1; //初始 空闲状态
    int STATUS_PULL_DOWN = 2;//下拉状态
    int STATUS_REFRESHING = 3; //正在刷新
    int STATUS_RESETING = 4; // 复位过程中
}
