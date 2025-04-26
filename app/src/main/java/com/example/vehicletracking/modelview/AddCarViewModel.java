package com.example.vehicletracking.modelview;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vehicletracking.models.CarModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

public class AddCarViewModel extends ViewModel {

    // Observer pattern: LiveData
    private final MutableLiveData<Boolean> isUploading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isSuccess = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>(null);

    public LiveData<Boolean> getIsUploading() {
        return isUploading;
    }

    public LiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Singleton: единичный доступ к Firebase
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    // Factory: создание CarModel
    private CarModel createCarModel(String name, String model, String number, String photoUrl, String lat, String lon) {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        return new CarModel(name, model, number, "", photoUrl, lat, lon, userId);
    }

    public void uploadImageAndCar(Uri imageUri, String name, String model, String number, String lat, String lon) {
        isUploading.setValue(true);
        final StorageReference imgRef = storageReference.child("photo/" + UUID.randomUUID());

        imgRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            if (uri != null) {
                                uploadCarData(name, model, number, uri.toString(), lat, lon);
                            }
                        }).addOnFailureListener(e -> {
                            isUploading.setValue(false);
                            errorMessage.setValue(e.getMessage());
                        }))
                .addOnFailureListener(e -> {
                    isUploading.setValue(false);
                    errorMessage.setValue(e.getMessage());
                });
    }

    public void uploadCarWithoutImage(String name, String model, String number, String lat, String lon) {
        isUploading.setValue(true);
        uploadCarData(name, model, number, "", lat, lon);
    }

    private void uploadCarData(String name, String model, String number, String photoUrl, String lat, String lon) {
        DocumentReference docRef = firestore.collection("CarInfo").document();
        CarModel car = createCarModel(name, model, number, photoUrl, lat, lon);
        car.setDocId(docRef.getId());

        docRef.set(car, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    isUploading.setValue(false);
                    isSuccess.setValue(true);
                })
                .addOnFailureListener(e -> {
                    isUploading.setValue(false);
                    errorMessage.setValue(e.getMessage());
                });
    }


}
