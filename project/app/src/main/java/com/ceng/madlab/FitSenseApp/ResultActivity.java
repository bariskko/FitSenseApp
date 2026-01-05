package com.ceng.madlab.FitSenseApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private TextView tvResultRate, tvResultStatus, tvBMIResult;
    private Button btnBackHome;
    FirebaseFirestore db;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userID = user.getUid();
        }

        initViews();

        double fatRate = getIntent().getDoubleExtra("fatRate", 0.0);
        double bmi = getIntent().getDoubleExtra("bmi", 0.0);
        boolean isMale = getIntent().getBooleanExtra("isMale", true);

        String status = FatCalculator.getBodyStatus(fatRate, isMale);

        tvResultRate.setText(String.format("%% %.1f", fatRate));
        tvResultStatus.setText(status);

        if (tvBMIResult != null) {
            tvBMIResult.setText(String.format("%.1f", bmi));
        }
        saveResultToFirebase(fatRate, bmi, status);
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }


    private void saveResultToFirebase(double fatRate, double bmi, String status) {
        if (userID == null) return;
        Map<String, Object> measurement = new HashMap<>();
        measurement.put("fatRate", fatRate);
        measurement.put("bmi", bmi);
        measurement.put("status", status);
        measurement.put("date", Timestamp.now());


        db.collection("users").document(userID).collection("measurements")
                .add(measurement)
                .addOnSuccessListener(documentReference -> {

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ResultActivity.this, "Kayıt hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void initViews() {
        tvResultRate = findViewById(R.id.tvResultRate);
        tvResultStatus = findViewById(R.id.tvResultStatus);
        tvBMIResult = findViewById(R.id.tvBMIResult);
        btnBackHome = findViewById(R.id.btnBackHome);
    }
}