package com.example.vehicletracking.presentation;

public interface SignUpIntent {
    class Submit implements SignUpIntent {
        public final String name;
        public final String email;
        public final String password;

        public Submit(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }
    }
}
