package com.bakalis.tictactoe.models;

import java.util.ArrayList;

public class GameState {

    //A String Array that contains the current State of the blocks of the Game. Used values are  "X", "O" and "blank".
    private ArrayList<String> gameState;
    //The mark that users use to play with. Used values are  "X" and "O".
    private String mark;

    //Initializes the Array List with "blank" Strings.
    public GameState(){
        this.gameState = new ArrayList<String>();
        for(int i=0;i<9;i++){
            gameState.add("blank");
        }
    }

    //Getters and Setters.
    public ArrayList<String> getGameState() {
        return gameState;
    }

    public void setGameState(ArrayList<String> gameState) {
        this.gameState = gameState;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
