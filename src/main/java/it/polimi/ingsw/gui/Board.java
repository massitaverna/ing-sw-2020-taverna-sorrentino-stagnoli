package it.polimi.ingsw.gui;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.Connection;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
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
        public Cell(int x, int y){
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
        public void resetImages(){
            this.level.setImage(emptyImage);
            this.dome.setImage(emptyImage);
            this.worker.setImage(emptyImage);
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

    //used to store the state of the gui
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
    private Map<Level, List<Coord>> buildableSpaces;

    private List<Coord> clickableCells;
    private boolean canMove, canBuild, canSkip;

    //resources and window components
    public AnchorPane boardWindow;
    public ImageView moveBtnOn, moveBtnOff, buildBtnOn, buildBtnOff, skipBtnOn, skipBtnOff, backBtn;
    public ImageView redBanner, blueBanner, yellowBanner;
    public TextArea messageBox;
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

        this.serverConnection.addObserver(new MessageReceiver());
        exec = Executors.newFixedThreadPool(1);
        exec.submit(serverConnection);
        System.out.println("You have entered the lobby.");
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
                //player has chose to move in clickedCell
                if(clickableCells.contains(clickedCell)){
                    List<Object> action = new ArrayList<>();
                    action.add("onMoveChosen");
                    action.add(clickedCell);
                    serverConnection.asyncSend(action);
                    this.state=State.NONE;
                    this.disableAll();
                }else{
                    this.showMessage("Invalid move.");
                }
                break;

            case BUILD:
                //player has chose to build in clickedCell
                if(clickableCells.contains(clickedCell)){
                    List<Object> action = new ArrayList<>();
                    action.add("onBuildChosen");
                    action.add(clickedCell);
                    //TODO: add gui elements to choose the level for building
                    serverConnection.asyncSend(action);
                    this.state=State.NONE;
                    this.disableAll();
                }else{
                    this.showMessage("Invalid build.");
                }
                break;

            case INITIALIZINGWORKERS:
                //worker initialization
                if(clickableCells.contains(clickedCell)){
                    List<Object> action = new ArrayList<>();
                    action.add("onWorkerInitialization");
                    action.add(clickedCell);
                    serverConnection.asyncSend(action);
                    this.state=State.NONE;
                    this.disableAll();
                }else{
                    this.showMessage("Invalid selection.");
                }
                break;

            case SELECTINGWORKER:
                //player has chose where to place his worker
                if(clickableCells.contains(clickedCell)){
                    List<Object> action = new ArrayList<>();
                    action.add("onWorkerChosen");
                    action.add(clickedCell);
                    serverConnection.asyncSend(action);
                    this.state=State.NONE;
                    this.disableAll();
                }else{
                    this.showMessage("Invalid selection.");
                }
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
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                //reset the board
                board[i][j].resetImages();
                //draw the board
                switch (b.getSpace(new Coord(i, j)).getHeight()){
                    case GROUND:
                        this.board[i][j].setLevelImage(this.emptyImage);
                        break;
                    case LVL1:
                        this.board[i][j].setLevelImage(this.lvl1Image);
                        break;
                    case LVL2:
                        this.board[i][j].setLevelImage(this.lvl2Image);
                        break;
                    case LVL3:
                        this.board[i][j].setLevelImage(this.lvl3Image);
                        break;
                }
                if(b.getSpace(new Coord(i, j)).isDome()){
                    this.board[i][j].setDomeImage();
                }
            }
        }
        //draw worker
        for(Worker w: b.getWorkers()){
            board[w.getPosition().x][w.getPosition().y].setWorkerImage(workersTokens.get(w.getColor()));
        }
    }

    //used to show a message in the text area
    private void showMessage(String message){
        this.messageBox.appendText(message);
        this.messageBox.appendText("---");
    }

    //enable move button
    private void enableMove(boolean enable){
        this.moveBtnOff.setVisible(!enable);
        this.moveBtnOn.setVisible(enable);
    }

    //enable build button
    private void enableBuild(boolean enable){
        this.buildBtnOff.setVisible(!enable);
        this.buildBtnOn.setVisible(enable);
    }

    //enable skip button
    private void enableSkip(boolean enable){
        this.skipBtnOff.setVisible(!enable);
        this.skipBtnOn.setVisible(enable);
    }

    //enable back button
    private void enableBack(boolean enable){
        this.backBtn.setVisible(enable);
    }

    //disable board and all buttons
    private void disableAll(){
        this.enableBoard(false);
        this.enableMove(false);
        this.enableBuild(false);
        this.enableSkip(false);
        this.enableBack(false);
    }

    //called when the move button is clicked
    @FXML
    public void moveAction(MouseEvent event){
        this.disableAll();
        this.enableBoard(true);
        this.state = State.MOVE;
        this.enableBack(true);
    }

    //called when the move button is clicked
    @FXML
    public void buildAction(MouseEvent event){
        this.disableAll();
        this.enableBoard(true);
        this.state = State.BUILD;
        this.enableBack(true);
    }

    //called when the move button is clicked
    @FXML
    public void skipAction(MouseEvent event){
        this.disableAll();
        this.state = State.NONE;
        //send a message to the server, player wants to skip
        List<Object> objects = new ArrayList<>();
        objects.add("skipAction");
        serverConnection.asyncSend(objects);
    }

    //called when the back button is clicked
    @FXML
    public void goBack(){
        disableAll();
        this.state = State.NONE;
        if(canSkip)
            enableSkip(true);
        if(canMove)
            enableMove(true);
        if(canBuild)
            enableBuild(true);
    }

    //to add player banner (when game is ready)
    private void addPlayerBanner(Map<String, Color> playerColorMap){

    }

    //to remove a player banner (when he looses)
    private void removePlayerBanner(String nickname){

    }

    //to highlight a player banner during it's turn
    private void highlightPlayerBanner(String nickname){

    }

    //to ask challenger to choose gods for the game
    private List<String> godsSelectionPopup(int numPlayers){

        final List<String>[] gods = new List[]{new ArrayList<>()};

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GodsPopup.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            Stage stage = new Stage();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Choose the playable gods");
            stage.initOwner(boardWindow.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            ((GodsPopup) loader.getController()).setNumPlayers(numPlayers);
            stage.setOnCloseRequest(windowEvent -> gods[0] = ((GodsPopup) loader.getController()).getChoices());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gods[0];

    }

    //to ask a player for his god
    private String godSelectionPopup(List<String> availableGods){

        final String[] god = new String[1];

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GodPopup.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            Stage stage = new Stage();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Choose the playable gods");
            stage.initOwner(boardWindow.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            ((GodPopup) loader.getController()).setGods(availableGods);
            stage.setOnCloseRequest(windowEvent -> god[0] = ((GodPopup) loader.getController()).getChoice());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return god[0];
    }

    //to ask challenger for starting player
    private int startPlayerSelectionPopup(List<String> players){
        return 0;
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
                this.showMessage("Set up phase is done!");
                break;

            case "onGodsChosen":
                disableAll();
                List<String> selectedGods = (List<String>) objs.get(1);
                System.out.println("Challenger has chosen the playable gods");
                this.showMessage("Challenger has chosen the playable gods");
                break;

            case "onPlayerAdded":
                disableAll();
                String nickname = (String) objs.get(1);
                int numCurr = (int) objs.get(2);
                int numTot = (int) objs.get(3);
                System.out.println(nickname + " has joined the game. Waiting for " + (numTot-numCurr) + " more player(s)");
                this.showMessage(nickname + " has joined the game. Waiting for " + (numTot-numCurr) + " more player(s)");
                //TODO: add player banner
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
                    this.showMessage("The challenger is choosing the gods");
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
                    this.showMessage("Challenger is choosing the starting player");
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
                break;

            case "onMyTurn":
                disableAll();
                List<Coord> selectableWorkers = (List<Coord>) objs.get(1);
                this.clickableCells.clear();
                this.clickableCells.addAll(selectableWorkers);
                this.state = State.SELECTINGWORKER;
                enableBoard(true);
                //a questo punto la board è abilitata e verrà selezionato il worker per il turno
                break;

            case "onMyAction":
                disableAll();
                this.canMove = false;
                this.canBuild = false;
                this.canSkip = false;
                List<Coord> movableSpaces = (List<Coord>) objs.get(1);
                this.buildableSpaces = (Map<Level, List<Coord>>) objs.get(2);
                boolean canEndTurn = (boolean) objs.get(3);
                this.clickableCells.clear();
                this.clickableCells.addAll(movableSpaces);
                boolean thereAreBuilds = false;
                for(Level l:this.buildableSpaces.keySet()){
                    this.clickableCells.addAll(this.buildableSpaces.get(l));
                    if(this.buildableSpaces.get(l).size() > 0)
                        thereAreBuilds = true;
                }
                if(canEndTurn){
                    this.enableSkip(true);
                    this.canSkip = true;
                }
                if(movableSpaces.size() > 0) {
                    this.enableMove(true);
                    this.canMove = true;
                }
                if(thereAreBuilds) {
                    this.enableBuild(true);
                    this.canBuild = true;
                }
                this.state = State.NONE;
                //a questo punto un button tra move e build verrà premuto, e la board verrà abilitata
                break;

            case "onEnd":
                disableAll();
                System.out.println("Game Ended");
                break;

            case "onMessage":
                disableAll();
                String message = (String)((List<Object>) receivedObject).get(1);
                System.out.println(message);
                break;

            case "disconnected":
                disableAll();
                // the game is no more valid, client must disconnect
                System.out.println("A client disconnected from the game, disconnecting...");
                //Close socket, streams
                this.serverConnection.closeConnection();
                //Close this windows and show main menu window
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/home.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) { e.printStackTrace(); }
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.sizeToScene();
                stage.setTitle("Santorini Game");
                stage.show();
                ((Stage) boardWindow.getScene().getWindow()).close();
                break;

            default:
                disableAll();
                System.out.println("Event message not recognized.");
                break;
        }
    }

    public void closeConnection(){
        this.serverConnection.closeConnection();
    }

}
