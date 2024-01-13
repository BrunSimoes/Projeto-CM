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
            "CREATE TABLE scores (id INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT, level TEXT, score TEXT, FOREIGN KEY(user) REFERENCES users(id));";


    public DatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createScores);
        db.execSQL(createUsers);
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

        // Inserting the new user into the 'users' table
        long result = db.insert("users", null, values);

        // Check if the insertion was successful
        if (result != -1) {
            // User registered successfully
            // You can add further actions or log messages here if needed
        } else {
            // Registration failed
            // You can handle the failure scenario here
        }

        db.close();
    }

    public void registerScore(String user, String level, String score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user", user);
        values.put("level", level);
        values.put("score", score);

        // Inserting the new user into the 'users' table
        long result = db.insert("users", null, values);

        // Check if the insertion was successful
        if (result != -1) {
            // User registered successfully
            // You can add further actions or log messages here if needed
        } else {
            // Registration failed
            // You can handle the failure scenario here
        }

        db.close();
    }

}
