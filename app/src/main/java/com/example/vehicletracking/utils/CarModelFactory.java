package com.example.vehicletracking.utils;

import com.example.vehicletracking.models.CarModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public interface CarModelFactory {
    CarModel createCarModel(String name, String model, String number, String photoUrl, String lat, String lon);
}

