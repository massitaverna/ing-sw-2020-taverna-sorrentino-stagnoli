package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.*;

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

        //Check coordinates c are valid
        if (Coord.validCoord(c)) {
            return board[c.x][c.y];
        }

        return null;
    }

    public void addWorker(Worker w) throws IllegalStateException {

        //if worker is already present, throw exception
        if(this.workers.contains(w)){
            throw new IllegalStateException("thw worker has already been added.");
        }

        this.workers.add(w);
    }

    public Worker[] getAllWorkers(){
        return (Worker[])this.workers.toArray();
    }

    public Worker getWorkerByPosition(Coord pos) throws WorkerNotFoundException {

        //Check coordinates pos are valid
        if (!Coord.validCoord(pos)) {
            throw new WorkerNotFoundException("Worker not found: Invalid coordinates given.");
        }

        for(Worker w: this.workers){
            if(w.getPosition().equals(pos)){
                return w;
            }
        }
        throw new WorkerNotFoundException("There is no worker in the selected position.");
   }

    public void initializeWorker(Worker worker, Coord coord) throws IllegalArgumentException, IllegalStateException {

        //Check worker Belongs to the game
        if(!this.workers.contains(worker)){
            throw new IllegalArgumentException("The worker " + worker.toString() + " is not part of the game.");
        }

        //Check coordinates are valid
        if (!Coord.validCoord(coord)) {
            throw new IllegalArgumentException("Invalid Coordinates");
        }

        Space dest = board[coord.x][coord.y];
        if (dest.isOccupied()) {
            throw new IllegalStateException("Tried to initialize worker on an occupied space.");
        }
        if (worker.getPosition() != null) {
            throw new IllegalStateException("Tried to initialize worker when he is already placed in board.");
        }
        worker.setPosition(coord);
        dest.setOccupied();
    }

    //ONLY FOR INITIALIZATION PHASE !!!!!!
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
        //Check newPos is valid
        if(!Coord.validCoord(newPos)){
            throw new InvalidCoordinatesException("Invalid coordinates.");
        }

        //Check that worker w is in the list of workers
        if(!this.workers.contains(w)){
            throw new IllegalWorkerActionException("The worker " + w.toString() + " is not part of the game.");
        }

        if(w.getPosition() == null){
            throw new IllegalWorkerActionException("The worker is not initialized.");
        }

        //Check that worker w is near newPos
        if(!(w.getPosition().isNear(newPos))){
            throw new IllegalWorkerActionException("Cannot move here from that position.");
        }

        Space currentSpace, newSpace;
        currentSpace = this.board[w.getPosition().x][w.getPosition().y];
        newSpace = this.board[newPos.x][newPos.y];

        //not space occupied
        if ( !currentSpace.isOccupied() ) {
            //not space full
            if ( !(newSpace.getHeight() == Level.DOME) ) {

                w.setPosition(newPos);
                currentSpace.setUnoccupied();
                newSpace.setOccupied();
            }
            else {
                throw new SpaceFullException("Space is DOME.");
            }
        }
        else{
            throw new SpaceOccupiedException("Space occupied by another worker.");
        }

        //TODO : Check for Winning
    }

    public void workerForceMove(Worker w, Coord newPos){

    }

    public void workerBuild(Worker w, Coord buildPos) throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException{
        //Check buildPos is valid
        if(!Coord.validCoord(buildPos)){
            throw new InvalidCoordinatesException("Invalid coordinates.");
        }

        //Check that worker w in is the list of workers
        if(!this.workers.contains(w)){
            throw new IllegalWorkerActionException("The worker " + w.toString() + " is not part of the game.");
        }

        if(w.getPosition() == null){
            throw new IllegalWorkerActionException("The worker is not initialized.");
        }

        //Check that worker w is near newPos
        if(!(w.getPosition().isNear(buildPos))){
            throw new IllegalWorkerActionException("Cannot build here from that position");
        }

        //not space occupied
        if(!this.board[buildPos.x][buildPos.y].isOccupied()) {
            //not space full
            if (!(this.board[buildPos.x][buildPos.y].getHeight() == Level.DOME)) {
                this.board[buildPos.x][buildPos.y].levelUp();
            }
            else{
                throw new SpaceFullException("Space is DOME.");
            }
        }
        else{
            throw new SpaceOccupiedException("Space occupied by another worker.");
        }
    }

    //potrebbero essere messe nel controller, perchè qua in mezzo ci andrà anche la logica degli effetti delle divinità
    public /*Map<Coord, Space>*/ List<Coord> getMovableSpacesAround(Coord c, int maxDiff) throws InvalidCoordinatesException{

        //Check coordinates c are valid
        if(!Coord.validCoord(c)){
            throw new InvalidCoordinatesException("Invalid coordinates.");
        }

        List<Coord> result = new ArrayList<>();
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

        //Check coordinates c are valid
        if(!Coord.validCoord(c)){
            throw new InvalidCoordinatesException("Invalid coordinates.");
        }

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

    @Override
    public String toString() {

        String numLine =            "       1         2         3         4         5     \n";
        String horizontalBorder =   "  +---------+---------+---------+---------+---------+\n";
        String topSpaceLine =       "  |         |         |         |         |         |\n";

        String stdColor = (char) 27 + "[39m";

        String boardString = numLine + horizontalBorder;
        String workerLine;
        String lvl3Line;
        String lvl2Line;
        String lvl1Line;

        for (int i = 0; i < 5; i++) {

            workerLine = "  |";
            lvl3Line = (char) 65+i + " |";
            lvl2Line = "  |";
            lvl1Line = "  |";

            for (int j = 0; j < 5; j++) {
                if (board[i][j].isOccupied()) {
                    try {
                        Worker workerInSpace = this.getWorkerByPosition(new Coord(i ,j));
                        String colredWorker = "";
                        switch (workerInSpace.getColor()) {
                            case RED:
                                colredWorker = (char) 27 + "[31mW";
                                break;
                            case BLUE:
                                colredWorker = (char) 27 + "[34mW";
                                break;
                            case YELLOW:
                                colredWorker = (char) 27 + "[33mW";
                                break;
                        }
                        workerLine = workerLine + "    "+ colredWorker + stdColor + "    |";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (board[i][j].isDome())
                    workerLine = workerLine + "    ^    |";  // a space cannot be occupied by a worker and be a dome at the same time
                else
                    workerLine = workerLine + "         |";

                switch (board[i][j].getHeight()) {
                    case LVL3:
                        lvl3Line = lvl3Line + "   ***   |";
                        lvl2Line = lvl2Line + "  *****  |";
                        lvl1Line = lvl1Line + " ******* |";
                        break;

                    case LVL2:
                        lvl3Line = lvl3Line + "         |";
                        lvl2Line = lvl2Line + "  *****  |";
                        lvl1Line = lvl1Line + " ******* |";
                        break;

                    case LVL1:
                        lvl3Line = lvl3Line + "         |";
                        lvl2Line = lvl2Line + "         |";
                        lvl1Line = lvl1Line + " ******* |";
                        break;

                    case GROUND:
                        lvl1Line = lvl1Line + "         |";
                        lvl2Line = lvl2Line + "         |";
                        lvl3Line = lvl3Line + "         |";
                        break;
                }
            }

            workerLine = workerLine + "\n";
            lvl3Line = lvl3Line + "\n";
            lvl2Line = lvl2Line + "\n";
            lvl1Line = lvl1Line + "\n";

            boardString = boardString  + topSpaceLine + workerLine + lvl3Line + lvl2Line + lvl1Line + horizontalBorder;
        }
        return boardString;
    }
}
