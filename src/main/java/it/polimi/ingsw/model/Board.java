package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private Space[][] board;
    private List<Worker> workers;

    public Board(){
        this.board = new Space[5][5];
        this.workers = new ArrayList<>();
    }

    public Space getSpace(Coord c){
        //TODO: Check coordinates c are valid
        return board[c.x][c.y];
    }

    public void addWorker(Worker w){
        this.workers.add(w);
    }

    public Worker[] getAllWorkers(){
        return (Worker[])this.workers.toArray();
    }


    public ArrayList<Space> getUnoccupiedSpaces(){

        ArrayList<Space> unoccupiedSpaces = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(!board[i][j].isOccupied()){
                    unoccupiedSpaces.add(board[i][j]);
                }
            }
        }

        return unoccupiedSpaces;
    }

    // potrebbero essere messe nel controller, perchè qua in mezzo ci andrà anche la
    // logica degli effetti delle divinità
    public Map<Coord, Space> getMovableSpacesAround(Coord c, int maxDiff){
        //TODO: Check coordinates c are valid
        Map<Coord, Space> result = new HashMap<>();
        for (int i = -1; i < 1; i++) {
            for (int j = -1; j < 1; j++) {
                if (i==0 && j==0)
                    continue;

                //if(space not occupied/dome && Rules.CheckSomething) ??
                if(!this.board[c.x + i][c.y + j].isOccupied() &&
                        !this.board[c.x + i][c.y + j].isDome() &&
                        (this.board[c.x + i][c.y + j].height.ordinal() - this.board[c.x][c.y].height.ordinal()
                                <= maxDiff
                        )
                ) {
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
                if(!this.board[c.x + i][c.y + j].isOccupied() &&
                        !this.board[c.x + i][c.y + j].isDome() ){

                    result.put(new Coord(c.x + i, c.y + j), this.board[c.x + i][c.y + j]);
                }
            }
        }

        return result;
    }

    public void workerMove(Worker w, Coord newPos) throws Exception{
        //TODO: Check newPos is valid
        //TODO: Check that worker w in in the list of workers
        Space currentSpace = this.board[w.getPosition().x][w.getPosition().y];
        Space newSpace = this.board[newPos.x][newPos.y];
        if(!currentSpace.isOccupied() && !(newSpace.getHeight() == Level.DOME)) {
            // placeWorker(w, newSpace); // Sostituisce le seguenti istruzioni commentate
            w.setPosition(newPos);
            newSpace.setOccupied();
            currentSpace.setUnoccupied();
        }
        else {
            throw new Exception();
        }
    }

    public void workerBuild(Worker w, Coord buildPos) throws Exception{
        //TODO: Check newPos is valid
        //TODO: Check that worker w in in the list of workers
        if(!this.board[buildPos.x][buildPos.y].isOccupied() &&
                this.board[buildPos.x][buildPos.y].getHeight() != Level.DOME) {
            this.board[buildPos.x][buildPos.y].levelUp();
        }
        else{
            throw new Exception();
        }
    }

    public void initializeWorker(Worker worker, Coord coord) throws IllegalArgumentException,
            IllegalStateException {
        Space dest = board[coord.x][coord.y];
        if (dest.isOccupied()) {
            throw new IllegalArgumentException("Tried to initialize worker on an occupied space.");
        }
        if (worker.getPosition() != null) {
            throw new IllegalStateException("Tried to initialize worker when he is already placed in board.");
        }
        worker.setPosition(coord);
    }
    @Override
    public String toString() {

        //TODO: redefine this method
        return super.toString();
    }
}
