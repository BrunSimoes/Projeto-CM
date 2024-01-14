package com.example.memoriegame;

import androidx.lifecycle.ViewModel;

public class ViewModelShare extends ViewModel{
    private int nCards = 0;
    private int nPower = 0;
    private boolean joker = false;

    public void setNCards(int nCards){
        this.nCards=nCards;
    }
    public void setNPower(int nPower){
        this.nPower=nPower;
    }

    public void setJoker(boolean joker){
        this.joker= joker;
    }

    public int getNCards(){
        return nCards;
    }
    public int getNPower(){
        return nPower;
    }

    public boolean getJoker(){
        return joker;
    }


}
