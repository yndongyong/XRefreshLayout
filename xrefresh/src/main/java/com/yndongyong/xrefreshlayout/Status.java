package com.yndongyong.xrefreshlayout;

/**
 * Created by ad15 on 2017/10/10.
 */

public interface Status {
    int IDLE = 0;//当前是空闲
    int OVER_REFRESH_OFFSET = 1;//达到刷新点的状态
    int REFRESH = 2;//刷新状态
    int SCROLL_TO_INIT = 3;//从小于或者等于触发刷新的位置 回滚到初始位置 的状态
}
