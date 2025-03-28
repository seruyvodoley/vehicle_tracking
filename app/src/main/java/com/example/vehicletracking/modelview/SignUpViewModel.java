package com.example.vehicletracking.modelview;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vehicletracking.models.userModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class SignUpViewModel extends ViewModel {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private MutableLiveData<String> registrationStatus;

    public SignUpViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        registrationStatus = new MutableLiveData<>();
    }

    public LiveData<String> getRegistrationStatus() {
        return registrationStatus;
    }

    // Метод для регистрации пользователя
    public void signUpUser(String name, String email, String password) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            registrationStatus.setValue("Заполните все поля");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String userId = firebaseAuth.getCurrentUser().getUid();
                userModel model = new userModel(name, email, password, userId);
                firestore.collection("Users").document(userId).set(model, SetOptions.merge()).addOnSuccessListener(unused -> {
                    registrationStatus.setValue("Пользователь успешно зарегистрирован");
                }).addOnFailureListener(e -> {
                    registrationStatus.setValue("Ошибка регистрации: " + e.getMessage());
                });
            } else {
                registrationStatus.setValue("Ошибка регистрации: " + task.getException().getMessage());
            }
        });
    }
}
