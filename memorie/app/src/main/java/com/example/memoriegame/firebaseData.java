package com.example.memoriegame;

import android.content.Context;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class firebaseData {

    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public firebaseData(Context context, FirebaseFirestore db) {
        this.db = db;
    }

    //UPLOAD DATA FUNCTIONS
    public static void uploadScore(String username, int level, int score) {
        Map<String, Object> scoreMap = new HashMap<>();
        scoreMap.put("username", username);
        scoreMap.put("level", level);
        scoreMap.put("score", score);

        db.collection("scores").add(scoreMap);
    }
    public static void uploadUser(String email, String username, String password) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("username", username);
        userMap.put("password", password);

        db.collection("users").add(userMap);
    }


    //GET DATA FUNCTIONS
    static void getAllScores() {
        db.collection("scores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            List<String> scoreDataList = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                String email = document.getString("user");
                                String level = document.getString("level");
                                String score = document.getString("score");

                                scoreDataList.add("email: " + email + ", level: " + level + ", score: " + score);
                            }

                            for (String scoreData : scoreDataList) {
                                Log.d("FirestoreData", scoreData);
                            }

                        } else {
                            Log.w("FirestoreData", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}