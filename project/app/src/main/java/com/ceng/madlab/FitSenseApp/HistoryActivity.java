package com.ceng.madlab.FitSenseApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<Measurement> measurementList;

    private Button btnSaveResult;
    private Button btnNewMeasurement;


    private FirebaseFirestore db;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userID = user.getUid();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        initViews();
        setupRecyclerView();
        loadDataFromFirebase();
        setupListeners();
    }

    private void initViews() {

        btnSaveResult = findViewById(R.id.btnGenerateReport);
        btnNewMeasurement = findViewById(R.id.btnShareReport);
        recyclerView = findViewById(R.id.recyclerViewHistory);
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        measurementList = new ArrayList<>();

    }

    private void loadDataFromFirebase() {
        db.collection("users").document(userID).collection("measurements")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    measurementList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {

                        Measurement m = new Measurement(
                                doc.getId(),
                                doc.getDouble("fatRate"),
                                doc.getDouble("bmi"),
                                doc.getString("status"),
                                doc.getTimestamp("date")
                        );
                        measurementList.add(m);
                    }


                    adapter = new HistoryAdapter(measurementList, userID);
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HistoryActivity.this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void setupListeners() {
        btnSaveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HistoryActivity.this, "Saved / Report Generated!", Toast.LENGTH_SHORT).show();
            }
        });


        btnNewMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MeasurementActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}