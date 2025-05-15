package com.example.vehicletracking.tracking.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapTrackingViewModel extends ViewModel {
    private Handler handler;
    private Runnable runnable;
    private Marker marker;
    private double currentLat;
    private double currentLng;

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void startTracking(String carName, GoogleMap map) {
        Log.d("Firestore", "Fetching Firestore document for car: " + carName);

        firestore.collection("CarInfo")
                .whereEqualTo("name", carName)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        currentLat = Double.parseDouble(doc.getString("latitude"));
                        currentLng = Double.parseDouble(doc.getString("longitude"));

                        LatLng startPosition = new LatLng(currentLat, currentLng);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition, 15));
                        marker = map.addMarker(new MarkerOptions().position(startPosition).title(carName));

                        Log.d("Tracking", "Starting from: " + currentLat + ", " + currentLng);

                        handler = new Handler(Looper.getMainLooper());
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                currentLat += 0.0001;
                                currentLng += 0.0001;

                                LatLng newPosition = new LatLng(currentLat, currentLng);
                                if (marker != null) {
                                    marker.setPosition(newPosition);
                                }

                                map.moveCamera(CameraUpdateFactory.newLatLng(newPosition));
                                savePositionToFirestore(doc.getId(), newPosition); // сохраняем в Firestore

                                handler.postDelayed(this, 1000);
                            }
                        };
                        handler.post(runnable);
                    } else {
                        Log.e("Firestore", "Car not found with name: " + carName);
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching car data", e));
    }

    private void savePositionToFirestore(String docId, LatLng position) {
        firestore.collection("CarInfo").document(docId)
                .update("latitude", String.valueOf(position.latitude),
                        "longitude", String.valueOf(position.longitude))
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Position updated"))
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to update position", e));
    }


    public void stopTracking() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            handler = null;
            runnable = null;
            Log.d("MapTrackingVM", "Tracking stopped");
        }
    }

    public interface MarkerCallback {
        void onMarkerReady(MarkerOptions markerOptions, String address);
    }

    public void createMarkerWithAddress(LatLng latLng, Context context, MarkerCallback callback) {
        new Thread(() -> {
            String address = null;
            try {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    address = addresses.get(0).getAddressLine(0);
                } else {
                    address = "Адрес не найден";
                }
            } catch (IOException e) {
                address = "Ошибка геокодера";
            }

            String finalAddress = address;
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title("Выбрано место")
                    .snippet(finalAddress);

            new Handler(Looper.getMainLooper()).post(() -> callback.onMarkerReady(markerOptions, finalAddress));
        }).start();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopTracking();
    }
}
