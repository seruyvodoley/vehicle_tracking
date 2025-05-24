package com.example.vehicletracking.presentation;

public class AddCarState {
    public final boolean isUploading;
    public final boolean isSuccess;
    public final String errorMessage;

    public AddCarState(boolean isUploading, boolean isSuccess, String errorMessage) {
        this.isUploading = isUploading;
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
    }

    public static AddCarState initial() {
        return new AddCarState(false, false, null);
    }
}
