package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Game {

    private Board gameBoard;
    private PriorityQueue<Player> gameQueue;    //coda di gioco
    private boolean win = false;

    //si suppone che la lista players contenga i giocatori ordinati in senso orario (necessario per l'estrazione dei god)
    public Game(PriorityQueue<Player> players) {

        this.gameBoard = new Board();
        this.gameQueue = players;
    }

    public void Start() throws Exception{
        setupWorkers();

        while(!win){
            //prossimo giocatore
            Player currentPlayer = gameQueue.poll();

            //sceglie operaio
            Worker currentWorker = currentPlayer.PickWorker();
            //posizione dell'operaio
            Coord currentPosition = gameBoard.GetWorkerPosition(currentWorker);

            //lista mosse possibili
            ArrayList<Coord> possibleMoves = gameBoard.CheckMoveSpacesAround(currentPosition);
            if(possibleMoves.size() == 0) //se non ci sono mosse disponibili, si perde
                Loose(currentPlayer);

            //giocatore sceglie mossa
            Coord move = currentPlayer.ChooseMove(possibleMoves);
            //aggiorna coordinate ed altitudine
            gameBoard.MoveWorker(currentWorker, move);
            System.out.println("Player " + currentPlayer.getNickname() + " moved worker " + currentWorker.toString() +
                    " in position X: " + move.x + " Y: " + move.y);

            //dopo mossa, controlla vincita
            //TODO : Check for win

            //lista costruzioni possibili
            ArrayList<Coord> possibleBuilds = gameBoard.CheckBuildableSpacesAround(move);
            if(possibleBuilds.size() == 0)
                Loose(currentPlayer); //se non ci sono mosse disponibili, si perde

            //giocatore sceglie dove costruire
            Coord buildPosition = currentPlayer.ChooseBuild(possibleBuilds);
            gameBoard.Build(buildPosition);
            System.out.println("Player " + currentPlayer.getNickname() + " built with worker " + currentWorker.toString() +
                    " in position X: " + buildPosition.x + " Y: " + buildPosition.y);

            //metti giocatore in fila
            gameQueue.add(currentPlayer);
        }
    }

    private void Loose(Player p){

    }

    private void Win(){

    }

    private void setupWorkers() throws Exception{
        for(Player p : gameQueue){
            ArrayList<Coord> possiblePositions = gameBoard.GetUnoccupiedSpaces();
            Coord[] positions = p.ChooseStartingPositions(possiblePositions);
            gameBoard.AddWorker(p.getWorkers()[0], positions[0]);
            gameBoard.AddWorker(p.getWorkers()[1], positions[1]);
        }
    }


}
