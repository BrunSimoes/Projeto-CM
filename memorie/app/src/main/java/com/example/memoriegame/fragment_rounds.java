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

    ViewModelShare modelSh;
    private SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(requireContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rounds, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        modelSh = new ViewModelShare();

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
            sessionManager.setNJoca("0");
            sessionManager.setNCards("6");
            sessionManager.setNPower("2");
            sessionManager.setNColuna("3");
            sessionManager.setNRow("3");

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, MemorieGame_fg.class, null)
                    .commit();
        });
        round2 = view.findViewById(R.id.round2);
        round2.setOnClickListener( (v) -> {

            sessionManager.setNJoca("0");
            sessionManager.setNCards("10");
            sessionManager.setNPower("2");
            sessionManager.setNColuna("4");
            sessionManager.setNRow("3");

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, MemorieGame_fg.class, null)
                    .commit();
        });
        round3 = view.findViewById(R.id.round3);
        round3.setOnClickListener( (v) -> {

            sessionManager.setNJoca("1");
            sessionManager.setNCards("14");
            sessionManager.setNPower("4");
            sessionManager.setNColuna("4");
            sessionManager.setNRow("5");

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, MemorieGame_fg.class, null)
                    .commit();
        });
        round4 = view.findViewById(R.id.round4);
        round4.setOnClickListener( (v) -> {

            sessionManager.setNJoca("1");
            sessionManager.setNCards("16");
            sessionManager.setNPower("6");
            sessionManager.setNColuna("4");
            sessionManager.setNRow("6");

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, MemorieGame_fg.class, null)
                    .commit();
        });
    }
}
