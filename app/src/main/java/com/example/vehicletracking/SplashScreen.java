package com.example.vehicletracking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000;
    ImageView Roadmap, Carimage;
    TextView text1, text2;
    Animation Top_anim, Bottom_anim, Left_anim;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Roadmap=findViewById(R.id.roadmap);
        Carimage=findViewById(R.id.car);
        text1=findViewById(R.id.text1);
        text2=findViewById(R.id.text2);
        Top_anim= AnimationUtils.loadAnimation(this, R.anim.topanim);
        Bottom_anim= AnimationUtils.loadAnimation(this, R.anim.bottomanim);
        Left_anim= AnimationUtils.loadAnimation(this, R.anim.leftanim);

        Roadmap.setAnimation(Top_anim);
        Carimage.setAnimation(Left_anim);
        text1.setAnimation(Bottom_anim);
        text2.setAnimation(Bottom_anim);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish(); // Закрываем SplashScreenActivity
        }, SPLASH_TIME_OUT);
    }
}