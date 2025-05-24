package com.example.vehicletracking.presentation;

import android.net.Uri;

public abstract class AddCarEvent {
    public static class SubmitWithImage extends AddCarEvent {
        public final String name, model, number, lat, lon;
        public final Uri imageUri;

        public SubmitWithImage(String name, String model, String number, String lat, String lon, Uri imageUri) {
            this.name = name;
            this.model = model;
            this.number = number;
            this.lat = lat;
            this.lon = lon;
            this.imageUri = imageUri;
        }
    }

    public static class SubmitWithoutImage extends AddCarEvent {
        public final String name, model, number, lat, lon;

        public SubmitWithoutImage(String name, String model, String number, String lat, String lon) {
            this.name = name;
            this.model = model;
            this.number = number;
            this.lat = lat;
            this.lon = lon;
        }
    }
}
