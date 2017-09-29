package com.yndongyong.demo.xrefreshlayout;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_up;
    private Button btn_dwon;
    private TextView tv_tips;
    private ImageView ic_mute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView() {
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_dwon = (Button) findViewById(R.id.btn_dwon);

        btn_up.setOnClickListener(this);
        btn_dwon.setOnClickListener(this);
        tv_tips = (TextView) findViewById(R.id.tv_tips);
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
                ViewCompat.offsetTopAndBottom(tv_tips,-30);
                break;
            case R.id.btn_dwon:
                ViewCompat.offsetTopAndBottom(tv_tips,30);
                break;
        }
    }
}
