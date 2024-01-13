package com.example.memoriegame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

import android.widget.Button;


public class fragment_welcome extends Fragment {
    Button playButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playButton = view.findViewById(R.id.play_button);
        playButton.setOnClickListener( (v) -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment_menu.class, null)
                    .commit();
        });
    }

}
