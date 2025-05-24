package com.example.vehicletracking.presentation;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class TrackingViewModel extends ViewModel {

    private final MutableLiveData<TrackingState> state = new MutableLiveData<>(TrackingState.idle());
    private final Handler handler = new Handler(Looper.getMainLooper());

    private boolean tracking = false;
    private Marker marker;
    private Runnable trackingRunnable;

    public LiveData<TrackingState> getState() {
        return state;
    }

    public void processIntent(TrackingIntent intent) {
        if (intent instanceof TrackingIntent.StartTracking) {
            startTracking(((TrackingIntent.StartTracking) intent).carName);
        } else if (intent instanceof TrackingIntent.StopTracking) {
            stopTracking();
        } else if (intent instanceof TrackingIntent.UpdateLocation) {
            TrackingIntent.UpdateLocation loc = (TrackingIntent.UpdateLocation) intent;
            state.setValue(new TrackingState(true, loc.latitude, loc.longitude, null));
        } else if (intent instanceof TrackingIntent.ErrorOccurred) {
            state.setValue(new TrackingState(false, 0.0, 0.0, ((TrackingIntent.ErrorOccurred) intent).message));
        }
    }

    private void startTracking(String carName) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("CarInfo")
                .whereEqualTo("name", carName)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        double lat = Double.parseDouble(doc.getString("latitude"));
                        double lng = Double.parseDouble(doc.getString("longitude"));

                        startSimulation(lat, lng, doc.getId());
                    } else {
                        processIntent(new TrackingIntent.ErrorOccurred("Машина не найдена"));
                    }
                })
                .addOnFailureListener(e -> processIntent(new TrackingIntent.ErrorOccurred("Ошибка Firestore: " + e.getMessage())));
    }

    private void startSimulation(double lat, double lng, String docId) {
        tracking = true;
        double[] currentLat = {lat};
        double[] currentLng = {lng};
        Random random = new Random();

        trackingRunnable = new Runnable() {
            @Override
            public void run() {
                if (!tracking) return;

                double latOffset = (random.nextDouble() - 0.5) * 0.0003;
                double lngOffset = (random.nextDouble() - 0.5) * 0.0003;
                currentLat[0] += latOffset;
                currentLng[0] += lngOffset;

                LatLng newPos = new LatLng(currentLat[0], currentLng[0]);

                FirebaseFirestore.getInstance()
                        .collection("CarInfo").document(docId)
                        .update("latitude", String.valueOf(newPos.latitude),
                                "longitude", String.valueOf(newPos.longitude));

                processIntent(new TrackingIntent.UpdateLocation(newPos.latitude, newPos.longitude));
                handler.postDelayed(this, 500);
            }
        };

        handler.post(trackingRunnable);
    }

    private void stopTracking() {
        tracking = false;
        if (trackingRunnable != null) {
            handler.removeCallbacks(trackingRunnable);
            trackingRunnable = null;
        }
        state.setValue(TrackingState.idle());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopTracking();
    }
}
