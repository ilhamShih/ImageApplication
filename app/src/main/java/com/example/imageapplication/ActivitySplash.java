package com.example.imageapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imageapplication.activity.BazeActivity;

public class ActivitySplash extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent intent = new Intent(getApplicationContext(), BazeActivity.class);
                    startActivity(intent);
                    finish();
                }

        }, 3000);

    }

}