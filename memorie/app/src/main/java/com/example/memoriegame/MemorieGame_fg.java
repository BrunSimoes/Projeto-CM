package com.example.memoriegame;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
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
    private final int nCartas = 16;
    private  final int nPower = 6;

    //CARTAS
    private final int nLimMCards = 32;
    private final int nLimmCards = 8;

    private int combo = 0;
    private int multiplayer = 1;

    //OBJ
    private Cartas[] cartas = new Cartas[nCartas+nPower+1];
    private ImageView[] Imagens = new ImageView[nCartas+nPower+1];
    private ImageView[] ImagePower =  new ImageView[nPower/2];
    private PowerUps[] powers = new PowerUps[nPower/2];

    private String[] Power = {"Tempo","Roda","2x"};

    private int jokerPos = 0;
    private LinearLayout powerDisplay;

    //Score
    private int score = 0;

    //Nivel
    private int nivel = 3;

    private int primeiraCarta, segundaCarta = 0;
    private int pos1Carta, pos2Carta;
    private int nClicks = 0;


    private int turno = 0;
    private Handler hand;

    private Timer counterTime = new Timer(Integer.MAX_VALUE);



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

        loadPower(view);

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

        getJokerCord();

        for(int i = 0; i < Imagens.length; i++) {
            final int pos = i;
            Log.d("bb", "imageView" + (i));

            Imagens[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("erororo", nClicks+"");
                     setCarta(pos);
                            if (nClicks == 0) {
                                    if(checkJoker(pos)) {
                                        mPower();
                                        posPower=pos;
                                    }else{
                                        nClicks++;
                                    }


                            } else if (nClicks == 1) {
                                nClicks = 0;
                                Log.d("erororo", "pila");

                                if(checkJoker(pos)) {
                                    posPower=pos;
                                    mPower();
                                }else{
                                    runMec();
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
        int viewImageId = 0;
        int resourceId = 0;
        String card = "carta";

        for (int i = 0; i < nCartas; i++) {
                //Log.d("int",i+"");
                //Log.d("id", "imageView" + (i));
                 Log.d("i",i+"");

                viewImageId = getResources().getIdentifier("imageView" + (i+1), "id", getActivity().getPackageName());
                Imagens[i] = view.findViewById(viewImageId);

                aux++;
                    if(i%(nCartas/2)==0){
                        aux=1;
                    }

                Log.d("int",aux+"");

                if (Imagens[i] != null) {
                    Imagens[i].setTag(i);
                        resourceId = getResources().getIdentifier("carta" + aux, "drawable", getActivity().getPackageName());

                        if (resourceId != 0) {
                            cartas[i] = new Cartas(resourceId, i,card);
                            Imagens[i].setImageResource(R.drawable.carta_fundo);
                            Imagens[i].setClickable(true);
                        }

                }
        }

        aux=0;

        for (int i = nCartas; i < nCartas+nPower; i++) {
                Log.d("i",i+"");
                viewImageId = getResources().getIdentifier("imageView" + (i+1), "id", getActivity().getPackageName());
                Imagens[i] = view.findViewById(viewImageId);
                Imagens[i].setTag(i);

                aux++;
                if((nCartas+nPower/2)==i){
                    aux=1;
                }

                resourceId = 0;
                Log.d("debug",""+aux);
                resourceId = getResources().getIdentifier("powerup" + aux, "drawable", getActivity().getPackageName());

                if(aux==1){
                    card = "Tempo";
                }else if(aux==2){
                    card = "Roda";
                }else{
                    card = "2x";
                }

                if (resourceId != 0) {
                    cartas[i] = new Cartas(resourceId, i,card);
                    Imagens[i].setImageResource(R.drawable.carta_fundo);
                    Imagens[i].setClickable(true);
                }
            }

                card = "joker";
                int where = nCartas+nPower+1;
                Log.d("i",where-1+"");

                viewImageId = getResources().getIdentifier("imageView" + (where), "id", getActivity().getPackageName());
                Imagens[where-1] = view.findViewById(viewImageId);
                Imagens[where-1].setTag(where-1);
                resourceId = getResources().getIdentifier("powerup4", "drawable", getActivity().getPackageName());
                cartas[where-1] = new Cartas(resourceId, where-1,card);
                Imagens[where-1].setImageResource(R.drawable.carta_fundo);
                Imagens[where-1].setClickable(true);

        }

    private boolean checkJoker(int pos){
        boolean power = false;

            if(pos== jokerPos){
                power=true;
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
            combo++;
            calcScore();

            iniciarAnimacao(Imagens[pos1Carta], 1.0f, 0.1f);
            iniciarAnimacao(Imagens[pos2Carta], 1.0f, 0.1f);

            cartas[pos1Carta].desativar();
            cartas[pos2Carta].desativar();

            for(int i = 0; i<Power.length; i++){
                if(Power[i] == cartas[pos1Carta].obterType() && powers[i].obterType() == Power[i]){
                    powers[i].ativarPower();
                    updateSat(ImagePower[i],1.f, powers[i].obterImageId());
                }
            }

            enable();
            Score.setText(String.valueOf(score));
        }else{
            combo = 0;
            turno++;
            multiplayer = 1;
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

        score-=10;
        Score.setText(String.valueOf(score));

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

    private void getJokerCord(){
        int a = 0;

        for(int i = 0; i<Imagens.length; i++){
            if(cartas[i].obterType()=="joker") {
                jokerPos = i;
            }
        }
    }

    private void calcScore(){
        //f(x)=((2)/(1+ℯ^(-0.3 (-x+30))))
        float x = counterTime.getTempoAtual();
        float mult = logistica(1f,-0.2f,counterTime.getTempoAtual(),30f,1f);
        float pulo = logistica(90,-0.2f,-1*turno,30,10);
        score += (int) (pulo * mult)*multiplayer;
    }

    private float logistica(float salto , float declive, float x, float transH, float transV){
        float value = (float) ((salto)/(1 + Math.pow(Math.E,(declive)*(-x+transH)))+transV);
        return value;
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

        getJokerCord();

        for(int i = 1; i<=(nPower/2); i++) {
            ImagePower[i].setAlpha(1f);
            powers[i].ativarPower();
            ImagePower[i].setClickable(true);
            updateSat(ImagePower[i],0.f, powers[i].obterImageId());
        }

        Log.d("restart","restart");
    }

    private void loadPower(View view){
        int resourceId = 0 ;


            for(int i = 1; i<=(nPower/2); i++) {
                resourceId = getResources().getIdentifier("imageViewPower" + i, "id", getActivity().getPackageName());
                if(resourceId != 0) {
                    ImagePower[i - 1] = view.findViewById(resourceId);
                    ImagePower[i - 1].setTag(i);

                    int idDraw = setSat( ImagePower[i - 1]);
                    updateSat(ImagePower[i - 1], 0.f,idDraw);

                    powers[i-1] = new PowerUps(Power[i-1], i,idDraw);
                    int posP = i-1;
                            ImagePower[i - 1].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    boolean ui = false;
                                    if(powers[posP].getStatus()) {
                                        if (1 == (int) ImagePower[posP].getTag()) {
                                            counterTime.reduceTime(5);
                                            iniciarAnimacao(ImagePower[posP], 1f, 0.1f);
                                        }

                                        if (2 == (int) ImagePower[posP].getTag() && nClicks==1) {
                                        findCard();
                                        nClicks = 0;
                                        iniciarAnimacao(ImagePower[posP], 1f, 0.1f);
                                        }else{
                                            iniciarAnimacao2(ImagePower[posP], 1f, 0.1f);
                                            ui=true;
                                        }

                                        if (3 == (int) ImagePower[posP].getTag()) {
                                            multiplayer = 2;
                                            iniciarAnimacao(ImagePower[posP],1f,0.1f);
                                        }
                                        
                                        if(!ui) {
                                            ImagePower[posP].setClickable(false);
                                            powers[posP].desabilitarPower();
                                        }
                                    }
                                }
                            });
                }
            }
        }

        private void findCard(){
            int fundPos = 0;

            for(int i = 0; i<cartas.length; i++){
                if(cartas[i].obterIdImage() == primeiraCarta && cartas[i].obterId() != cartas[pos1Carta].obterId()){
                    fundPos = i;
                }
            }

            Log.d("cartasID",cartas[fundPos].obterId()+","+cartas[pos1Carta].obterId());

            setCarta(fundPos);
            runMec();
        }

        private void runMec(){
            unable();
            Handler handler = new Handler();
            handler.postDelayed((Runnable) () -> mecanica(), 750);
        }

        private void setCarta(int pos){
            if(nClicks==0){
                pos1Carta = pos;
                nClicks = 0;
                primeiraCarta = cartas[pos].obterIdImage();
            }else if(nClicks==1){
                pos2Carta = pos;
                segundaCarta = cartas[pos].obterIdImage();
                Log.d("erororo", "picha");
            }
            Imagens[pos].setClickable(false);
            cartas[pos].setClicked(true);
            mostrarCarta(Imagens[pos],cartas[pos].obterIdImage());
        }


    private int setSat(ImageView image){
        int resourceDraw = 0;
        int i  = (int) image.getTag();

        // Obtenha o Drawable do XML
        resourceDraw = getResources().getIdentifier("powerup" + i +"_d", "drawable", getActivity().getPackageName());

        return resourceDraw;
    }

    private void updateSat(ImageView image,float sat,int id){
        Drawable drawable = getResources().getDrawable(id);

        float saturacaoDesejada = sat;

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(saturacaoDesejada);

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        drawable.setColorFilter(colorFilter);

        image.setImageDrawable(drawable);
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

    private void iniciarAnimacao2(ImageView elemento, float inicial, float fin) {
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(elemento, "rotation", 0f, 5f, 0f, -5f, 0f);
        rotationAnimator.setDuration(100);
        rotationAnimator.setRepeatCount(0);
        rotationAnimator.start();
    }
}