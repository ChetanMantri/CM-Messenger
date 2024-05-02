package com.example.cmmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cmmessenger.databinding.ActivitySplashBinding;


public class splash extends AppCompatActivity {
    ActivitySplashBinding binding;
    Animation top,bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        top= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        binding.img.setAnimation(top);
        binding.logotext.startAnimation(bottom);
        binding.from.startAnimation(bottom);
        binding.name.startAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(splash.this,registration.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}