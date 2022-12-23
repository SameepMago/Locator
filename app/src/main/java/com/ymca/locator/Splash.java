package com.ymca.locator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        logo=(ImageView)findViewById(R.id.logo);
        Animation an1=AnimationUtils.loadAnimation(this,R.anim.slide_up);
        logo.startAnimation(an1);
        CountDownTimer countDownTimer=new CountDownTimer(3500,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Log.i("prog","timer progress");
            }

            @Override
            public void onFinish() {
                Intent intent=new Intent(Splash.this,Login.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }
}