package com.example.vehicletracking.presentation;

public abstract class AddCarEffect {
    public static class ShowToast extends AddCarEffect {
        public final String message;
        public ShowToast(String message) {
            this.message = message;
        }
    }

    public static class NavigateBack extends AddCarEffect {}
}
