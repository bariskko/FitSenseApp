package com.ceng.madlab.FitSenseApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button btnEnterMeasurement;
    private ImageView btnHistory, btnLogout;
    private ProgressBar progressBar;
    private TextView tvFatRate, tvFatStatus;
    private RecyclerView recyclerRecent;
    private HistoryAdapter adapter;
    private List<Measurement> recentList;
    private FirebaseFirestore db;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            goLogin();
            return;
        }
        userID = user.getUid();
        initViews();
        setupListeners();
        loadUserData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(userID != null) {
            loadUserData();
        }
    }

    private void initViews() {
        btnEnterMeasurement = findViewById(R.id.btnEnterMeasurement);
        btnHistory = findViewById(R.id.btnHistory);
        btnLogout = findViewById(R.id.btnLogout);
        progressBar = findViewById(R.id.progressBar);
        tvFatRate = findViewById(R.id.tvFatRate);
        tvFatStatus = findViewById(R.id.tvFatStatus);
        recyclerRecent = findViewById(R.id.recyclerRecent);
        recyclerRecent.setLayoutManager(new LinearLayoutManager(this));
        recentList = new ArrayList<>();
        adapter = new HistoryAdapter(recentList, userID);
        recyclerRecent.setAdapter(adapter);
    }

    private void setupListeners() {
        btnEnterMeasurement.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MeasurementActivity.class);
            startActivity(intent);
        });


        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
            startActivity(intent);
        });


        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Çıkış Yap")
                    .setMessage("Çıkış yapmak istediğinize emin misiniz?")
                    .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            goLogin();
                        }
                    })
                    .setNegativeButton("Hayır", null)
                    .show();
        });
    }

    private void loadUserData() {
        db.collection("users").document(userID).collection("measurements")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    recentList.clear();

                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot latestDoc = queryDocumentSnapshots.getDocuments().get(0);
                        double fatRate = latestDoc.getDouble("fatRate");
                        String status = latestDoc.getString("status");

                        tvFatRate.setText(String.format("%.1f%%", fatRate));
                        tvFatStatus.setText(status);
                        progressBar.setProgress((int) fatRate);

                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Measurement m = new Measurement(
                                    doc.getId(),
                                    doc.getDouble("fatRate"),
                                    doc.getDouble("bmi"),
                                    doc.getString("status"),
                                    doc.getTimestamp("date")
                            );
                            recentList.add(m);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        tvFatRate.setText("--%");
                        tvFatStatus.setText("No Data");
                        progressBar.setProgress(0);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Veri alınamadı", Toast.LENGTH_SHORT).show();
                });
    }

    private void goLogin() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}