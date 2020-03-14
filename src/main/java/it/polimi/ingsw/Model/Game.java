package it.polimi.ingsw.Model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Game {

    private Board gameBoard;
    private ArrayList<Player> gameQueue;    //coda di gioco

    //si suppone che la lista players contenga i giocatori ordinati in senso orario e che ogni player ha gia scelto il suo god.
    public Game(ArrayList<Player> players) {

        this.gameBoard = new Board();
        this.gameQueue = players;
    }

    public void Start() throws IllegalMoveException, InvalidParameterException, SpaceFullException{
        setupWorkers();

        while(true){
            //prossimo giocatore
            Player currentPlayer = gameQueue.get(0);
            gameQueue.remove(currentPlayer);
            System.out.println(currentPlayer.getNickname() + "'s turn.");

            //sceglie operaio
            Worker currentWorker = currentPlayer.PickWorker();
            //posizione dell'operaio
            Coord currentPosition = gameBoard.GetWorkerPosition(currentWorker);

            //lista mosse possibili
            ArrayList<Coord> possibleMoves = gameBoard.CheckMoveSpacesAround(currentPosition);
            if(possibleMoves.size() == 0) { //se non ci sono mosse disponibili, si perde
                Loose(currentPlayer);
                break;
            }

            //giocatore sceglie mossa
            Coord move = currentPlayer.ChooseMove(possibleMoves);
            //aggiorna coordinate ed altitudine
            gameBoard.MoveWorker(currentWorker, move);
            System.out.println("Player " + currentPlayer.getNickname() + " moved worker " + currentWorker.toString() +
                    " in position X: " + move.x + " Y: " + move.y + " height: " + gameBoard.getHeight(move));

            //dopo mossa, controlla vincita
            //TODO : Check for win
            if(gameBoard.getHeight(move).ordinal() == 3){
                this.Win(currentPlayer);
                break;
            }

            //lista costruzioni possibili
            ArrayList<Coord> possibleBuilds = gameBoard.CheckBuildableSpacesAround(move);
            if(possibleBuilds.size() == 0) {
                Loose(currentPlayer); //se non ci sono mosse disponibili, si perde
                break;
            }

            //giocatore sceglie dove costruire
            Coord buildPosition = currentPlayer.ChooseBuild(possibleBuilds);
            System.out.println("Player " + currentPlayer.getNickname() + " wants to build with worker "
                    + currentWorker.toString() + " in position X: " + buildPosition.x + " Y: " + buildPosition.y +
                    " height: " + gameBoard.getHeight(buildPosition));
            gameBoard.Build(buildPosition);
            System.out.println("Built. height: " + gameBoard.getHeight(buildPosition));

            //metti giocatore in fila
            gameQueue.add(currentPlayer);
        }
    }

    //codice eseguito alla sconfitta di un player
    private void Loose(Player p){
        System.out.println(p.getNickname() + " loose.");
    }

    //codice eseguito alla vincita di un player
    private void Win(Player p){
        System.out.println(p.getNickname() + " wins!!");
    }

    //fase iniziale di gioco, posizionamento workers
    private void setupWorkers() throws InvalidParameterException, IllegalMoveException{
        for(Player p : gameQueue){
            ArrayList<Coord> possiblePositions = gameBoard.GetUnoccupiedSpaces();
            Coord[] positions = p.ChooseStartingPositions(possiblePositions);
            gameBoard.AddWorker(p.getWorkers()[0], positions[0]);
            gameBoard.AddWorker(p.getWorkers()[1], positions[1]);
            System.out.println(p.getNickname() + " put worker 0 " + p.getWorkers()[0].toString() + " in space " + positions[0].toString());
            System.out.println(p.getNickname() + " put worker 1 " + p.getWorkers()[1].toString() + " in space " + positions[1].toString());
        }
    }


}
