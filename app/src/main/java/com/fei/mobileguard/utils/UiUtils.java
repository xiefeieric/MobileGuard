package com.fei.mobileguard.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Fei on 30/09/15.
 */
public class UiUtils {

    public static void showToast(final Activity context, final String msg) {

        if ("main".equals(Thread.currentThread().getName())) {
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        } else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}
