package com.fei.mobileguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.fei.mobileguard.R;

/**
 * Created by Fei on 19/09/15.
 */
public abstract class ActivityBaseSetup extends Activity {

    public SharedPreferences mSharedPreferences;
    public GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getRawX()-e2.getRawX()>200) {
                    showNextPage();
                } else if (e2.getRawX()-e1.getRawX()>200){
                    showPreviousPage();
                }
                return true;
            }
        });
    }

    abstract void showNextPage();
    abstract void showPreviousPage();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

}
