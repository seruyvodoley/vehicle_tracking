package com.example.vehicletracking.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.vehicletracking.domain.AddCarUseCase;
import com.example.vehicletracking.presentation.AddCarViewModel;

public class AddCarViewModelFactory implements ViewModelProvider.Factory {

    private final AddCarUseCase useCase;

    public AddCarViewModelFactory(AddCarUseCase useCase) {
        this.useCase = useCase;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddCarViewModel.class)) {
            return (T) new AddCarViewModel(useCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
