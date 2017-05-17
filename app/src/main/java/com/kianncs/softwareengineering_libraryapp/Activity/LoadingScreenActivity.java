package com.kianncs.softwareengineering_libraryapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.kianncs.softwareengineering_libraryapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingScreenActivity extends AppCompatActivity {
    private static final long SPLASH_TIME=4000;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView = (ImageView) findViewById(R.id.imageViewLogo);
        setContentView(R.layout.activity_loading_screen);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent().setClass(LoadingScreenActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_TIME);

    }
}
