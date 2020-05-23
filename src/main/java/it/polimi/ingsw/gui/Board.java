package it.polimi.ingsw.gui;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.Connection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Board implements Initializable {

    //used for reacting when a message arrives from the server
    private class MessageReceiver implements Observer<Object> {
        @Override
        public void update(Object message) {
            //System.out.println("Received: " + message.toString());
            receivedObject = message;
            handleMessageReceived();
        }
    }

    //used to store the graphics and the coordinates for the board cells
    private class Cell{
        private ImageView level, dome, worker;
        private boolean enabled;
        private Coord coord;
        public Cell(int x, int  y){
            this.level = new ImageView();
            this.dome = new ImageView();
            this.worker = new ImageView();
            this.level.setX(0);
            this.level.setY(0);
            this.dome.setX(0);
            this.dome.setY(0);
            this.worker.setX(0);
            this.worker.setY(0);
            this.level.setFitWidth(cellDim);
            this.level.setFitHeight(cellDim);
            this.dome.setFitWidth(cellDim/1.30);
            this.dome.setFitHeight(cellDim/1.30);
            this.worker.setFitWidth(cellDim/1.30);
            this.worker.setFitHeight(cellDim/1.30);
            this.coord = new Coord(x, y);
            this.enabled = false;

            this.level.setOnMouseClicked(mouseEvent -> { if(this.enabled){ onCellClicked(this.coord); } } );
        }
        public void setPosition(int x, int y){
            this.level.setX(x);
            this.level.setY(y);
            this.dome.setX(x + domeOffset);
            this.dome.setY(y + domeOffset);
            this.worker.setX(x + domeOffset);
            this.worker.setY(y + domeOffset);
        }
        public void setLevelImage(Image img){
            this.level.setImage(img);
        }
        public void setDomeImage(){
            this.dome.setImage(domeImage);
        }
        public void setWorkerImage(Image worker){
            this.worker.setImage(worker);
        }
        public void removeWorkerImage(){
            this.worker.setImage(emptyImage);
        }
        public void resetImages(){
            this.level.setImage(null);
            this.dome.setImage(null);
            this.worker.setImage(null);
        }
        public void enable(){
            this.enabled = true;
            this.level.setCursor(Cursor.HAND);
        }
        public void disable(){
            this.enabled = false;
            this.level.setCursor(Cursor.DEFAULT);
        }
        public ImageView getLevelImage(){
            return this.level;
        }
        public ImageView getDomeImage(){
            return this.dome;
        }
        public ImageView getWorkerImage(){
            return this.worker;
        }
    }

    private enum State{
        NONE, MOVE, BUILD, INITIALIZINGWORKERS, SELECTINGWORKER;
    }

    //connection variables
    private Object receivedObject;
    private Connection serverConnection;
    private ExecutorService exec; //to listen for server messages on a separate thread

    //game variables
    private boolean isChallenger;
    private String nickname;
    private Map<String, Color> playersColors;
    private Map<String, God> playersGods;
    private Cell[][] board;
    private State state = State.NONE;

    private List<Coord> clickableCells;

    //resources and window components
    public AnchorPane boardWindow;
    public ImageView moveBtnOn, moveBtnOff, buildBtnOn, buildBtnOff, skipBtnOn, skipBtnOff;
    private Image lvl1Image, lvl2Image, lvl3Image, domeImage, emptyImage;
    private Map<Color, Image> workersTokens;
    private int cellStep = 106, intialX = 384, initialY = 109, cellDim = 90, domeOffset = 10;

    public Board(){
        //initialize board
        this.board = new Cell[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                this.board[i][j] = new Cell(i, j);
                this.board[i][j].setPosition(i*cellStep + intialX, j*cellStep + initialY);
            }
        }
        //initialize resources
        this.lvl1Image = new Image("Board/lvl_1.png");
        this.lvl2Image = new Image("Board/lvl_2.png");
        this.lvl3Image = new Image("Board/lvl_3.png");
        this.domeImage = new Image("Board/dome.png");
        this.emptyImage = new Image("Board/empty.png");
        this.workersTokens = new HashMap<>();
        this.workersTokens.put(Color.RED, new Image("Board/token_rosso.png"));
        this.workersTokens.put(Color.BLUE, new Image("Board/token_blu.png"));
        this.workersTokens.put(Color.YELLOW, new Image("Board/token_giallo.png"));

        this.playersColors = new HashMap<>();
        this.playersGods = new HashMap<>();

        this.clickableCells = new ArrayList<>();
    }

    //used to pass connection and game parameters from the previous window (main menu)
    public void setParameters(Connection serverConnection, boolean isChallenger, String nickname){
        this.serverConnection = serverConnection;
        this.serverConnection.addObserver(new MessageReceiver());

        this.isChallenger = isChallenger;
        this.nickname = nickname;

        exec = Executors.newFixedThreadPool(1);
        System.out.println("You have entered the lobby.");
        exec.submit(serverConnection);
    }

    //Initializes the board
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //add each ImageView the matrix board as children of the main pane
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                this.board[i][j].setLevelImage(emptyImage); //to make cell clickable even if there is no image (for setup phase, placing workers)
                boardWindow.getChildren().add(board[i][j].getLevelImage());
                boardWindow.getChildren().add(board[i][j].getDomeImage());
                boardWindow.getChildren().add(board[i][j].getWorkerImage());
            }
        }
    }

    //called when clicking on a cell (can be clicked only if the cell was enabled)
    private void onCellClicked(Coord clickedCell){
        switch (this.state){
            case MOVE:
                //TODO: player has chose to move in clickedCell

                //if cella selezionata non è in this.clickableCells:
                //TODO: show error label / message
                break;

            case BUILD:
                //TODO: player has chose to build in clickedCell

                //if cella selezionata non è in this.clickableCells:
                //TODO: show error label / message
                break;

            case INITIALIZINGWORKERS:
                //TODO: worker initialization
                //if(cella selezionata va bene (è libera)
                //crea la lista di object, metti stringa onWorkerQualcosa.., metti coordinate selezionate
                //this.serverConnection.send( lista di object )

                //if cella selezionata non è in this.clickableCells:
                //TODO: show error label / message
                break;

            case SELECTINGWORKER:
                //TODO: player has chose where to place his worker

                //if cella selezionata non è in this.clickableCells:
                //TODO: show error label / message
                break;

            default:
                break;
        }
    }

    //enable board, means the cell are clickable
    private void enableBoard(boolean enable){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(enable){
                    board[i][j].enable();
                }else{
                    board[i][j].disable();
                }
            }
        }
    }

    //used to update the graphics when the onBoardChanged event arrives
    private void updateBoard(it.polimi.ingsw.model.Board b){
        //TODO: update the board
        //reset the board

        //draw the board
    }

    //enable move button
    private void enableMove(boolean enable){
        if(enable){
            this.moveBtnOff.setVisible(false);
            this.moveBtnOn.setVisible(true);
        }
        else{
            this.moveBtnOff.setVisible(true);
            this.moveBtnOn.setVisible(false);
        }
    }

    //enable build button
    private void enableBuild(boolean enable){
        if(enable){
            this.buildBtnOff.setVisible(false);
            this.buildBtnOn.setVisible(true);
        }
        else{
            this.buildBtnOff.setVisible(true);
            this.buildBtnOn.setVisible(false);
        }
    }

    //enable skip button
    private void enableSkip(boolean enable){
        if(enable){
            this.skipBtnOff.setVisible(false);
            this.skipBtnOn.setVisible(true);
        }
        else{
            this.skipBtnOff.setVisible(true);
            this.skipBtnOn.setVisible(false);
        }
    }

    //disable board and all buttons
    private void disableAll(){
        this.enableBoard(false);
        this.enableMove(false);
        this.enableBuild(false);
        this.enableSkip(false);
    }

    //called when the move button is clicked
    @FXML
    public void moveAction(MouseEvent event){
        this.disableAll();
        this.enableBoard(true);
        this.state = State.MOVE;
    }

    //called when the move button is clicked
    @FXML
    public void buildAction(MouseEvent event){
        this.disableAll();
        this.enableBoard(true);
        this.state = State.BUILD;
    }

    //called when the move button is clicked
    @FXML
    public void skipAction(MouseEvent event){
        this.disableAll();
        this.state = State.NONE;
        //TODO: send a message to the server, player want to skip
    }

    //called when receiving a message from the server
    private void handleMessageReceived() {
        List<Object> objs;
        if (receivedObject instanceof List)
            objs = (List<Object>) receivedObject;
        else {
            System.out.println("Something went wrong in handling received message");
            return;
        }
        String event = (String) objs.get(0);

        switch (event) {
            //MODEL MESSAGES
            case "onBoardChanged":
                disableAll();
                it.polimi.ingsw.model.Board b = (it.polimi.ingsw.model.Board)objs.get(1);
                System.out.println(b.toString());
                this.updateBoard(b);
                break;

            case "onGameReady":
                disableAll();
                System.out.println("Set up phase is done!");
                //TODO: make a message appear on the window
                break;

            case "onGodsChosen":
                disableAll();
                List<String> selectedGods = (List<String>) objs.get(1);
                System.out.println("Challenger has chosen the playable gods");
                //TODO: make a message appear on the window
                break;

            case "onPlayerAdded":
                disableAll();
                String nickname = (String) objs.get(1);
                int numCurr = (int) objs.get(2);
                int numTot = (int) objs.get(3);
                System.out.println(nickname + " has joined the game. Waiting for " + (numTot-numCurr) + " more player(s)");
                //TODO: make a message appear on the window
                break;

            case "onGodSelection":
                disableAll();
                List<String> godsForSelection = (List<String>) objs.get(1);
                //TODO: make a popup window appear to select a god from the list
                break;

            case "onGodsSelection":
                disableAll();
                if(isChallenger){
                    List<String> allGods = (List<String>) objs.get(1);
                    int numPlayers = (int) objs.get(2);
                    //TODO: make a popup window appear to make the challenger choose gods for the game
                }else{
                    System.out.println("The challenger is choosing the gods");
                    //TODO: make a message appear on the window
                }
                break;

            case "onStartPlayerSelection":
                disableAll();
                if(isChallenger){
                    List<String> players = (List<String>) objs.get(1);
                    //TODO: make a popup windows appear to make the challenger choose the start player
                }
                else {
                    System.out.println("Challenger is choosing the starting player");
                    //TODO: make a message appear on the window
                }
                break;

            case "onMyInitialization":
                disableAll();
                List<Coord> freeSpaces = (List<Coord>) objs.get(1);
                this.clickableCells.clear();
                this.clickableCells = freeSpaces;
                this.state = State.INITIALIZINGWORKERS;
                this.enableBoard(true);
                //a questo punto la board è abilitata e verrà selezionato lo spazio dove posizionare il worker
                //TODO: completare onCellClicked()
                break;

            case "onMyTurn":
                disableAll();
                List<Coord> selectableWorkers = (List<Coord>) objs.get(1);
                this.clickableCells.clear();
                this.clickableCells.addAll(selectableWorkers);
                this.state = State.SELECTINGWORKER;
                enableBoard(true);
                //a questo punto la board è abilitata e verrà selezionato il worker per il turno
                //TODO: completare onCellClicked()
                break;

            case "onMyAction":
                disableAll();
                List<Coord> movableSpaces = (List<Coord>) objs.get(1);
                Map<Level, List<Coord>> buildableSpaces = (Map<Level, List<Coord>>) objs.get(2);
                boolean canEndTurn = (boolean) objs.get(3);
                this.clickableCells.clear();
                this.clickableCells.addAll(movableSpaces);
                boolean thereAreBuilds = false;
                for(Level l:buildableSpaces.keySet()){
                    this.clickableCells.addAll(buildableSpaces.get(l));
                    if(buildableSpaces.get(l).size() > 0)
                        thereAreBuilds = true;
                }
                if(canEndTurn)
                    this.enableSkip(true);
                if(movableSpaces.size() > 0)
                    this.enableMove(true);
                if(thereAreBuilds)
                    this.enableBuild(true);
                this.state = State.NONE;
                //a questo punto un button tra move e build verrà premuto, e la board verrà abilitata
                //TODO: completare onCellClicked()
                break;

            case "onEnd":
                disableAll();
                System.out.println("Game Ended");
                break;

            case "onMessage":
                disableAll();
                String message = (String)((List<Object>) receivedObject).get(2);
                System.out.println(message);
                break;

            case "disconnected":
                disableAll();
                // the game is no more valid, client must disconnect
                System.out.println("A client disconnected from the game, disconnecting...");
                //TODO: Close socket, streams
                //TODO: Close this windows and show main menu window
                break;

            default:
                disableAll();
                System.out.println("Event message not recognized.");
                break;
        }
    }

}
