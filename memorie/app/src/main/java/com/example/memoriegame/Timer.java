package com.example.memoriegame;

import android.os.Handler;

public class Timer {

    private int tempoAtual;
    private Handler handler;
    private int limM;
    private Runnable runnable;

    public Timer(int limM) {
        this.limM = limM;
        this.tempoAtual = 0;
        this.handler = new Handler();
        this.runnable = new Runnable() {
            @Override
            public void run() {
                tempoAtual++;
                handler.postDelayed(this, 1000);
            }
        };
    }

    public void iniciarContador() {
        handler.postDelayed(runnable, 1000); // Inicia o runnable após 1 segundo
    }

    public void checkLimM(){
        if(tempoAtual>=limM){
            pararContador();
        }
    }

    // Obter o tempo atual
    public int getTempoAtual() {
        return tempoAtual;
    }

    // Parar o contador
    public void pararContador() {
        handler.removeCallbacks(runnable);
    }
}