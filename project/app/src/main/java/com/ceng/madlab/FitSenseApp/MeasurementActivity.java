package com.ceng.madlab.FitSenseApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MeasurementActivity extends AppCompatActivity {


    private EditText etHeight, etWeight, etNeck, etWaist, etHip;
    private TextView tvHipLabel;
    private RadioGroup radioGroupGender;
    private RadioButton rbMale, rbFemale;
    private Button btnCalculate;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);
        initViews();
        setupListeners();

        if (rbMale.isChecked()) {
            updateHipVisibility(R.id.rbMale);
            updateGenderButtonColors(true);
        }
    }

    private void initViews() {
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etNeck = findViewById(R.id.etNeck);
        etWaist = findViewById(R.id.etWaist);
        etHip = findViewById(R.id.etHip);
        tvHipLabel = findViewById(R.id.tvHipLabel);

        radioGroupGender = findViewById(R.id.radioGroupGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeasurementActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        radioGroupGender.setOnCheckedChangeListener((group, checkedId) -> {
            updateHipVisibility(checkedId);
            if (checkedId == R.id.rbMale) {
                updateGenderButtonColors(true);
            } else {
                updateGenderButtonColors(false);
            }
        });

        btnCalculate.setOnClickListener(v -> calculateAndProceed());
    }

    private void updateGenderButtonColors(boolean isMaleSelected) {
        int selectedColor = ContextCompat.getColor(this, R.color.primary_blue);
        int unselectedColor = Color.WHITE;
        int selectedTextColor = Color.WHITE;
        int unselectedTextColor = Color.BLACK;

        if (isMaleSelected) {
            rbMale.setBackgroundColor(selectedColor);
            rbMale.setTextColor(selectedTextColor);
            rbFemale.setBackgroundColor(unselectedColor);
            rbFemale.setTextColor(unselectedTextColor);
        } else {
            rbFemale.setBackgroundColor(selectedColor);
            rbFemale.setTextColor(selectedTextColor);
            rbMale.setBackgroundColor(unselectedColor);
            rbMale.setTextColor(unselectedTextColor);
        }
    }

    private void updateHipVisibility(int checkedId) {
        if (checkedId == R.id.rbMale) {
            etHip.setVisibility(View.GONE);
            tvHipLabel.setVisibility(View.GONE);
            etHip.setText("");
        } else {
            etHip.setVisibility(View.VISIBLE);
            tvHipLabel.setVisibility(View.VISIBLE);
        }
    }

    private void calculateAndProceed() {
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        boolean isMale = (selectedId == R.id.rbMale);

        // Boş alan kontrolü
        if (isInputEmpty(etHeight) || isInputEmpty(etWeight) ||
                isInputEmpty(etWaist) || isInputEmpty(etNeck)) {
            Toast.makeText(this, "Please fill all required fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isMale && isInputEmpty(etHip)) {
            Toast.makeText(this, "Hip measurement is required for females!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Verileri Sayıya Çevir
            double height = Double.parseDouble(etHeight.getText().toString());
            double weight = Double.parseDouble(etWeight.getText().toString());
            double waist = Double.parseDouble(etWaist.getText().toString());
            double neck = Double.parseDouble(etNeck.getText().toString());
            double hip = 0;

            if (!isMale) {
                hip = Double.parseDouble(etHip.getText().toString());
            }

            // --- HESAPLAMA KISMI (GÜNCELLENDİ) ---

            // 1. Yağ Oranını Hesapla (FatCalculator Sınıfını Kullanarak)
            double calculatedFatRate;
            if (isMale) {
                calculatedFatRate = FatCalculator.calculateForMale(height, waist, neck);
            } else {
                calculatedFatRate = FatCalculator.calculateForFemale(height, waist, neck, hip);
            }

            // 2. BMI Hesapla
            double calculatedBMI = FatCalculator.calculateBMI(weight, height);

            // 3. Verileri Paketle ve LoadingActivity'ye Gönder
            Intent intent = new Intent(MeasurementActivity.this, LoadingActivity.class);
            intent.putExtra("isMale", isMale);

            // Hesaplanan Sonuçları Gönderiyoruz:
            intent.putExtra("fatRate", calculatedFatRate);
            intent.putExtra("bmi", calculatedBMI);

            // Ham verileri de gönderelim (İleride detay sayfasında lazım olabilir)
            intent.putExtra("height", height);
            intent.putExtra("weight", weight);
            intent.putExtra("waist", waist);
            intent.putExtra("neck", neck);
            intent.putExtra("hip", hip);

            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isInputEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }
}