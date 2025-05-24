package com.example.vehicletracking.data;

import android.net.Uri;

import com.example.vehicletracking.data.CarRepository;
import com.example.vehicletracking.models.CarModel;
import com.example.vehicletracking.utils.ConcreteCarModelFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class CarRepositoryImpl implements CarRepository {

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef = storage.getReference();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final ConcreteCarModelFactory factory = new ConcreteCarModelFactory();

    @Override
    public void addCarWithImage(String name, String model, String number, String lat, String lon, Uri imageUri, CarCallback callback) {
        StorageReference ref = storageRef.child("photo/" + UUID.randomUUID());
        ref.putFile(imageUri)
                .addOnSuccessListener(task -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    uploadCarData(name, model, number, lat, lon, uri.toString(), callback);
                }).addOnFailureListener(e -> callback.onResult(CarRepository.Result.error(e.getMessage()))))
                .addOnFailureListener(e -> callback.onResult(CarRepository.Result.error(e.getMessage())));
    }

    @Override
    public void addCarWithoutImage(String name, String model, String number, String lat, String lon, CarCallback callback) {
        uploadCarData(name, model, number, lat, lon, "", callback);
    }

    private void uploadCarData(String name, String model, String number, String lat, String lon, String photoUrl, CarCallback callback) {
        CarModel car = factory.createCarModel(name, model, number, photoUrl, lat, lon);
        String id = firestore.collection("CarInfo").document().getId();
        car.setDocId(id);

        firestore.collection("CarInfo").document(id)
                .set(car, SetOptions.merge())
                .addOnSuccessListener(unused -> callback.onResult(CarRepository.Result.success()))
                .addOnFailureListener(e -> callback.onResult(CarRepository.Result.error(e.getMessage())));
    }
}
