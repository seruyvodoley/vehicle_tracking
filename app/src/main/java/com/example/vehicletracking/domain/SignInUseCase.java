package com.example.vehicletracking.domain;

import com.example.vehicletracking.data.SignInRepository;
import com.example.vehicletracking.presentation.SignInResult;
import com.google.firebase.auth.AuthCredential;

public class SignInUseCase {

    private final SignInRepository repository = new SignInRepository();

    public void signInWithEmail(String email, String password, SignInCallback callback) {
        repository.signInWithEmail(email, password, callback);
    }

    public void signInWithGoogle(AuthCredential credential, SignInCallback callback) {
        repository.signInWithGoogle(credential, callback);
    }

    public interface SignInCallback {
        void onResult(SignInResult result);
    }
}
