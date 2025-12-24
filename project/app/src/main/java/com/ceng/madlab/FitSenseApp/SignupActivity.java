package com.ceng.madlab.FitSenseApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {


    private EditText etName, etSurname, etMail, etPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Toast.makeText(SignupActivity.this, "Kayıt Başarılı! Yönlendiriliyorsunuz...", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                startActivity(intent);


                finish();
            }
        });
    }
}