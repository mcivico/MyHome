package com.infobosccoma.projecte.myhome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.infobosccoma.projecte.myhome.Model.ListFlatsActivity;


public class SplashActivity extends ActionBarActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){


            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, ListFlatsActivity.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);
    }



}
