package com.example.memoriegame;

public class PowerUps {

    private int id;
    private int ImageId;
    private boolean status;
    private boolean disponibilidade;
    private int idImage;
    private String type;

    public PowerUps(String type, int id, int idImage) {
        this.id     = id;
        this.status = false;
        this.disponibilidade = false;
        this.type = type;
        this.idImage = idImage;
    }

    //Ativar power-up especifico
    public void ativarPower(){
      status = true;
    }

    //get Status do Power UP
    public boolean getStatus(){
        return status;
    }
    public String obterType() { return type;}
    public int obterImageId(){
        return idImage;
    }

    //Desativar Power UP
    public void desabilitarPower(){
        status = false;
    }


}
