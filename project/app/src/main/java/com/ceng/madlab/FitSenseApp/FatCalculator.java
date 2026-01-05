package com.ceng.madlab.FitSenseApp;

public class FatCalculator {


    public static double calculateForMale(double height, double waist, double neck) {

        if ((waist - neck) <= 0) return 0.0;
        double result = 495 / (1.0324 - 0.19077 * Math.log10(waist - neck) + 0.15456 * Math.log10(height)) - 450;
        return clampResult(result);
    }
    public static double calculateForFemale(double height, double waist, double neck, double hip) {

        if ((waist + hip - neck) <= 0) return 0.0;
        double result = 495 / (1.29579 - 0.35004 * Math.log10(waist + hip - neck) + 0.22100 * Math.log10(height)) - 450;
        return clampResult(result);
    }


    public static double calculateBMI(double weightKg, double heightCm) {
        if (heightCm <= 0 || weightKg <= 0) return 0.0;
        double heightMeter = heightCm / 100.0;
        double bmi = weightKg / (heightMeter * heightMeter);

        return Math.round(bmi * 10.0) / 10.0;
    }
    public static double calculateFatMass(double weightKg, double fatRate) {
        double fatMass = weightKg * (fatRate / 100.0);
        return Math.round(fatMass * 10.0) / 10.0;
    }


    public static String getBodyStatus(double fatRate, boolean isMale) {
        if (isMale) {
            if (fatRate < 2) return "Critical Low"; // Çok düşük
            if (fatRate < 6) return "Essential Fat";
            if (fatRate < 14) return "Athlete";
            if (fatRate < 18) return "Fitness";
            if (fatRate < 25) return "Average";
            return "Obese";
        } else {
            if (fatRate < 10) return "Critical Low";
            if (fatRate < 14) return "Essential Fat";
            if (fatRate < 21) return "Athlete";
            if (fatRate < 25) return "Fitness";
            if (fatRate < 32) return "Average";
            return "Obese";
        }
    }
    private static double clampResult(double value) {
        if (value < 0) value = 0;
        if (value > 100) value = 99.9;

        return Math.round(value * 10.0) / 10.0;
    }
}