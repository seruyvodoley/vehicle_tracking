package com.example.vehicletracking.singleton.activities;

import com.google.firebase.auth.FirebaseAuth;

public class AuthSingleton {
    private static FirebaseAuth instance;

    private AuthSingleton() {}

    public static FirebaseAuth getInstance() {
        if (instance == null) {
            instance = FirebaseAuth.getInstance();
        }
        return instance;
    }
}
