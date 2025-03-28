package com.example.vehicletracking.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DatabaseTest {
    private DatabaseHelper dbHelper;

    public DatabaseTest(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void runTests() {
        Log.d("DB_TEST", "=== ТЕСТИРОВАНИЕ CRUD ОПЕРАЦИЙ ===");

        // Создание пользователя
        createUserTest();

        // Чтение пользователя
        readUserTest();

        // Обновление пароля
        updatePasswordTest();

        // Удаление пользователя
        deleteUserTest();
    }

    private void createUserTest() {
        boolean result = dbHelper.insertUser("testUser", "testuser@example.com", "password123");
        Log.d("DB_TEST", "CREATE: " + (result ? "УСПЕШНО" : "ОШИБКА"));
    }

    private void readUserTest() {
        Cursor cursor = dbHelper.getUser("testUser");

        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            Log.d("DB_TEST", "READ: Найден пользователь - " + username + " / " + password);
            cursor.close();
        } else {
            Log.d("DB_TEST", "READ: Пользователь не найден");
        }
    }

    private void updatePasswordTest() {
        boolean result = dbHelper.updateUserPassword("testUser", "newPassword456");
        Log.d("DB_TEST", "UPDATE: " + (result ? "УСПЕШНО" : "ОШИБКА"));
    }

    private void deleteUserTest() {
        boolean result = dbHelper.deleteUser("testUser");
        Log.d("DB_TEST", "DELETE: " + (result ? "УСПЕШНО" : "ОШИБКА"));
    }
}
