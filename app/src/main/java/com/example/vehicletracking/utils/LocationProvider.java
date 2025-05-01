package com.example.vehicletracking.utils;

public interface LocationProvider {
    void start(LocationCallback callback);
    void stop();
}
