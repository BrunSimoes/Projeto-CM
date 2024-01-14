package com.example.memoriegame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

public class fragment_leaderboard extends Fragment {

    Button rounds;

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "FirestoreExample";

    private List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
    private LeaderboardAdapter adapter;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(requireContext());

        db.collection("scores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String username = document.getString("username");
                                int level = document.getLong("level").intValue();
                                int score = document.getLong("score").intValue();

                                ScoreEntry scoreEntry = new ScoreEntry(username, level, score);
                                leaderboardEntries.add(new LeaderboardEntry(scoreEntry.getEmail(), scoreEntry.getScore()));
                            }

                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e(TAG, "Error getting documents", task.getException());
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LeaderboardAdapter(leaderboardEntries);
        recyclerView.setAdapter(adapter);

        //MUDAR PARA ROUNDS
        rounds = view.findViewById(R.id.rounds);
        rounds.setOnClickListener( (v) -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment_rounds.class, null)
                    .commit();
        });
    }

    private static class ScoreEntry {
        private String username;
        private int level;
        private int score;

        public ScoreEntry(String username, int level, int score) {
            this.username = username;
            this.level = level;
            this.score = score;
        }

        public String getEmail() {
            return username;
        }

        public int getLevel() {
            return level;
        }

        public int getScore() {
            return score;
        }
    }
}
