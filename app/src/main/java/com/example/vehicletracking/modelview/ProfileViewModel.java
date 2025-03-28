package com.example.vehicletracking.modelview;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vehicletracking.activities.MainActivity;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private MutableLiveData<String> profileStatus;

    public ProfileViewModel() {
        mAuth = FirebaseAuth.getInstance();
        profileStatus = new MutableLiveData<>();
    }

    public LiveData<String> getProfileStatus() {
        return profileStatus;
    }

    public void logOut(Activity activity) {
        mAuth.signOut();
        profileStatus.setValue("Выход из аккаунта выполнен");

        if (activity instanceof MainActivity) {
            ((MainActivity) activity).hideBottomNavigation();
        }
    }

    public void changePassword(String oldPassword, String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), oldPassword))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            updatePassword(newPassword);
                        } else {
                            profileStatus.setValue("Неверный старый пароль");
                        }
                    });
        } else {
            profileStatus.setValue("Пользователь не найден");
        }
    }

    // Метод для смены пароля
    private void updatePassword(String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            profileStatus.setValue("Пароль успешно изменен");
                        } else {
                            profileStatus.setValue("Не удалось изменить пароль");
                        }
                    });
        }
    }

    // Метод удаления пользователя
    public void deleteUser(Activity activity) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    profileStatus.setValue("Аккаунт успешно удален");
                    if (activity instanceof MainActivity) {
                        ((MainActivity) activity).hideBottomNavigation();
                    }
                } else {
                    profileStatus.setValue("Ошибка при удалении аккаунта");
                }
            });
        } else {
            profileStatus.setValue("Пользователь не аутентифицирован");
        }
    }
}
