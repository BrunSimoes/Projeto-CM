package com.example.memoriegame;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;


public class MemorieGame_fg extends Fragment {

    private TextView Score;
    private TextView Timer;
    private int nCartas = 20;
    private int nPower = 3;

    //CARTAS
    private int nLimMCards = 32;
    private int nLimmCards = 8;

    //OBJ
    private Cartas[] cartas = new Cartas[nCartas+nPower];
    private ImageView[] Imagens = new ImageView[nCartas+nPower];
    private ImageView[] ImagePower =  new ImageView[nPower];

    private Integer[] idPowerUps = new Integer[nPower];
    private LinearLayout powerDisplay;

    //Score
    private int score = 0;

    //Nivel
    private int nivel = 3;

    private int primeiraCarta, segundaCarta = 0;
    private int pos1Carta, pos2Carta;
    private int nClicks = 0;

    private int totalTime = 100000;
    private int intervalTime = 10;

    private int turno = 0;
    private Handler hand;

    private Timer counterTime = new Timer(Integer.MAX_VALUE);


    //TimeSpan
    private long timestampStart = SystemClock.elapsedRealtime();
    private long timestampLast = SystemClock.elapsedRealtime();

    //Funcao //f(x)=((1)/(1+ℯ^(-0.3 (-x+30))))+1 -> funcao logistica max/inicio_x2 - min/fim_x1
    private float declive   = 0.2f;
    private float transVert = 1.f;
    private float transHori = 30.f;
    private float salto     = 1.f;

    private int margem      = 2;

    private int posPower    = 0;

    //Button
    private ImageButton hamButton;


    //SESSION MANAGER
    private SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(requireContext());
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

