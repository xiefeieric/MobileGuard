package com.fei.mobileguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fei.mobileguard.R;

public class ActivitySetup1 extends ActivityBaseSetup {

    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextPage();
            }
        });
    }

    @Override
    void showNextPage() {
        Intent intent = new Intent(ActivitySetup1.this,ActivitySetup2.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }

    @Override
    void showPreviousPage() {

    }


}
