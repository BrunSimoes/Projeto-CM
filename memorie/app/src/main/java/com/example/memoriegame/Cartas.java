package com.example.memoriegame;

public class Cartas {

    private int imageId;
    private int id;
    private boolean status;
    private boolean clicked;
    private int nCard;
    private int idPar;

    //guardar Status dos elementos a serem dispostos no fragmento Jogo
    public Cartas(int imageId, int id){
        this.status  = true;
        this.imageId = imageId;
        this.id      = id;
        this.clicked = false;
        this.idPar    = 0;
    }

    //desativar Carta
    public void desativar(){
        this.status = false;
    }

    //ativar Carta
    public void ativar(){
        this.status = true;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setImageId(int imageId){
        this.imageId = imageId;
    }

    public void setClicked(boolean clicked){
        this.clicked = clicked;
    }

    public void setIdPar(int idPar){
        this.idPar = idPar;
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
