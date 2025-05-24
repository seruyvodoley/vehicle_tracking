package com.example.vehicletracking.domain;

import com.example.vehicletracking.data.CarRepository;
import com.example.vehicletracking.presentation.AddCarEvent;

public class AddCarUseCase {

    private final CarRepository repository;

    public AddCarUseCase(CarRepository repository) {
        this.repository = repository;
    }

    public void execute(AddCarEvent event, CarRepository.CarCallback callback) {
        if (event instanceof AddCarEvent.SubmitWithImage) {
            AddCarEvent.SubmitWithImage e = (AddCarEvent.SubmitWithImage) event;
            repository.addCarWithImage(e.name, e.model, e.number, e.lat, e.lon, e.imageUri, callback);
        } else if (event instanceof AddCarEvent.SubmitWithoutImage) {
            AddCarEvent.SubmitWithoutImage e = (AddCarEvent.SubmitWithoutImage) event;
            repository.addCarWithoutImage(e.name, e.model, e.number, e.lat, e.lon, callback);
        }
    }
}
