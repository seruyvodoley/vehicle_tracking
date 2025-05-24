package com.example.vehicletracking.data;

import androidx.annotation.NonNull;

import com.example.vehicletracking.domain.CarLocation;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.function.Consumer;

public class CarRemoteDataSource {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void getCarLocation(String carName, Consumer<CarLocation> onSuccess, Consumer<String> onError) {
        firestore.collection("CarInfo")
                .whereEqualTo("name", carName)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        double lat = Double.parseDouble(doc.getString("latitude"));
                        double lng = Double.parseDouble(doc.getString("longitude"));
                        onSuccess.accept(new CarLocation(lat, lng));
                    } else {
                        onError.accept("Автомобиль не найден");
                    }
                })
                .addOnFailureListener(e -> onError.accept(e.getMessage()));
    }

    public void updateCarLocation(String carName, CarLocation location, Runnable onSuccess, Consumer<String> onError) {
        firestore.collection("CarInfo")
                .whereEqualTo("name", carName)
                .limit(1)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        String docId = snapshot.getDocuments().get(0).getId();
                        firestore.collection("CarInfo")
                                .document(docId)
                                .update("latitude", String.valueOf(location.getLatitude()),
                                        "longitude", String.valueOf(location.getLongitude()))
                                .addOnSuccessListener(aVoid -> onSuccess.run())
                                .addOnFailureListener(e -> onError.accept(e.getMessage()));
                    } else {
                        onError.accept("Документ не найден");
                    }
                })
                .addOnFailureListener(e -> onError.accept(e.getMessage()));
    }
}
