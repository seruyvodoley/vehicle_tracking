package com.example.vehicletracking.data;

import android.net.Uri;

public interface CarRepository {

    interface CarCallback {
        void onResult(Result result);
    }

    class Result {
        public final boolean isSuccess;
        public final String errorMessage;

        public Result(boolean isSuccess, String errorMessage) {
            this.isSuccess = isSuccess;
            this.errorMessage = errorMessage;
        }

        public static Result success() {
            return new Result(true, null);
        }

        public static Result error(String message) {
            return new Result(false, message);
        }
    }

    void addCarWithImage(String name, String model, String number, String lat, String lon, Uri imageUri, CarCallback callback);
    void addCarWithoutImage(String name, String model, String number, String lat, String lon, CarCallback callback);
}
