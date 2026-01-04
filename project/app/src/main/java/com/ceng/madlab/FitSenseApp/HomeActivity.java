package com.ceng.madlab.FitSenseApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView; // ImageView eklendi
import android.widget.ProgressBar;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private Button btnEnterMeasurement;
    private ImageView btnHistory; // Yeni butonumuz
    private ProgressBar progressBar;
    private TextView tvFatRate, tvFatStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        updateDashboard();

        // Measurement Butonu
        btnEnterMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MeasurementActivity.class);
                startActivity(intent);
            }
        });

        // YENİ: History Butonu
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDashboard();
    }

    private void initViews() {
        btnEnterMeasurement = findViewById(R.id.btnEnterMeasurement);

        // Yeni butonu bağlıyoruz
        btnHistory = findViewById(R.id.btnHistory);

        progressBar = findViewById(R.id.progressBar);
        tvFatRate = findViewById(R.id.tvFatRate);
        tvFatStatus = findViewById(R.id.tvFatStatus);
    }

    private void updateDashboard() {
        double currentFatRate = 17.3;
        String currentStatus = "Fit";

        if (tvFatRate != null) tvFatRate.setText(currentFatRate + "%");
        if (tvFatStatus != null) tvFatStatus.setText(currentStatus);
        if (progressBar != null) progressBar.setProgress((int) currentFatRate);
    }
}