package com.example.memoriegame;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;


public class MemorieGame_fg extends Fragment {

    private TextView Score;
    private TextView Timer;
    private int nImages = 18;
    private int nLimMCards = 32;
    private int nLimmCards = 8;
    private ImageView[] Imagens = new ImageView[nImages];
    private Integer[] ordemCartas= new Integer[nImages];

    //Score
    private int score = 0;

    private int primeiraCarta, segundaCarta = 0;
    private int pos1Carta, pos2Carta;
    private int nClicks = 0;

    private int totalTime = 100000;
    private int intervalTime = 10;

    private int turno = 0;


    //TimeSpan
    private long timestampStart = SystemClock.elapsedRealtime();
    private long timestampLast = SystemClock.elapsedRealtime();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_memorie_game_fg, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //carregar textViews
        Score = view.findViewById(R.id.ScoreText);
        Score.setText(String.valueOf(0));

        Timer = view.findViewById(R.id.TimerText);
        Timer.setText("00:00");

        CountDownTimer countDownTimer = new CountDownTimer(totalTime, intervalTime) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Atualize o TextView a cada intervalo
                long secondsRemaining = millisUntilFinished * 1000;
                Timer.setText("Tempo: " + secondsRemaining);
            }

            @Override
            public void onFinish() {

            }
        };

        // Inicie o temporizador
        countDownTimer.start();

        //carregar as imagens para o array
        loadImgs(view);

        //shuffle mistura as imagens
        Collections.shuffle(Arrays.asList(ordemCartas));

        for(int i = 0; i < Imagens.length; i++) {
            final int pos = i;
            Log.d("bb", "imageView" + (i));

            Imagens[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nClicks == 0) {
                        primeiraCarta = ordemCartas[pos];
                        Log.d("prima", String.valueOf(primeiraCarta));
                        nClicks++;

                        Imagens[pos].setClickable(false);
                        mostrarCarta(Imagens[pos],ordemCartas[pos]);

                        pos1Carta=pos;

                    } else if (nClicks == 1) {
                        segundaCarta = ordemCartas[pos];
                        Log.d("cega", String.valueOf(segundaCarta));

                        mostrarCarta(Imagens[pos],ordemCartas[pos]);
                        Imagens[pos].setClickable(false);

                        nClicks=0;
                        pos2Carta=pos;

                        unable();

                        Handler handler = new Handler();
                        handler.postDelayed((Runnable) () -> mecanica(),750);
                    }
                }
            });
        }

    }

    private void mostrarCarta(ImageView iV, int id){
       iV.setImageResource(id);
    }
    private void ocultarCarta(ImageView iV, int id){
       iV.setImageResource(id);
    }

    private void setScore(){

    }

    private void loadImgs(View view){
        // Usando um ciclo para inicializar os TextViews com findViewById
        int aux = 1;

        for (int i = 0; i < 18; i++) {
                Log.d("int",i+"");
                Log.d("id", "imageView" + (i));

                int viewImageId = getResources().getIdentifier("imageView" + (i+1), "id", getActivity().getPackageName());
                Imagens[i] = view.findViewById(viewImageId);

                aux++;
                if(i%9==0){
                    aux=1;
                }

                Log.d("int",aux+"");

                if (Imagens[i] != null) {
                    Imagens[i].setTag(i);
                    Log.d("tag",i+"");
                    int resourceId = getResources().getIdentifier("placeholder_0" + aux, "drawable", getActivity().getPackageName());
                    Log.d("bb", (i % 9)+"");

                    if (resourceId != 0) {
                        ordemCartas[i] = resourceId;
                        Imagens[i].setImageResource(R.drawable.cartaoculta);
                        Imagens[i].setClickable(true);
                    }
                }
            }
    }

    private void setTurn(){

    }

    private void setDialog(){

    }


    //mecanica -> em caso de as cartas não serem iguais rodar as cartas para o default
       //Oculta epanas as duas que foram ativadas previamente antes de perder um turno
       //Contador de Turnos
       //Set pontuação em função de tempo e numeros de pares feitos
       //Cartas selecionadas são desabilitadas(click), quando as duas selecionadas todas sõa desabilitadas(click)

    private void mecanica(){
        if(primeiraCarta == segundaCarta){
            score++;
            Imagens[pos1Carta].setVisibility(View.INVISIBLE);
            Imagens[pos2Carta].setVisibility(View.INVISIBLE);
            enable();
            Score.setText(String.valueOf(score));
        }else{
            turno++;
            ocultarCarta(Imagens[pos1Carta],R.drawable.cartaoculta);
            ocultarCarta(Imagens[pos2Carta],R.drawable.cartaoculta);
            enable();
        }

        if(checkOver()){
            Log.d("Status","Game Over!");
        }
    }

    private void unable(){
        for(int i=0; i<Imagens.length; i++){
            Imagens[i].setClickable(false);
        }
    }

    private void enable(){
        for(int i=0; i<Imagens.length; i++){
            Imagens[i].setClickable(true);
        }
    }
    private boolean checkOver(){
        boolean aux = true;

        for(int i=0; i<Imagens.length; i++){
            if(Imagens[i].getVisibility() != View.INVISIBLE){
                aux = false;
                break;
            }
        }

        return aux;
    }
}