package com.ceseagod.rajarani.mainfolder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ceseagod.rajarani.MainActivity;
import com.ceseagod.rajarani.R;

public class SpalshActivitys extends AppCompatActivity {

        private static int SPLASH_TIME_OUT = 5000;
        //Hooks
        View first,second,third,fourth,fifth,sixth;
        TextView a, slogan;
        //Animations
        Animation topAnimantion,bottomAnimation,middleAnimation;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_spalsh_activitys);
            //Hooks
            first = findViewById(R.id.first_line);
            second = findViewById(R.id.second_line);
            third = findViewById(R.id.third_line);
            fourth = findViewById(R.id.fourth_line);
            fifth = findViewById(R.id.fifth_line);
            sixth = findViewById(R.id.sixth_line);
            a = findViewById(R.id.a);
            slogan = findViewById(R.id.tagLine);

            //Animation Calls
            topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_animation);
            bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
            middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);

            //-----------Setting Animations to the elements of SplashScreen-------- - first.setAnimation(topAnimantion);
            second.setAnimation(topAnimantion);
            third.setAnimation(topAnimantion);
            fourth.setAnimation(topAnimantion);
            fifth.setAnimation(topAnimantion);
            sixth.setAnimation(topAnimantion);
            a.setAnimation(middleAnimation);
            slogan.setAnimation(bottomAnimation);

            //Splash Screen Code to call new Activity after some time
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    Calling new Activity

                    Intent intent = new Intent(SpalshActivitys.this, check.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
}