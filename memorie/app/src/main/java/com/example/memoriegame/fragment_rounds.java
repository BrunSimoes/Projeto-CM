package com.example.memoriegame;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.Nullable;

public class fragment_rounds extends Fragment {
    ImageView leaderboard;
    Button round1, round2, round3, round4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rounds, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //NAVBAR
        leaderboard = view.findViewById(R.id.leaderboard);
        leaderboard.setOnClickListener( (v) -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment_leaderboard.class, null)
                    .commit();
        });


        //ROUND BUTTONS
        round1 = view.findViewById(R.id.round1);
        round1.setOnClickListener( (v) -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, MemorieGame_fg.class, null)
                    .commit();
        });
        round2 = view.findViewById(R.id.round2);
        round1.setOnClickListener( (v) -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, MemorieGame_fg.class, null)
                    .commit();
        });
        round3 = view.findViewById(R.id.round4);
        round1.setOnClickListener( (v) -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, MemorieGame_fg.class, null)
                    .commit();
        });
        round4 = view.findViewById(R.id.round3);
        round1.setOnClickListener( (v) -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, MemorieGame_fg.class, null)
                    .commit();
        });
    }

}
