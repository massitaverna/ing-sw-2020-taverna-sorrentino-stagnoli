package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {

    private Space[][] board;

    public Board(){
        board = new Space[5][5];
    }

    public Space getSpace(Coord c){
        //TODO: Check coordinates c are valid
        return board[c.x][c.y];
    }

    public ArrayList<Space> getUnoccupiedSpaces(){

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

    //potrebbero essere messe nel controller
/*    public Map<Coord, Space> getMovableSpacesAround(Coord c, int maxDiff){
        //TODO: Check coordinates c are valid
        Map<Coord, Space> result = new HashMap<>();
        for (int i = -1; i < 1; i++) {
            for (int j = -1; j < 1; j++) {
                if (i==0 && j==0)
                    continue;

                //if(space not occupied/dome && Rules.CheckSomething) ??
                if(!this.board[c.x + i][c.y + j].getOccupied() &&
                        !this.board[c.x + i][c.y + j].isDome() &&
                        (this.board[c.x + i][c.y + j].height.ordinal() - this.board[c.x][c.y].height.ordinal() <= maxDiff) ){

                    result.put(new Coord(c.x + i, c.y + j), this.board[c.x + i][c.y + j]);
                }
            }
        }

        return result;
    }

    public Map<Coord, Space> getBuildableSpacesAround(Coord c){
        //TODO: Check coordinates c are valid
        Map<Coord, Space> result = new HashMap<>();
        for (int i = -1; i < 1; i++) {
            for (int j = -1; j < 1; j++) {
                if (i==0 && j==0)
                    continue;

                //if(space not occupied/dome && Rules.CheckSomething) ??
                if(!this.board[c.x + i][c.y + j].getOccupied() &&
                        !this.board[c.x + i][c.y + j].isDome() ){

                    result.put(new Coord(c.x + i, c.y + j), this.board[c.x + i][c.y + j]);
                }
            }
        }

        return result;
    }*/
}
