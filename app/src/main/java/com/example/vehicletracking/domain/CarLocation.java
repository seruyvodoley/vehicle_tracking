package com.example.vehicletracking.domain;

public class CarLocation {
    private double latitude;
    private double longitude;

    public CarLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
