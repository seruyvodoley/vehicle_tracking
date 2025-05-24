package com.example.vehicletracking.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vehicletracking.domain.SignUpUseCase;
import com.example.vehicletracking.presentation.SignUpIntent;
import com.example.vehicletracking.presentation.SignUpState;

public class SignUpViewModel extends ViewModel {

    private final SignUpUseCase signUpUseCase;
    private final MutableLiveData<SignUpState> state = new MutableLiveData<>(SignUpState.idle());

    public SignUpViewModel() {
        this.signUpUseCase = new SignUpUseCase();
    }

    public LiveData<SignUpState> getState() {
        return state;
    }

    public void processIntent(SignUpIntent intent) {
        if (intent instanceof SignUpIntent.Submit) {
            SignUpIntent.Submit submit = (SignUpIntent.Submit) intent;
            state.setValue(SignUpState.loading());

            signUpUseCase.execute(submit.name, submit.email, submit.password, new SignUpUseCase.Callback() {
                @Override
                public void onSuccess(String message) {
                    state.postValue(SignUpState.success(message));
                }

                @Override
                public void onFailure(String message) {
                    state.postValue(SignUpState.error(message));
                }
            });
        }
    }
}
