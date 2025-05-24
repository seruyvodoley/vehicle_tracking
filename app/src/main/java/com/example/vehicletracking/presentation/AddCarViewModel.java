package com.example.vehicletracking.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vehicletracking.domain.AddCarUseCase;

public class AddCarViewModel extends ViewModel {

    private final AddCarUseCase useCase;

    private final MutableLiveData<AddCarState> _state = new MutableLiveData<>(AddCarState.initial());
    private final MutableLiveData<AddCarEffect> _effect = new MutableLiveData<>();

    public LiveData<AddCarState> state = _state;
    public LiveData<AddCarEffect> effect = _effect;

    public AddCarViewModel(AddCarUseCase useCase) {
        this.useCase = useCase;
    }

    public void onEvent(AddCarEvent event) {
        _state.setValue(new AddCarState(true, false, null));

        useCase.execute(event, result -> {
            if (result.isSuccess) {
                _state.postValue(new AddCarState(false, true, null));
                _effect.postValue(new AddCarEffect.ShowToast("Машина успешно добавлена"));
                _effect.postValue(new AddCarEffect.NavigateBack());
            } else {
                _state.postValue(new AddCarState(false, false, result.errorMessage));
                _effect.postValue(new AddCarEffect.ShowToast("Ошибка: " + result.errorMessage));
            }
        });
    }
}
