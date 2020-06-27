package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board implements Cloneable, Serializable {

    private static final long serialVersionUID = 2L;

    public static final int BOARD_SIZE = 5;
    private Space[][] board;
    private List<Worker> workers;

    public Board(){
        this.board = new Space[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.board[i][j] = new Space();
            }
        }
        this.workers = new ArrayList<>();
    }

    /**
     * Get the reference to the space in the given coordinates
     * @param c the coordinates
     * @return
     * @throws InvalidCoordinatesException when invalid coordinates
     */
    public Space getSpace(Coord c) throws InvalidCoordinatesException {

        //Check coordinates c are valid
        if (Coord.validCoord(c)) {
            return board[c.x][c.y].clone();
        } else {
            throw new InvalidCoordinatesException("Invalid coordinates.");
        }
    }

    void addWorker(Worker w) throws IllegalStateException {

        //if worker is already present, throw exception
        if(this.workers.contains(w)) {
            throw new IllegalStateException("The worker has already been added.");
        }

        this.workers.add(w);
    }

    Worker[] getAllWorkers(){
        Worker[] allWorkers = new Worker[this.workers.size()];
        for (int i = 0; i < this.workers.size(); i++) {
            allWorkers[i] = this.workers.get(i);
        }
        return allWorkers;
    }

    Worker getWorkerByPosition(Coord pos) throws WorkerNotFoundException {

        //Check coordinates pos are valid
        if (!Coord.validCoord(pos)) {
            throw new WorkerNotFoundException("Worker not found: Invalid coordinates given.");
        }

        for(Worker w: this.workers){
            if(pos.equals(w.getPosition())) {
                return w;
            }
        }
        throw new WorkerNotFoundException("There is no worker in the selected position.");
   }

    /**
     * Get a clone of the worker in the given coordinates
     * @param pos the coordinates
     * @return
     */
    public Worker getWorkerCopy (Coord pos) {
        return getWorkerByPosition(pos).clone();
   }

    /**
     * Intializes the worker of the given player, in the given coordinates
     * @param player the player
     * @param coord the coordinates
     * @throws IllegalArgumentException when invalid coordinates
     * @throws IllegalStateException when the workers of the player are all already set
     */
    void initializeWorker(Player player, Coord coord) throws IllegalArgumentException, IllegalStateException {

        Worker worker = workers.stream()
                .filter(w -> w.getPlayerNickname().equals(player.getNickname()))
                .filter(w -> w.getPosition() == null)
                .findFirst().orElse(null);

        if (worker == null) {
            throw new IllegalStateException("Workers for player " + player.getNickname() +
                    " have already been initialized.");
        }


        Space dest = board[coord.x][coord.y];
        if (dest.isOccupied()) {
            throw new IllegalStateException("Tried to initialize worker on an occupied space.");
        }

        worker.setPosition(coord);
        dest.setOccupied();
    }

    /**
     * Intializes the given worker, in the given coordinates
     * @param worker the worker
     * @param coord the coordinates
     * @throws IllegalArgumentException when invalid coordinates
     * @throws IllegalStateException when the worker is already set
     */
    void initializeWorker(Worker worker, Coord coord) throws IllegalArgumentException, IllegalStateException {

        //Check worker belongs to the game
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

    //THIS METHOD IS USED ONLY FOR INITIALIZATION PHASE !!!!!!

    /**
     * Get all the unoccupied spaces
     * @return a list containing all the unoccupied spaces
     */
    public List<Coord> getUnoccupiedSpaces() {

        List<Coord> unoccupiedSpaces = new ArrayList<>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(!board[i][j].isOccupied()){
                    unoccupiedSpaces.add(new Coord(i, j));
                }
            }
        }

        return unoccupiedSpaces;
    }

    /**
     * Get all the spaces
     * @return a list containing all the spaces
     */
    public List<Space> getAllSpaces() {
        return getAllCoord().stream()
                .map(this::getSpace)
                .collect(Collectors.toList());
    }

    /**
     * Get all the possible coordinates for the board
     * @return a list containing all the possible coordinates for the board
     */
    public List<Coord> getAllCoord() {
        List<Coord> result = new ArrayList<>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                result.add(new Coord(i, j));
            }
        }
        return result;
    }

    void workerMove(Worker w, Coord newPos) throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException {
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


        Space currentSpace, newSpace;
        currentSpace = this.board[w.getPosition().x][w.getPosition().y];
        newSpace = this.board[newPos.x][newPos.y];

        //Space not occupied
        if ( !newSpace.isOccupied() ) {
            //Space not full
            if ( !(newSpace.isDome()) ) {

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

    }

    void workerMove(Coord src, Coord dest) throws
            IllegalWorkerActionException, SpaceOccupiedException, SpaceFullException {

        Worker w = getWorkerByPosition(src);
        workerMove(w, dest);
    }

    void workerForceMove(Worker w, Coord newPos, Coord forcePos){
        if(!Coord.validCoord(newPos) || !Coord.validCoord(forcePos)){
            throw new InvalidCoordinatesException("Invalid coordinates.");
        }

        //Check that worker w is in the list of workers
        if(!this.workers.contains(w)){
            throw new IllegalWorkerActionException("The worker " + w.toString() + " is not part of the game.");
        }

        //check that in newPos there is a worker
        Worker otherW = this.getWorkerByPosition(newPos);

        //check worker w is initialized
        if(w.getPosition() == null){
            throw new IllegalWorkerActionException("The worker is not initialized.");
        }


        Space currentSpace, newSpace, forceSpace;
        currentSpace = this.board[w.getPosition().x][w.getPosition().y];
        newSpace = this.board[newPos.x][newPos.y];
        forceSpace = this.board[forcePos.x][forcePos.y];

        //not space full

        w.setPosition(newPos);
        otherW.setPosition(forcePos);

        currentSpace.setUnoccupied();
        newSpace.setOccupied();
        forceSpace.setOccupied();
    }


    void workerBuild(Worker w, Coord buildPos, Level level) throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException{
        //Check buildPos is valid
        if(!Coord.validCoord(buildPos)){
            throw new InvalidCoordinatesException("Invalid coordinates.");
        }

        //Check that worker w in is the list of workers
        if(!this.workers.contains(w)){
            throw new IllegalWorkerActionException("The worker " + w.toString() + " is not part of the game.");
        }

        //check worker w is initialized
        if(w.getPosition() == null){
            throw new IllegalWorkerActionException("The worker is not initialized.");
        }

        this.board[buildPos.x][buildPos.y].setLevel(level);
    }

    void workerBuild(Worker w, Coord buildPos) throws InvalidCoordinatesException, SpaceFullException, SpaceOccupiedException, IllegalWorkerActionException {
        /*//Check that worker w is near newPos
        if(!(w.getPosition().isNear(buildPos))){
            throw new IllegalWorkerActionException("Cannot build here from that position");
        }*/

        if(!Coord.validCoord(buildPos)){
            throw new InvalidCoordinatesException("Invalid coordinates.");
        }

        //Check that worker w in is the list of workers
        if(!this.workers.contains(w)){
            throw new IllegalWorkerActionException("The worker " + w.toString() + " is not part of the game.");
        }

        //check worker w is initialized
        if(w.getPosition() == null){
            throw new IllegalWorkerActionException("The worker is not initialized.");
        }

        //if not space occupied
        if(!this.board[buildPos.x][buildPos.y].isOccupied()) {
            //if not space full
            if (!(this.board[buildPos.x][buildPos.y].isDome())) {
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

    /**
     * Get all the movable spaces around a given coordinate
     * @param c the coordinate
     * @param maxDiff the max difference in height between the center space and the around one.
     * @return a list containing all the coordinates of movable spaces around a given coordinate
     * @throws InvalidCoordinatesException
     */
    public List<Coord> getMovableSpacesAround(Coord c, int maxDiff) throws InvalidCoordinatesException{

        //Check coordinates c are valid
        if(!Coord.validCoord(c)){
            throw new InvalidCoordinatesException("Invalid coordinates.");
        }

        List<Coord> result = new ArrayList<>();
        for (int i = -1; i < 1; i++) {
            for (int j = -1; j < 1; j++) {
                if (i == 0 && j == 0)
                    continue;

                //if(space not occupied/dome && Rules.CheckSomething) ??
                if (Coord.validCoord(new Coord(c.x + i, c.y + j))) {
                    if (!this.board[c.x + i][c.y + j].isOccupied() &&
                            !this.board[c.x + i][c.y + j].isDome() &&
                            (this.board[c.x + i][c.y + j].getHeight().ordinal() - this.board[c.x][c.y].getHeight().ordinal()
                                    <= maxDiff
                            )
                    ) {
                        /*result.put(new Coord(c.x + i, c.y + j), this.board[c.x + i][c.y + j]);*/
                        result.add(new Coord(c.x + i, c.y + j));
                    }
                }
            }
        }

        return result;
    }

    /**
     * Get all the buildable spaces around a given coordinate
     * @param c the coordinate
     * @return a list containing all the coordinates of buildable spaces around a given coordinate
     * @throws InvalidCoordinatesException
     */
    public List<Coord> getBuildableSpacesAround(Coord c) throws InvalidCoordinatesException{

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
                if(Coord.validCoord(new Coord(c.x + i, c.y + j))) {
                    if (!this.board[c.x + i][c.y + j].isOccupied() &&
                            !this.board[c.x + i][c.y + j].isDome()) {

                        result.add(new Coord(c.x + i, c.y + j));
                    }
                }
            }
        }

        return result;
    }

    /**
     * Get all the spaces around a given coordinate
     * @param c the coordinate
     * @return a list containing all the coordinates of spaces around a given coordinate
     * @throws InvalidCoordinatesException
     */
    public List<Coord> getSpacesAround(Coord c) throws InvalidCoordinatesException{
        //Check coordinates c are valid
        if(!Coord.validCoord(c)){
            throw new InvalidCoordinatesException("Invalid coordinates.");
        }

        List<Coord> result = new ArrayList<>();
        for (int i = -1; i < 1; i++) {
            for (int j = -1; j < 1; j++) {
                if (i==0 && j==0)
                    continue;

                Coord nextCoord = new Coord(c.x + i, c.y + j);
                if(Coord.validCoord(nextCoord)){
                    result.add(nextCoord);
                }
            }
        }

        return result;
    }

    void remove (Player player) {
        List<Worker> workersToBeRemoved = workers.stream()
                .filter(w -> w.getPlayerNickname().equals(player.getNickname()))
                .collect(Collectors.toList());

        workersToBeRemoved.stream()
                .map(Worker::getPosition)
                .map(c -> board[c.x][c.y])
                .forEach(Space::setUnoccupied);

        workers.removeAll(workersToBeRemoved);
    }

    /**
     *Get all the workers on the board
     * @return
     */
    public List<Worker> getWorkers(){
        List<Worker> res = new ArrayList<>();
        for(Worker w : this.workers){
            res.add(w.clone());
        }
        return res;
    }

    /**
     *ToString
     * @return
     */
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

        for (int j = 0; j < 5; j++) {

            workerLine = "  |";
            lvl3Line = (char) ('A'+j) + " |";
            lvl2Line = "  |";
            lvl1Line = "  |";

            for (int i = 0; i < 5; i++) {
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

    /**
     *Clone
     * @return
     */
    @Override
    public Board clone() {
        Space[][] board = new Space[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = getSpace(new Coord(i,j));
            }
        }
        Board result = new Board();
        result.board = board;
        for(Worker w: this.workers){
            result.workers.add(w.clone());
        }

        return result;
    }

    /**
     *Equals
     * @param o
     * @return
     */
    @Override
    public boolean equals (Object o) {
        if (!(o instanceof Board)) return false;
        Board that = (Board) o;
        boolean areEqual = true;

        for (int i = 0; i < BOARD_SIZE && areEqual; i++) {
            for (int j = 0; j < BOARD_SIZE && areEqual; j++) {
                areEqual = this.board[i][j].equals(that.board[i][j]);
            }
        }
        if (areEqual) {
            areEqual = this.workers.equals(that.workers);
        }

        return areEqual;
    }
}