        hamButton = view.findViewById(R.id.popupButton);
        hamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirDialog2();
            }
        });

        //
        powerDisplay = view.findViewById(R.id.powerDisplay);

        //create PowerCElls
        for(int i =0; i<nPower; i++) {
            ImagePower[i] = new ImageView(getActivity());
            int resourceId = getResources().getIdentifier("powerup" + i, "drawable", getActivity().getPackageName());

            ImagePower[i].setImageResource(resourceId);
            ImagePower[i].setId(View.generateViewId());
            powerDisplay.addView(ImagePower[i]);
        }





        //Iniciar Timer
        counterTime.iniciarContador();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int tempoAtual = counterTime.getTempoAtual();
                counterTime.checkLimM();
                Timer.setText(String.valueOf(tempoAtual));
                new Handler().postDelayed(this, 1000);
            }
        }, 1000);

        //carregar as imagens para o array
        loadImgs(view);
        associate();

        //shuffle mistura as imagens
        Collections.shuffle(Arrays.asList(cartas));

        getPowerCords();

        for(int i = 0; i < Imagens.length; i++) {
            final int pos = i;
            Log.d("bb", "imageView" + (i));

            Imagens[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nClicks == 0) {
                            primeiraCarta = cartas[pos].obterIdImage();

                            Imagens[pos].setClickable(false);
                            cartas[pos].setClicked(true);
                            pos1Carta=pos;
                            mostrarCarta(Imagens[pos],cartas[pos].obterIdImage());

                            if(checkPower(pos)) {
                                mPower();
                                posPower=pos;
                            }else{
                                nClicks++;
                            }


                    } else if (nClicks == 1) {
                        pos2Carta = pos;
                        segundaCarta = cartas[pos].obterIdImage();
                        Log.d("cega", String.valueOf(segundaCarta));

                        mostrarCarta(Imagens[pos], cartas[pos].obterIdImage());
                        Imagens[pos].setClickable(false);
                        cartas[pos].setClicked(true);

                        nClicks = 0;

                        if(checkPower(pos)) {
                            posPower=pos;
                            mPower();
                        }else{
                            unable();
                            Handler handler = new Handler();
                            handler.postDelayed((Runnable) () -> mecanica(), 750);
                        }
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

    private void associate(){
        //No Verificar se o numero de cartas Igual a Imagens
        //atribuição de uma Tag a Imagens para rodar a carta de forma automatica
        //quando a usar o power up de relevar
        if(cartas.length == Imagens.length) {
            for (int i = 0; i <cartas.length; i++) {
                cartas[i].setId((int) Imagens[i].getTag());
            }
        }else {
            Log.d("error","Carregamento de Cartas ou Elementos de Imagem Incorreto");
        }
    }

    private void loadImgs(View view){
        // Usando um ciclo para inicializar os TextViews com findViewById
        int aux  = 1;
        int auxB = 0;
        String card = "carta";

        for (int i = 0; i < nCartas + nPower; i++) {
                //Log.d("int",i+"");
                //Log.d("id", "imageView" + (i));

                int viewImageId = getResources().getIdentifier("imageView" + (i+1), "id", getActivity().getPackageName());
                Imagens[i] = view.findViewById(viewImageId);

                aux++;
                if(i%(nCartas/2)==0){
                    aux=1;
                }

                Log.d("int",aux+"");

                if (Imagens[i] != null) {
                    Imagens[i].setTag(i);
                    int resourceId = 0;
                        //Log.d("tag", i + "");
                        if(i < nCartas) {
                            resourceId = getResources().getIdentifier("carta" + aux, "drawable", getActivity().getPackageName());
                        }else if(i < nCartas + nPower){
                            Log.d("debug",""+i);
                            resourceId = getResources().getIdentifier("powerup" + aux, "drawable", getActivity().getPackageName());

                            if(aux==1){
                                card = "Tempo";
                            }else if(aux==2){
                                card = "Roda";
                            }else{
                                card = "2x";
                            }
                        }
                        //Log.d("bb", (i % 9) + "");

                        if (resourceId != 0) {
                            cartas[i] = new Cartas(resourceId, i,card);
                            Imagens[i].setImageResource(R.drawable.carta_fundo);
                            Imagens[i].setClickable(true);
                        }

                }
        }

        /*for(int i=0; i<nPower; i++){

        }*/
    }


    private boolean checkPower(int pos){
        boolean power = false;

        for(int i=0; i<idPowerUps.length; i++){
            if(pos==idPowerUps[i]){
                power=true;
                break;
            }
        }
        return power;
    }

    private void mPower(){
        unable();
        Handler handler = new Handler();
        handler.postDelayed((Runnable) () -> powerMec(),750);
        nClicks=0;
    }


    //mecanica -> em caso de as cartas não serem iguais rodar as cartas para o default
       //Oculta epanas as duas que foram ativadas previamente antes de perder um turno
       //Contador de Turnos
       //Set pontuação em função de tempo e numeros de pares feitos
       //Cartas selecionadas são desabilitadas(click), quando as duas selecionadas todas sõa desabilitadas(click)

    private void mecanica(){
        if(primeiraCarta == segundaCarta){
            turno++;
            calcScore();

            iniciarAnimacao(Imagens[pos1Carta], 1.0f, 0.1f);
            iniciarAnimacao(Imagens[pos2Carta], 1.0f, 0.1f);

            cartas[pos1Carta].desativar();
            cartas[pos2Carta].desativar();

            enable();
            Score.setText(String.valueOf(score));
        }else{
            turno++;
            ocultarCarta(Imagens[pos1Carta],R.drawable.carta_fundo);
            ocultarCarta(Imagens[pos2Carta],R.drawable.carta_fundo);
            enable();
        }

        String userName = sessionManager.getUserName();
        if(checkOver()){
            firebaseData.uploadScore(userName, nivel, score);
            exibirDialog();
            Log.d("Status","Game Over!");
        }
    }

    private void powerMec(){
        if(posPower!=pos1Carta){
            ocultarCarta(Imagens[pos1Carta],R.drawable.carta_fundo);
        }

        turno++;
        cartas[posPower].desativar();
        iniciarAnimacao(Imagens[posPower], 1.0f, 0.1f);
        enable();
    }

    private void unable(){
        for(int i=0; i<Imagens.length; i++){
            Imagens[i].setClickable(false);
        }
    }

    private void enable(){
        for(int i=0; i<Imagens.length; i++){
            if(cartas[i].obterStatus()){
                Imagens[i].setClickable(true);
            }
        }
    }
    private boolean checkOver() {
        boolean aux = true;

        for (int i = 0; i < Imagens.length; i++) {
            if (cartas[i].obterStatus()) {
                aux = false;
                break;
            }
        }

        return aux;
    }

    private void getPowerCords(){
        int a = 0;

        for(int i = 0; i<Imagens.length; i++){
            if(cartas[i].obterType()!="carta") {
                idPowerUps[a] = i;
                a++;
            }
        }
    }

    private void calcScore(){
        //f(x)=((2)/(1+ℯ^(-0.3 (-x+30))))
        float x = counterTime.getTempoAtual();
        float mult = (float) ((salto)/(1 + Math.pow(Math.E,(-1*declive)*(-x+transHori)))+transVert);
        float pulo = (float) (((90)/(1 + Math.pow(Math.E,(-0.2f)*(-turno+30))))+10); //70 jogadas devolve 10pts
        score += (int) pulo * mult;
    }

    private void exibirDialog() {
        // Cria um objeto de diálogo
        final Dialog dialog = new Dialog(getActivity());

        // Define o conteúdo do diálogo a partir do layout XML
        dialog.setContentView(R.layout.gameover_dialog);

        TextView Score     = dialog.findViewById(R.id.textScoreWrite);
        TextView Challenge = dialog.findViewById(R.id.textChallenge);
        TextView Time      = dialog.findViewById(R.id.textTimeWrite);

        Score.setText(String.valueOf(score));
        Time.setText(String.valueOf(counterTime.getTempoAtual()));
        Challenge.setText("Challenge " + 1);

        //Load Buttons
        Button restart = dialog.findViewById(R.id.button);
        Button exit    = dialog.findViewById(R.id.button2);

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartAll();
                dialog.dismiss();
            }
        });

        //Load ImgButton
        ImageButton cross = dialog.findViewById(R.id.imageButton);

        // Exibe o diálogo
        dialog.show();
    }

    private void exibirDialog2() {
        // Cria um objeto de diálogo
        final Dialog dialog = new Dialog(getActivity());

        // Define o conteúdo do diálogo a partir do layout XML
        dialog.setContentView(R.layout.menu_dialog);

        //Load Buttons
        Button restart = dialog.findViewById(R.id.button1);
        Button exit    = dialog.findViewById(R.id.button);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartAll();
                dialog.dismiss();
            }
        });

        //Load ImgButton
        ImageButton cross = dialog.findViewById(R.id.imageButton);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO METER EM PAUSA
                dialog.dismiss();
            }
        });

        // Exibe o diálogo
        dialog.show();
    }

    private void restartAll(){
        for(int i = 0; i<Imagens.length; i++){
           Imagens[i].setClickable(true);
           Imagens[i].setAlpha(1f);
           Imagens[i].setImageResource(R.drawable.carta_fundo);

           cartas[i].ativar();
           cartas[i].setClicked(false);
        }

        counterTime.reset();
        score = 0;

        Score.setText(String.valueOf(0));
        Timer.setText("00:00");

        Collections.shuffle(Arrays.asList(cartas));

        getPowerCords();

        Log.d("restart","restart");
    }


    private void iniciarAnimacao(ImageView elemento, float inicial, float fin) {
        AlphaAnimation animacao = new AlphaAnimation(inicial, fin);

        animacao.setDuration(500);

        animacao.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                elemento.setAlpha(fin);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        elemento.startAnimation(animacao);
    }
}