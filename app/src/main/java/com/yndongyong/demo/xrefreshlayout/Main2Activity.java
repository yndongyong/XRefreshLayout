package com.yndongyong.demo.xrefreshlayout;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_up;
    private Button btn_dwon;
    private TextView tv_tips;
    private ImageView ic_mute;
    private ImageView iv_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView() {
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_dwon = (Button) findViewById(R.id.btn_dwon);
        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);

        btn_up.setOnClickListener(this);
        btn_dwon.setOnClickListener(this);
        ic_mute = (StatusImageView) findViewById(R.id.ic_mute);
       /* ic_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_up:
//                ViewCompat.offsetTopAndBottom(tv_tips,-30);
                final ValueAnimator anim = ValueAnimator.ofFloat(0f, 180f).setDuration(200);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        iv_arrow.setRotation((Float) animation.getAnimatedValue());
                        iv_arrow.requestLayout();
                    }
                });
                anim.start();
                break;
            case R.id.btn_dwon:
//                ViewCompat.offsetTopAndBottom(tv_tips,30);
                final ValueAnimator anim1 = ValueAnimator.ofFloat(180f,0f).setDuration(200);
                anim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        iv_arrow.setRotation((Float) animation.getAnimatedValue());
                        iv_arrow.requestLayout();
                    }
                });
                anim1.start();
                break;
        }
    }
}
