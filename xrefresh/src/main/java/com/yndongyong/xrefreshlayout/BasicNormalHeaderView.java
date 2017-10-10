package com.yndongyong.xrefreshlayout;

import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Created by ad15 on 2017/9/4.
 */

public class BasicNormalHeaderView implements XHeaderView {

    int currentStatus = Status.IDLE;

    private ViewGroup parent;
    private View rootView;

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
     *
     * @param offset  sipper 偏移量
     * @param sipperCurrentOffset  sipper 当前的位置
     * @param sipperInitOffset  sipper 初始位置
     * @param sipperRefreshOffset  sipper 触发刷新的位置
     */
    @Override
    public void onPull(int offset, int sipperCurrentOffset, int sipperInitOffset, int sipperRefreshOffset) {

    }

    private void roteToUp() {
        final ValueAnimator anim = ValueAnimator.ofFloat(0f, 180f).setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv_arrow.setRotation((Float) animation.getAnimatedValue());
            }
        });
        anim.start();
    }

    /**
     * 恢复到初始状态
     */
    private void reset() {
        iv_progress.clearAnimation();
        iv_arrow.clearAnimation();
        iv_arrow.setVisibility(View.VISIBLE);
        iv_arrow.setRotation(0f);
        iv_progress.setVisibility(View.INVISIBLE);
        currentStatus = Status.IDLE;
    }

    /**
     * 要保证动画时间和headerview隐藏的时间一致 采用系统的 mediumAnimTime
     */
    private void changeToInit() {
        final ValueAnimator anim1 = ValueAnimator.ofFloat(180f, 0f).setDuration(rootView.getResources().getInteger(
                android.R.integer.config_mediumAnimTime));
        anim1.start();
    }


    private void changeReleaseToRefresh() {
        roteToUp();
    }

    private void changeToRefresh() {
        final RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);//设置动画持续时间
        animation.setRepeatCount(-1);//设置重复次数
        animation.setInterpolator(new LinearInterpolator());
//        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态

        //
        iv_arrow.setVisibility(View.INVISIBLE);
        iv_progress.setVisibility(View.VISIBLE);
        iv_progress.startAnimation(animation);
    }

    @Override
    public void changeStatus(int state) {
        if (this.currentStatus != state) {
            switch (state) {
                case Status.IDLE:
                    reset();
                    break;
                case Status.OVER_REFRESH_OFFSET:
                    changeReleaseToRefresh();
                    break;
                case Status.REFRESH:
                    changeToRefresh();
                    break;
                case Status.SCROLL_TO_INIT:
                    changeToInit();
                    break;
                default:

                    break;
            }
            this.currentStatus = state;
        }
    }
}
