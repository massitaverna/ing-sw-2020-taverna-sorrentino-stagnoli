//mod
package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

import java.util.ArrayList;
import java.util.List;

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

    public Worker getWorkerByPosition(Coord pos) throws WorkerNotFoundException{
        for(Worker w: this.workers){
            if(w.getPosition().equals(pos)){
                return w;
            }
        }
        throw new WorkerNotFoundException("There is no worker in the selected position.");
   }

    public List<Coord> getUnoccupiedSpaces(){

        List<Coord> unoccupiedSpaces = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(!board[i][j].isOccupied()){
                    unoccupiedSpaces.add(new Coord(i, j));
                }
            }
        }

        return unoccupiedSpaces;
    }

    public void workerMove(Worker w, Coord newPos) throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException {
        //TODO: Check newPos is valid
        if(Coord.validCoord(newPos));

        //TODO: Check that worker w is in the list of workers

        //TODO: Check that worker w is near newPos
        if(!(w.getPosition().isNear(newPos))){
            throw new IllegalWorkerActionException("Cannot move here from that position");
        }

        Space currentSpace, newSpace;
        currentSpace = this.board[w.getPosition().x][w.getPosition().y];
        newSpace = this.board[newPos.x][newPos.y];

        if (!currentSpace.isOccupied()) {
            if (!(newSpace.getHeight() == Level.DOME)) {
                w.setPosition(newPos);
                currentSpace.setUnoccupied();
                newSpace.setOccupied();
            }
            else {
                throw new SpaceOccupiedException("Space occupied by another worker.");
            }
        }
        else{
            throw new SpaceFullException("Space is DOME.");
        }
    }

    public void workerBuild(Worker w, Coord buildPos) throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException{
        //TODO: Check buildPos is valid
        if(Coord.validCoord(buildPos));

        //TODO: Check that worker w in is the list of workers

        //TODO: Check that worker w is near newPos
        if(!(w.getPosition().isNear(buildPos))){
            throw new IllegalWorkerActionException("Cannot build here from that position");
        }

        if(!this.board[buildPos.x][buildPos.y].isOccupied()) {
            if (!(this.board[buildPos.x][buildPos.y].getHeight() == Level.DOME)) {
                this.board[buildPos.x][buildPos.y].levelUp();
            }
            else{
                throw new SpaceOccupiedException("Space occupied by another worker.");
            }
        }
        else{
            throw new SpaceFullException("Space is DOME.");
        }
    }

    //potrebbero essere messe nel controller, perchè qua in mezzo ci andrà anche la logica degli effetti delle divinità
    public /*Map<Coord, Space>*/ List<Coord> getMovableSpacesAround(Coord c, int maxDiff) throws InvalidCoordinatesException{
        //TODO: Check coordinates c are valid
        if(Coord.validCoord(c));

        List<Coord> result = new ArrayList<Coord>();
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
                    /*result.put(new Coord(c.x + i, c.y + j), this.board[c.x + i][c.y + j]);*/
                    result.add(new Coord(c.x + i, c.y + j));
                }
            }
        }

        return result;
    }

    public /*Map<Coord, Space>*/  List<Coord> getBuildableSpacesAround(Coord c) throws InvalidCoordinatesException{
        //TODO: Check coordinates c are valid
        if(Coord.validCoord(c));

        List<Coord> result = new ArrayList<>();
        for (int i = -1; i < 1; i++) {
            for (int j = -1; j < 1; j++) {
                if (i==0 && j==0)
                    continue;

                //if(space not occupied/dome && Rules.CheckSomething) ??
                if(!this.board[c.x + i][c.y + j].isOccupied() &&
                        !this.board[c.x + i][c.y + j].isDome() ) {

                    result.add(new Coord(c.x + i, c.y + j));
                }
            }
        }

        return result;
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
