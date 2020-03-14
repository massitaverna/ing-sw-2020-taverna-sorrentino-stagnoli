package it.polimi.ingsw.Model;

import java.security.InvalidParameterException;
import java.util.*;

public class Board {

    private Space[][] board;
    private HashMap<Worker, Coord> workersPositions;    //posizioni operai
    private static final int DIM = 5;

    public Board(){

        this.board = new Space[DIM][DIM];
        workersPositions = new HashMap<Worker, Coord>();
        for(int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                this.board[i][j] = new Space();
            }
        }

    }

    public void AddWorker(Worker worker, Coord position) throws InvalidParameterException, IllegalMoveException {

        //se coordinate invalide
        if(!isValidCoord(position))
            throw new InvalidParameterException();

        //se space gia' occupato
        if(board[position.x][position.y].getOccupied()) {
            throw new IllegalMoveException();
        }
        else{
            workersPositions.put(worker, position);
            board[position.x][position.y].Occupy();
        }

    }

    public Coord GetWorkerPosition(Worker worker){
        return workersPositions.get(worker);
    }

    public Level GetWorkerHeight(Worker worker){
        return getHeight(GetWorkerPosition(worker));
    }

    public void MoveWorker(Worker worker, Coord newPos) throws InvalidParameterException, IllegalMoveException {
        if(!isValidCoord(newPos))
            throw new InvalidParameterException();

        Coord actualPos = GetWorkerPosition(worker);
        if((board[newPos.x][newPos.y].getLevel().ordinal() - board[actualPos.x][actualPos.y].getLevel().ordinal()) <= 1){
            board[actualPos.x][actualPos.y].Free();
            board[newPos.x][newPos.y].Occupy();
            workersPositions.put(worker, newPos);
        }
        else{
            throw new IllegalMoveException();
        }
    }

    //ritorna lista di coordinate di tutti gli spazi non occupati (non occupati da workers o cupole)
    public ArrayList<Coord> GetUnoccupiedSpaces(){

        ArrayList<Coord> result = new ArrayList<Coord>();

        for(int i = 0; i < DIM; i++){
            for(int j = 0; j < DIM; j++){
                if(!this.board[i][j].getOccupied() || !this.board[i][j].getDome()){
                    result.add(new Coord(i, j));
                }
            }
        }

        return result;
    }

    //ritorna lista di coordinate di spazi vicini allo spazio di coordinate c, in cui è lecito spostarsi (non occupati da workers o cupole)
    public ArrayList<Coord> CheckMoveSpacesAround(Coord c) throws InvalidParameterException {
        if(!isValidCoord(c))
            throw new InvalidParameterException();

        ArrayList<Coord> result = new ArrayList<Coord>();
        Level currentHeight = this.board[c.x][c.y].getLevel();

        //check all neighbours
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {

                //skip the central Space
                if(i==0 && j==0)
                    break;

                Coord neighbour = new Coord(c.x+i, c.y+j);

                if(isValidCoord(neighbour)){
                    if(!this.board[neighbour.x][neighbour.y].getOccupied() && !this.board[neighbour.x][neighbour.y].getDome()){
                        //if diff in height in 1 or less
                        if((currentHeight.ordinal() - board[neighbour.x][neighbour.y].getLevel().ordinal()) >= -1) {
                            result.add(neighbour);
                        }
                    }
                }
            }
        }

        return result;
    }

    //ritorna lista di coordinate di spazi vicini dove si può costruire (non occupati da workers o cupole)
    public ArrayList<Coord> CheckBuildableSpacesAround(Coord c) throws InvalidParameterException {
        if(!isValidCoord(c))
            throw new InvalidParameterException();

        ArrayList<Coord> result = new ArrayList<Coord>();
        Level currentHeight = this.board[c.x][c.y].getLevel();

        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {

                //skip the central Space
                if(i==0 && j==0)
                    break;

                Coord neighbour = new Coord(c.x+i, c.y+j);

                if(isValidCoord(neighbour)){
                    if(!this.board[neighbour.x][neighbour.y].getOccupied() && !this.board[neighbour.x][neighbour.y].getDome()){
                            result.add(neighbour);
                    }
                }
            }
        }

        return result;
    }

    public Level getHeight(Coord c) throws InvalidParameterException{
        if(!isValidCoord(c))
            throw new InvalidParameterException();

        return board[c.x][c.y].getLevel();
    }

    public void Build(Coord c) throws InvalidParameterException, SpaceFullException {
        if(!isValidCoord(c))
            throw new InvalidParameterException();

        board[c.x][c.y].Build();
    }

    private boolean isValidCoord(Coord c){
        return (!(c.x < 0 || c.x > DIM-1) && !(c.y < 0 || c.y > DIM-1));
    }
}
