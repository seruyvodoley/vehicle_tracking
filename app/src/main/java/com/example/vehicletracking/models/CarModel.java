package com.example.vehicletracking.models;

public class CarModel {
    private String name, model, number, docId, photoUrl, latitude, longitude, userId;

    public CarModel() {}

    public CarModel(String name, String model, String number, String docId, String photoUrl, String latitude, String longitude, String userId) {
        this.name = name;
        this.model = model;
        this.number = number;
        this.docId = docId;
        this.photoUrl = photoUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getDocId() { return docId; }
    public void setDocId(String docId) { this.docId = docId; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
