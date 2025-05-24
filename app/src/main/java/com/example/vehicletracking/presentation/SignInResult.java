package com.example.vehicletracking.presentation;

public class SignInResult {
    public final boolean success;
    public final String message;

    public SignInResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
