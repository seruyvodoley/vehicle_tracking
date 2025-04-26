package com.example.vehicletracking.utils;

import com.example.vehicletracking.models.CarModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ConcreteCarModelFactory implements CarModelFactory {
    private final FirebaseAuth auth;

    public ConcreteCarModelFactory() {
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    public CarModel createCarModel(String name, String model, String number, String photoUrl, String lat, String lon) {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        return new CarModel(name, model, number, "", photoUrl, lat, lon, userId);
    }
}
