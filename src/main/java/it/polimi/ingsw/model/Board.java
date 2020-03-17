package it.polimi.ingsw.model;

public class Board {

    private Space[][] board;

    public Board(){
        board = new Space[5][5];
    }

    public Space getSpace(int x, int y){
        return board[x][y];
    }
}
