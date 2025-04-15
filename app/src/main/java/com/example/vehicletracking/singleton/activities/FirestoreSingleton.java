package com.example.vehicletracking.singleton.activities;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreSingleton {
    private static FirebaseFirestore instance;

    private FirestoreSingleton() {}

    public static FirebaseFirestore getInstance() {
        if (instance == null) {
            instance = FirebaseFirestore.getInstance();
        }
        return instance;
    }
}
