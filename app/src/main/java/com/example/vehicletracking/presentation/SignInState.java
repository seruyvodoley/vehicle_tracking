package com.example.vehicletracking.presentation;

public class SignInState {
    public final boolean isLoading;
    public final String message;
    public final boolean isSuccess;

    public SignInState(boolean isLoading, String message, boolean isSuccess) {
        this.isLoading = isLoading;
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public static SignInState idle() {
        return new SignInState(false, null, false);
    }

    public static SignInState loading() {
        return new SignInState(true, null, false);
    }

    public static SignInState success() {
        return new SignInState(false, "Вход выполнен успешно", true);
    }

    public static SignInState error(String message) {
        return new SignInState(false, message, false);
    }
}
