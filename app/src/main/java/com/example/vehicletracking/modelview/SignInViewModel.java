package com.example.vehicletracking.modelview;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vehicletracking.models.userModel;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInViewModel extends ViewModel {

    private FirebaseAuth firebaseAuth;
    private MutableLiveData<String> loginStatus;

    public SignInViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
        loginStatus = new MutableLiveData<>();
    }

    public LiveData<String> getLoginStatus() {
        return loginStatus;
    }

    // Метод для входа через email и пароль
    public void signInWithEmailAndPassword(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            loginStatus.setValue("Введите email и пароль");
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loginStatus.setValue("Вход выполнен успешно");
                    } else {
                        loginStatus.setValue("Ошибка входа: " + task.getException().getMessage());
                    }
                });
    }

    // Метод для входа через Google
    public void signInWithGoogle(AuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loginStatus.setValue("Вход выполнен успешно");
                    } else {
                        loginStatus.setValue("Ошибка входа через Google: " + task.getException().getMessage());
                    }
                });
    }
}
