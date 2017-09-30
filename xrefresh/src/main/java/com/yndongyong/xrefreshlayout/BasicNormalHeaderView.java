package com.yndongyong.xrefreshlayout;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ad15 on 2017/9/4.
 */

public class BasicNormalHeaderView implements XHeaderView {

    int currentStatus  = XRefreshLayout.States.IDLE;

    private ViewGroup parent;
    private View rootView;

    private int height;
    private ImageView iv_arrow;
    private ImageView iv_progress;
    private ValueAnimator valueAnimator;

    private boolean flag = false;

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

    @Override
    public void onPull(int targetCurrentOffset, int targetInitOffset, int targetRefreshOffset) {
        if (targetCurrentOffset >= targetRefreshOffset) {
            if (!flag) {
                flag = !flag;
                roteToUp();
            }

            //超过刷新位置
        } else {
           /* if (targetCurrentOffset >= targetRefreshOffset / 2) {
                //超过刷新位置的一半
                if (!flag) {
                    flag = !flag;
                    roteToDown();
                }
            }*/
        }
    }

    private void roteToUp() {
        final ValueAnimator anim = ValueAnimator.ofFloat(0f, 180f).setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv_arrow.setRotation((Float) animation.getAnimatedValue());
                iv_arrow.requestLayout();
            }
        });
        anim.start();
    }

    private void roteToDown() {
        final ValueAnimator anim1 = ValueAnimator.ofFloat(180f,0f).setDuration(200);
        anim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv_arrow.setRotation((Float) animation.getAnimatedValue());
                iv_arrow.requestLayout();
            }
        });
        anim1.start();
    }


    public void changeReleaseToRefresh() {
//        tv_tips.setText("释放去刷新");
    }

    public void changeToRefresh() {
        /*final RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(3000);//设置动画持续时间
        animation.setRepeatCount(-1);//设置重复次数
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        //
        iv_arrow.setVisibility(View.GONE);
        iv_progress.setVisibility(View.VISIBLE);
        iv_progress.setAnimation(animation);
        animation.startNow();*/
    }

    @Override
    public void changeToIdle() {
        flag = !flag;
        /*final RotateAnimation animation = new RotateAnimation(-180f, 0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);//设置动画持续时间
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        iv_arrow.setVisibility(View.VISIBLE);
        iv_progress.setVisibility(View.GONE);
        iv_arrow.setAnimation(animation);
        animation.startNow();*/
    }

    @Override
    public void changeStatus(int state) {
        if (this.currentStatus != state) {
            this.currentStatus = state;
        }
    }
}
