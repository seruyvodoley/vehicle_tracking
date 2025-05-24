package com.example.vehicletracking.domain;

import android.text.TextUtils;

import com.example.vehicletracking.models.userModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class SignUpUseCase {

    public interface Callback {
        void onSuccess(String message);
        void onFailure(String message);
    }

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void execute(String name, String email, String password, Callback callback) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            callback.onFailure("Заполните все поля");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uid = auth.getCurrentUser().getUid();
                userModel user = new userModel(name, email, password, uid);
                db.collection("Users").document(uid).set(user, SetOptions.merge())
                        .addOnSuccessListener(unused -> callback.onSuccess("Пользователь успешно зарегистрирован"))
                        .addOnFailureListener(e -> callback.onFailure("Ошибка сохранения: " + e.getMessage()));
            } else {
                callback.onFailure("Ошибка регистрации: " + task.getException().getMessage());
            }
        });
    }
}
