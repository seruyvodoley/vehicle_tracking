package com.example.vehicletracking.data;

import com.example.vehicletracking.domain.SignInUseCase;
import com.example.vehicletracking.presentation.SignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthCredential;

public class SignInRepository {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public void signInWithEmail(String email, String password, SignInUseCase.SignInCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onResult(new SignInResult(true, "Вход выполнен успешно"));
                    } else {
                        callback.onResult(new SignInResult(false, task.getException().getMessage()));
                    }
                });
    }

    public void signInWithGoogle(AuthCredential credential, SignInUseCase.SignInCallback callback) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onResult(new SignInResult(true, "Вход выполнен успешно"));
                    } else {
                        callback.onResult(new SignInResult(false, task.getException().getMessage()));
                    }
                });
    }
}
