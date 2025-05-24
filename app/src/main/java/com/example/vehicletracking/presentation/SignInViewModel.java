package com.example.vehicletracking.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.vehicletracking.presentation.SignInResult;
import com.example.vehicletracking.domain.SignInUseCase;
import com.google.firebase.auth.AuthCredential;

public class SignInViewModel extends ViewModel {

    private final MutableLiveData<SignInState> state = new MutableLiveData<>(SignInState.idle());
    private final MutableLiveData<SignInEffect> effect = new MutableLiveData<>();

    private final SignInUseCase signInUseCase = new SignInUseCase();

    public LiveData<SignInState> getState() {
        return state;
    }

    public LiveData<SignInEffect> getEffect() {
        return effect;
    }

    public void processIntent(SignInIntent intent) {
        if (intent instanceof SignInIntent.SignInWithEmail) {
            SignInIntent.SignInWithEmail signIn = (SignInIntent.SignInWithEmail) intent;
            signInWithEmail(signIn.email, signIn.password);
        } else if (intent instanceof SignInIntent.SignInWithGoogle) {
            signInWithGoogle(((SignInIntent.SignInWithGoogle) intent).credential);
        }
    }

    private void signInWithEmail(String email, String password) {
        state.setValue(SignInState.loading());
        signInUseCase.signInWithEmail(email, password, result -> {
            if (result.success) {
                state.postValue(SignInState.success());
                effect.postValue(new SignInEffect.NavigateToMainScreen());
            } else {
                state.postValue(SignInState.error(result.message));
                effect.postValue(new SignInEffect.ShowToast(result.message));
            }
        });
    }

    private void signInWithGoogle(AuthCredential credential) {
        state.setValue(SignInState.loading());
        signInUseCase.signInWithGoogle(credential, result -> {
            if (result.success) {
                state.postValue(SignInState.success());
                effect.postValue(new SignInEffect.NavigateToMainScreen());
            } else {
                state.postValue(SignInState.error(result.message));
                effect.postValue(new SignInEffect.ShowToast(result.message));
            }
        });
    }
}
