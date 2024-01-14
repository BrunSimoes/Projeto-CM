package com.example.memoriegame;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //ESTRUTURA SQL
    private static final String databaseName = "data.db";
    private static final int databaseVersion = 1;

    private static final String createUsers =
            "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, username TEXT, password TEXT);";

    private static final String createScores =
            "CREATE TABLE scores (user TEXT, level TEXT, score TEXT);";


    public DatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUsers);
        db.execSQL(createScores);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void registerUser(String email, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("username", username);
        values.put("password", password);

        long result = db.insert("users", null, values);

        // Check if the insertion was successful
        if (result != -1) {
            // User registered successfully
        } else {
            // Registration failed
        }

        db.close();
    }

    public void registerScore(String user, String level, String score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user", user);
        values.put("level", level);
        values.put("score", score);

        long result = db.insert("scores", null, values);
        if (result != -1) {
        } else {
        }

        db.close();
    }

}
