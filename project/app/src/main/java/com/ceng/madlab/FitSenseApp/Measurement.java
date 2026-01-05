package com.ceng.madlab.FitSenseApp;

import com.google.firebase.Timestamp;

public class Measurement {
    private String id;
    private double fatRate;
    private double bmi;
    private String status;
    private Timestamp date;

    public Measurement() {

    }

    public Measurement(String id, double fatRate, double bmi, String status, Timestamp date) {
        this.id = id;
        this.fatRate = fatRate;
        this.bmi = bmi;
        this.status = status;
        this.date = date;
    }

    public String getId() { return id; }
    public double getFatRate() { return fatRate; }
    public double getBmi() { return bmi; }
    public String getStatus() { return status; }
    public Timestamp getDate() { return date; }
}