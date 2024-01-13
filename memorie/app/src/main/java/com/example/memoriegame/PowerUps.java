package com.example.memoriegame;

public class PowerUps {

    private int id;
    private int ImageId;
    private boolean status;
    private boolean disponibilidade;

    public PowerUps(int id) {
        this.id     = id;
        this.status = false;
        this.disponibilidade = false;
    }

    //Ativar power-up especifico
    private void ativarPower(){
      status = true;
    }

    //get Status do Power UP
    private boolean getStatus(){
        return status;
    }

    //Desativar Power UP
    private void desabilitarPower(){
        status = false;
    }


}
