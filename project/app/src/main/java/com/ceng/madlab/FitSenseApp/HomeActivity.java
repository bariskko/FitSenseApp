package com.ceng.madlab.FitSenseApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // XML dosya ismin activity_home olmalı

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        btnMenu = findViewById(R.id.btnMenu);

        // Menü butonuna tıklayınca aç
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Menü elemanlarına tıklama
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    // Zaten buradayız
                } else if (id == R.id.nav_history) {
                    Toast.makeText(HomeActivity.this, "History Tıklandı", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    // Çıkış yapıp başa dön
                    finish();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}