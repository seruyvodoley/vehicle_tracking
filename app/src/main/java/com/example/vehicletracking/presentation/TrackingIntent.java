package com.example.vehicletracking.presentation;

public interface TrackingIntent {
    class StartTracking implements TrackingIntent {
        public final String carName;
        public StartTracking(String carName) { this.carName = carName; }
    }

    class StopTracking implements TrackingIntent {}

    class UpdateLocation implements TrackingIntent {
        public final double latitude;
        public final double longitude;
        public UpdateLocation(double lat, double lng) {
            this.latitude = lat;
            this.longitude = lng;
        }
    }

    class ErrorOccurred implements TrackingIntent {
        public final String message;
        public ErrorOccurred(String message) { this.message = message; }
    }
}
