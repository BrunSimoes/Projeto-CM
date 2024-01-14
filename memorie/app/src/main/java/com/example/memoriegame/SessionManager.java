package com.example.memoriegame;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_EMAIL = "email";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String N_CARDS = "nCards";
    private static final String N_POWER = "nPower";
    private static final String N_JOCA = "Joca";
    private static final String N_COLUNAS = "nColunas";
    private static final String N_LINHAS = "nLinhas" ;


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    //USER ID
    public void setUserId(String userId) {
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }
    //EMAIL
    public void setUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }
    //USERNAME
    public void setUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }


    //LOGIN STATE
    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setNCards(String nCards) {
        editor.putString(N_CARDS, nCards);
        editor.apply();
    }
    public String getNCards() {
        return sharedPreferences.getString(N_CARDS, null);
    }

    public void setNPower(String nPower) {
        editor.putString(N_POWER, nPower);
        editor.apply();
    }
    public String getNPower() {
        return sharedPreferences.getString(N_POWER, null);
    }

    public void setNJoca(String nJoca) {
        editor.putString(N_JOCA, nJoca);
        editor.apply();
    }
    public String getNJoker() {
        return sharedPreferences.getString(N_JOCA, null);
    }

    public void setNColuna(String nColuna) {
        editor.putString(N_COLUNAS, nColuna);
        editor.apply();
    }
    public String getNColuna() {
        return sharedPreferences.getString(N_COLUNAS, null);
    }

    public void setNRow(String nRow) {
        editor.putString(N_LINHAS, nRow);
        editor.apply();
    }
    public String getNRow() {
        return sharedPreferences.getString(N_LINHAS, null);
    }



}

