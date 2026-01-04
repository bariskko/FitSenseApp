package com.ceng.madlab.FitSenseApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast; // Kullanıcıya bilgi vermek için

// --- FIREBASE KÜTÜPHANELERİ ---
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private TextView tvResultRate, tvResultStatus, tvBMIResult;
    private Button btnBackHome;

    // Firebase Nesneleri
    FirebaseFirestore db;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 1. Firebase Kurulumu
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Kullanıcı giriş yapmışsa ID'sini al, yoksa (güvenlik için) boş dön
        if (user != null) {
            userID = user.getUid();
        }

        initViews();

        // 2. Verileri Al
        double fatRate = getIntent().getDoubleExtra("fatRate", 0.0);
        double bmi = getIntent().getDoubleExtra("bmi", 0.0);
        boolean isMale = getIntent().getBooleanExtra("isMale", true);

        // 3. Durumu Hesapla
        String status = FatCalculator.getBodyStatus(fatRate, isMale);

        // 4. Ekrana Yazdır
        tvResultRate.setText(String.format("%% %.1f", fatRate)); // Virgülden sonra 1 basamak
        tvResultStatus.setText(status);

        if (tvBMIResult != null) {
            tvBMIResult.setText(String.format("%.1f", bmi));
        }

        // --- 5. VERİTABANINA KAYDETME İŞLEMİ (OTOMATİK) ---
        // Kullanıcı sonucu gördüğü an veritabanına kaydediyoruz.
        saveResultToFirebase(fatRate, bmi, status);

        // 6. Ana Sayfaya Dön
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

    // --- FIREBASE KAYIT FONKSİYONU ---
    private void saveResultToFirebase(double fatRate, double bmi, String status) {
        // Eğer kullanıcı ID'si yoksa işlem yapma (Hata almamak için)
        if (userID == null) return;

        // Veri paketi oluşturuyoruz
        Map<String, Object> measurement = new HashMap<>();
        measurement.put("fatRate", fatRate);
        measurement.put("bmi", bmi);
        measurement.put("status", status);
        measurement.put("date", Timestamp.now()); // O anki tarih ve saat

        // Veritabanına gönder: users -> (userID) -> measurements -> (rastgeleBelgeID)
        db.collection("users").document(userID).collection("measurements")
                .add(measurement)
                .addOnSuccessListener(documentReference -> {
                    // Başarılı olursa (İstersen buraya Toast koyabilirsin ama otomatik olduğu için gerek yok)
                    // Toast.makeText(ResultActivity.this, "Sonuç kaydedildi", Toast.LENGTH_SHORT).show();
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