package com.example.memoriegame;

public class Cartas {

    private int imageId;
    private int id;
    private boolean status;

    //guardar Status dos elementos a serem dispostos no fragmento Jogo
    public Cartas(int imageId, int id){
        this.status  = true;
        this.imageId = imageId;
        this.id      = id;
    }

    //desativar Carta
    public void desativar(){
        this.status = false;
    }

    //ativar Carta
    public void ativar(){
        this.status = true;
    }

    public boolean obterStatus(){
        return status;
    }

    public int obterIdImage(){
        return imageId;
    }

    public int obterId(){
        return id;
    }
}
