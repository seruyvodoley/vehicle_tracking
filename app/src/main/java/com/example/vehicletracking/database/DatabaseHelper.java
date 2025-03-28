package com.example.vehicletracking.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_USERS = "users";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // 1. Создаем новую таблицу users_new с нужными полями
            db.execSQL("CREATE TABLE users_new (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE, " +
                    "email TEXT UNIQUE, " +  // Добавлен email
                    "password TEXT)");

            // 2. Копируем данные из старой таблицы (без email)
            db.execSQL("INSERT INTO users_new (id, username, password) " +
                    "SELECT id, username, password FROM users");

            // 3. Удаляем старую таблицу
            db.execSQL("DROP TABLE users");

            // 4. Переименовываем новую таблицу в users
            db.execSQL("ALTER TABLE users_new RENAME TO users");
        }
    }



    // **1. Вставка нового пользователя**
    public boolean insertUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;  // Если -1, значит ошибка
    }

    // **2. Получение пользователя по имени**
    public Cursor getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?";
        return db.rawQuery(query, new String[]{username});
    }
    public Cursor getPassword(String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_PASSWORD + " = ?";
        Log.d("DB Query", query);  // Логирование запроса
        return db.rawQuery(query, new String[]{password});
    }


    // **3. Обновление пароля пользователя**
    public boolean updateUserPassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int result = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{username});
        db.close();
        return result > 0;  // Если больше 0, значит обновление успешно
    }

    // **4. Удаление пользователя**
    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_USERS, COLUMN_EMAIL + " = ?", new String[]{username});
        db.close();
        return result > 0;  // Если больше 0, значит удаление успешно
    }
}
