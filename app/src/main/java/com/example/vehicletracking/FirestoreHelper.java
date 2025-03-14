package com.example.vehicletracking;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class FirestoreHelper {
    private final FirebaseFirestore db;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    // Добавление ТС в Firestore
    public void addVehicle(String name, String type, double lat, double lng) {
        Map<String, Object> vehicle = new HashMap<>();
        vehicle.put("name", name);
        vehicle.put("type", type);
        vehicle.put("location", new HashMap<String, Double>() {{
            put("lat", lat);
            put("lng", lng);
        }});

        db.collection("vehicles")
                .add(vehicle)
                .addOnSuccessListener(documentReference ->
                        System.out.println("ТС добавлено с ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        System.err.println("Ошибка: " + e.getMessage()));
    }
}
