package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Board {

    private Space[][] board;

    public Board(){
        board = new Space[5][5];
    }

    public Space getSpace(int x, int y){
        return board[x][y];
    }

    public ArrayList<Space> checkUnoccupiedSpaces(){

        ArrayList<Space> unoccupiedSpaces = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(!board[i][j].getOccupied()){
                    unoccupiedSpaces.add(board[i][j]);
                }
            }
        }

        return unoccupiedSpaces;
    }
}
