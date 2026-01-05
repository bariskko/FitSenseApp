package com.ceng.madlab.FitSenseApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, ResultActivity.class);
                boolean isMale = getIntent().getBooleanExtra("isMale", true);
                double fatRate = getIntent().getDoubleExtra("fatRate", 0.0);
                double bmi = getIntent().getDoubleExtra("bmi", 0.0); // BMI verisini yakala!


                intent.putExtra("isMale", isMale);
                intent.putExtra("fatRate", fatRate);
                intent.putExtra("bmi", bmi);

                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}