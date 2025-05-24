package com.example.vehicletracking.presentation;

public class TrackingState {
    public final boolean isTracking;
    public final double latitude;
    public final double longitude;
    public final String error;

    public TrackingState(boolean isTracking, double latitude, double longitude, String error) {
        this.isTracking = isTracking;
        this.latitude = latitude;
        this.longitude = longitude;
        this.error = error;
    }

    public static TrackingState idle() {
        return new TrackingState(false, 0.0, 0.0, null);
    }
}
