package com.fei.mobileguard.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.IBinder;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fei.mobileguard.R;
import com.fei.mobileguard.activity.ActivityRocketSmoke;

public class MyRocketService extends Service {

    private WindowManager mMWM;
    private WindowManager.LayoutParams mParams;
    private View mView;
    private ImageView ivRocket;
    private int mHeight;
    private int mWidth;
    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_ROCKET:
                    mMWM.updateViewLayout(mView,mParams);
                    break;
            }

        }
    };

    public static final int UPDATE_ROCKET = 1;


    public MyRocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initService();
    }

    private void initService() {
        mMWM = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        mHeight = mMWM.getDefaultDisplay().getHeight();
        mWidth = mMWM.getDefaultDisplay().getWidth();

        mParams = new WindowManager.LayoutParams();

        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        mView = View.inflate(this, R.layout.rocket, null);

        //add animationdrawable to the rocket
        ivRocket = (ImageView) mView.findViewById(R.id.ivRocket);
        ivRocket.setBackgroundResource(R.drawable.anim_rocket);
        AnimationDrawable rocketAnimation = (AnimationDrawable) ivRocket.getBackground();
        rocketAnimation.start();

        mMWM.addView(mView, mParams);
        MyOnTouchListener listener = new MyOnTouchListener();
        mView.setOnTouchListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMWM.removeView(mView);
    }

    class MyOnTouchListener implements View.OnTouchListener {

        private int mStartX;
        private int mStartY;
        private int mOffsetX;
        private int mOffsetY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartX = (int) event.getRawX();
                    mStartY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveX = (int) event.getRawX();
                    int moveY = (int) event.getRawY();

                    mOffsetX = moveX - mStartX;
                    mOffsetY = moveY - mStartY;

                    mParams.x += mOffsetX;
                    mParams.y += mOffsetY;
                    mMWM.updateViewLayout(v,mParams);

                    mStartX = moveX;
                    mStartY = moveY;
                    break;
                case MotionEvent.ACTION_UP:
//                    System.out.println("y: "+event.getRawX());
                    if (event.getRawY()>1400) {
                        lunchRocket();
                    }

                    break;

            }

            return true;
        }
    }

    private void lunchRocket() {
//        System.out.println(mWidth/2);
//        mParams.x = mWidth/2-mView.getWidth()/2;
        mParams.x = 0;
        mMWM.updateViewLayout(mView,mParams);

        Intent intent = new Intent(MyRocketService.this, ActivityRocketSmoke.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<=9;i++) {
                    mParams.y = (int) (mView.getY() - (mHeight-mView.getHeight()-20)/10*i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(UPDATE_ROCKET);

                }
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }
}
