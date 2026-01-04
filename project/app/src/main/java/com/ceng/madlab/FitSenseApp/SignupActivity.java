package com.ceng.madlab.FitSenseApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Firebase kütüphaneleri
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etSurname, etMail, etPassword;
    private Button btnSignUp;
    private FirebaseAuth mAuth; // Firebase yetkilisi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Firebase'i başlat
        mAuth = FirebaseAuth.getInstance();

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etMail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(SignupActivity.this, "Lütfen alanları doldurun", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length() < 6){
                    Toast.makeText(SignupActivity.this, "Şifre en az 6 karakter olmalı", Toast.LENGTH_SHORT).show();
                    return;
                }

                // --- FIREBASE KAYIT İŞLEMİ ---
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Kayıt Başarılı!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SignupActivity.this, "Hata: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}