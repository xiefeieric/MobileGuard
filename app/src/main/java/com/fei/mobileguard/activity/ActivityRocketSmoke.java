package com.fei.mobileguard.activity;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.fei.mobileguard.R;

public class ActivityRocketSmoke extends Activity {

    private ImageView ivSmokeTop,ivSmokeBottom;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_smoke);

        AlphaAnimation anim = new AlphaAnimation(0,1f);
        anim.setDuration(1000);
        anim.setFillAfter(true);
        ivSmokeTop = (ImageView) findViewById(R.id.ivSmokeTop);
        ivSmokeTop.startAnimation(anim);
        anim.setDuration(500);
        anim.setFillAfter(true);
        ivSmokeBottom = (ImageView) findViewById(R.id.ivSmokeBottom);
        ivSmokeBottom.startAnimation(anim);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

}
