package com.example.vehicletracking.presentation;

public interface SignInEffect {
    class NavigateToMainScreen implements SignInEffect {}
    class ShowToast implements SignInEffect {
        public final String message;

        public ShowToast(String message) {
            this.message = message;
        }
    }
}
