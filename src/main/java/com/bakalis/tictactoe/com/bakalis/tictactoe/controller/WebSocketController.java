package com.bakalis.tictactoe.com.bakalis.tictactoe.controller;

import com.bakalis.tictactoe.models.GameState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class WebSocketController {


    private GameState state;

    @Autowired
    private SimpMessagingTemplate template;

    //This variable is used to limit the available players that can play the game at a point to 2.
    private int usersConnected = 0;

    @MessageMapping("/connect")
    @SendToUser("/queue/initialize")
    public GameState hello(@Payload String message){
        state = new GameState();
        //Gives the X mark to the first player to connect.
        if(usersConnected==0){
            state.setMark("X");
        //Gives the O mark to the second player to connect.
        }else if(usersConnected==1){
            state.setMark("O");
        //Gives a "blank" String to the other users for the Android app to determine that a game is played and//
        //no more users can connect.
        }else {
            state.setMark("blank");
        }
        usersConnected++;
        System.out.println("ASDF"+ usersConnected);
        //Sends a message to the matchset topic that the 2 users are connected and the match may begin.
        if(usersConnected==2){
            template.convertAndSend("/topic/matchset", "connected!");
        }
        //returns the appropriate initial Data to the users.
        return state;
    }




    @MessageMapping("/maingame")
    @SendTo("/topic/tictactoe")
    //Receives a message containing the Game State in JSON Format, makes a GameState object out of it and Broadcasts
    //it to the /topic/tictactoe topic, where the main game data go. Also checks if game is over.
    public GameState hai(@Payload String message){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            state = objectMapper.readValue(message, GameState.class);
            System.out.println(state.getGameState().toString());
            System.out.println(state.getMark());
            checkIfGameIsOver();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return state;
    }

    //Checks if either a win condition is satisfied or all available moves were made without one.
    public void checkIfGameIsOver(){
        boolean gameOver = false;
        String winner = "blank";
        //Column 1 all X
        if(state.getGameState().get(0).matches("X") && state.getGameState().get(1).matches("X") && state.getGameState().get(2).matches("X")){
            gameOver = true;
            winner = "X";
        }
        //Column 1 all O
        if(state.getGameState().get(0).matches("O") && state.getGameState().get(1).matches("O") && state.getGameState().get(2).matches("O")){
            gameOver = true;
            winner = "O";
        }
        //Column 2 all X
        if(state.getGameState().get(3).matches("X") && state.getGameState().get(4).matches("X") && state.getGameState().get(5).matches("X")){
            gameOver = true;
            winner = "X";
        }
        //Column 2 all O
        if(state.getGameState().get(3).matches("O") && state.getGameState().get(4).matches("O") && state.getGameState().get(5).matches("O")){
            gameOver = true;
            winner = "O";
        }
        //Column 3 all X
        if(state.getGameState().get(6).matches("X") && state.getGameState().get(7).matches("X") && state.getGameState().get(8).matches("X")){
            gameOver = true;
            winner = "X";
        }
        //Column 3 all O
        if(state.getGameState().get(6).matches("O") && state.getGameState().get(7).matches("O") && state.getGameState().get(8).matches("O")){
            gameOver = true;
            winner = "O";
        }
        //Row 1 all X
        if(state.getGameState().get(0).matches("X") && state.getGameState().get(3).matches("X") && state.getGameState().get(6).matches("X")){
            gameOver = true;
            winner = "X";
        }
        //Row 1 all O
        if(state.getGameState().get(0).matches("O") && state.getGameState().get(3).matches("O") && state.getGameState().get(6).matches("O")){
            gameOver = true;
            winner = "O";
        }
        //Row 2 all X
        if(state.getGameState().get(1).matches("X") && state.getGameState().get(4).matches("X") && state.getGameState().get(7).matches("X")){
            gameOver = true;
            winner = "X";
        }
        //Row 2 all O
        if(state.getGameState().get(1).matches("O") && state.getGameState().get(4).matches("O") && state.getGameState().get(7).matches("O")){
            gameOver = true;
            winner = "O";
        }
        //Row 3 all X
        if(state.getGameState().get(2).matches("X") && state.getGameState().get(5).matches("X")&& state.getGameState().get(8).matches("X")){
            gameOver = true;
            winner = "X";
        }
        //Row 3 all O
        if(state.getGameState().get(2).matches("O") && state.getGameState().get(5).matches("O") && state.getGameState().get(8).matches("O")){
            gameOver = true;
            winner = "O";
        }
        //Diagonal from row 1 column 1 to row 3 column 3 all X
        if(state.getGameState().get(0).matches("X") && state.getGameState().get(4).matches("X") && state.getGameState().get(8).matches("X")){
            gameOver = true;
            winner = "X";
        }
        //Diagonal from row 1 column 1 to row 3 column 3 all O
        if(state.getGameState().get(0).matches("O") && state.getGameState().get(4).matches("O")&& state.getGameState().get(8).matches("O")){
            gameOver = true;
            winner = "O";
        }
        //Diagonal from row 1 column 3 to row 3 column 1 all X
        if(state.getGameState().get(2).matches("X") && state.getGameState().get(4).matches("X") && state.getGameState().get(6).matches("X")){
            gameOver = true;
            winner = "X";
        }
        //Diagonal from row 1 column 3 to row 3 column 1 all O
        if(state.getGameState().get(2).matches("O") && state.getGameState().get(4).matches("O") && state.getGameState().get(6).matches("O")){
            gameOver = true;
            winner = "O";
        }
        //If all blocks contain an X or an O but none of the win conditions is satisfied
        if(!state.getGameState().contains("blank")){
            gameOver=true;
            winner="blank";
        }
        //Sends the appropriate message to the /isgameover topic, where the Android application waits to learn about a winner.
        //Resets the usersConnected variable for the server to be able to set a new game up.
        if(gameOver){
            System.out.println("Game Over!! "+winner);
            template.convertAndSend("/topic/isgameover", "{\"winner\": \""+winner+"\"}");
            usersConnected = 0;
        }
    }


}
