package com.example.vehicletracking.presentation;

public class SignUpState {
    public final boolean isLoading;
    public final String message;
    public final boolean success;

    public SignUpState(boolean isLoading, String message, boolean success) {
        this.isLoading = isLoading;
        this.message = message;
        this.success = success;
    }

    public static SignUpState idle() {
        return new SignUpState(false, null, false);
    }

    public static SignUpState loading() {
        return new SignUpState(true, null, false);
    }

    public static SignUpState success(String message) {
        return new SignUpState(false, message, true);
    }

    public static SignUpState error(String message) {
        return new SignUpState(false, message, false);
    }
}
