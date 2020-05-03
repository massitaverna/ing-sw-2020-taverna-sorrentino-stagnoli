package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.App;
import it.polimi.ingsw.exceptions.model.IllegalWorkerChoiceException;
import it.polimi.ingsw.exceptions.model.WorkerNotFoundException;
import it.polimi.ingsw.listeners.EventSource;
import it.polimi.ingsw.listeners.Listener;
import it.polimi.ingsw.listeners.ModelEventListener;
import it.polimi.ingsw.model.handler.ActionType;
import it.polimi.ingsw.model.handler.RequestHandler;
import it.polimi.ingsw.model.handler.RequestHandlerCreator;

import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.stream.Collectors;

public class GameModel implements EventSource {

    private ModelState state;
    private int numPlayers;
    private List<Player> queue;
    private List<God> godsList;
    private final List<Color> colors;
    private Board board;
    private Player currentPlayer;
    private Worker currentWorker;
    private Turn turn;
    private Map<Player, RequestHandler> handlers;

    private List<ModelEventListener> modelListeners;

    /*per sollevare un evento (esempio) :
        for(ModelEventListener l: modelListeners){
            l.onAllPlayersArrived();
        }*/


    public GameModel() {
        this.state = new LobbyState(this);
        this.queue = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.colors.add(Color.YELLOW);
        this.colors.add(Color.RED);
        this.colors.add(Color.BLUE);
        this.board = new Board();
        this.godsList = new ArrayList<>();
        this.currentPlayer = null;
        this.currentWorker = null;
        this.turn = new Turn();
        this.handlers = new HashMap<>();
        this.modelListeners = new ArrayList<>();
        loadAvailableGods();

    }

    //STATE FUNCTIONS//
    public void changeState(ModelState state) {
        this.state = state;
    }

    public void nextStep() {
        state.nextStep();
    }

    //INIT FUNCTIONS//
    private void loadAvailableGods() {
        //Read from file all available gods
        InputStream inputStream = App.class
                .getClassLoader().getResourceAsStream("gods");
        if (inputStream == null) {
            System.out.println("\"gods\" file wasn't found. Exiting.");
            System.exit(1);
        }
        Scanner sc = new Scanner(inputStream);
        Gson gson = new Gson();
        while (sc.hasNext()) {
            godsList.add(gson.fromJson(sc.nextLine(), God.class));
        }

        System.out.println("Gods loaded: ");
        godsList.forEach(g -> System.out.println(g.getName() + ": " + g.getDescription()));

        /*
        // Alternative way, just to test:
        godsList.add(new God("Apollo", "Very powerful"));
        godsList.add(new God("Athena", "So powerful"));
        godsList.add(new God("Artemis", "Incredibly powerful"));
        godsList.add(new God("Minotaur", "Weak"));
        */

    }

    //SETUP FUNCTIONS//
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean allPlayersArrived(){
        return this.queue.size() == this.numPlayers;
    }

    public void addNewPlayer(Player player){

        if (queue.size() == 0) {
            currentPlayer = player; // Set Challenger as currentPlayer
        }
        queue.add(player);
        this.board.addWorker(player.getWorker(0));
        this.board.addWorker(player.getWorker(1));

        Random r = new Random();
        int i = r.nextInt(colors.size());
        Color randomColor = colors.get(i);
        setPlayerColor(player, randomColor);

        modelListeners.forEach(l -> l.onPlayerAdded(player.getNickname(), queue.size(), numPlayers));
        /*if(allPlayersArrived()) {
            for (ModelEventListener listener : modelListeners) {
                listener.onAllPlayersArrived();
            }
        }*/
    }

    public List<God> getAvailableGods() {
        return new ArrayList<>(godsList);
    }

    public List<String> getPlayersNicknames() {
        return queue.stream()
                .map(Player::getNickname)
                .collect(Collectors.toList());
    }

    public void setGods(List<String> gods) {
        this.godsList =
                godsList.stream()
                .filter(god -> gods.contains(god.getName()))
                .collect(Collectors.toList());

        nextPlayer();
    }

    public void setPlayerColor(Player p, Color c) throws IllegalArgumentException {
        //Check that player p is part of the game
        if( !(this.queue.contains(p))){
            throw new IllegalArgumentException("Given Player is not part of the game");
        }

        //If color has been choose by another player, throw exception
        if(!this.colors.contains(c))
            throw new IllegalArgumentException("Chosen color is not available any longer.");

        p.setWorkerColor(c);
        this.colors.remove(c);
    }

    public void assignGodToPlayer(Player p, God g) throws IllegalArgumentException {
        //Check that player p is part of the game
        if( !(this.queue.contains(p))){
            throw new IllegalArgumentException("Given Player is not part of the game.");
        }

        //If god has been chosen by another player, throw exception
        if(!this.godsList.contains(g))
            throw new IllegalArgumentException("Chosen god has been previously chosen by another player " +
                    "or has never been selected by Challenger.");

        p.setGod(g);
        this.godsList.remove(g);
        if (!godsList.isEmpty()) { // If all players have chosen, keep the Challenger as currentPlayer
            nextPlayer();
        }
    }

    public void setStartPlayer(Player startPlayer) throws IllegalArgumentException {

        if(!queue.contains(startPlayer))
            throw new IllegalArgumentException("Chosen player is not in the game.");

        boolean ordered = false;
        if (startPlayer.equals(queue.get(0))) ordered = true;
        while (!ordered) {
            Player moved = queue.remove(0);
            queue.add(moved);
            if (startPlayer.equals(queue.get(0))) ordered = true;
        }

        currentPlayer = startPlayer;
        currentPlayer.setAsStartPlayer();
    }

    //potrebbe ricevere (String Player, Coord C) anzichè il worker
    public void initializeWorker(Coord c) {
        /*
        if (!w.getPlayerNickname().equals(currentPlayer.getNickname())) {
            throw new IllegalStateException("Tried to initialize a worker not " +
                    "belonging to current player.");
        }
         */

        board.initializeWorker(currentPlayer, c);
        notifyBoardChanged();

        if (currentPlayer.getWorkersList().stream()
                .noneMatch(worker -> worker.getPosition() == null)
        ) {
            nextPlayer();
        }
    }

    public void initializeWorker(Worker w, Coord c) {
        if (!currentPlayer.getWorkersList().contains(w)) {
            throw new IllegalStateException("Tried to initialize a worker not " +
                    "belonging to current player.");
        }

        board.initializeWorker(w, c);

        if (currentPlayer.getWorkersList().stream()
                .noneMatch(worker -> worker.getPosition() == null)
        ) {
            nextPlayer();
        }
    }

    //GAME FUNCTIONS//
    void initRequestHandlers() {
        queue.forEach(p ->
                handlers.put(p,
                        new RequestHandlerCreator(p.getGod().getName())
                                .createHandler()
                )
        );
    }

    public Player getPlayerByNickname(String nick) throws IllegalArgumentException {

        Player res = queue.stream().filter(p -> p.getNickname().equals(nick)).findFirst().orElse(null);

        if (res != null) {
            return res;
        } else {
            throw new IllegalArgumentException("There is no player with the provided name.");
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setWorkerChoice(Coord workerPos) {
        Worker selected = board.getWorkerByPosition(workerPos);

        //Check that the worker in workerPos belongs to currentPlayer
        if( !(selected.getPlayerNickname().equals(currentPlayer.getNickname())) ) {
            throw new IllegalWorkerChoiceException("Selected worker does not belong to the current player");
        }

        currentWorker = selected;
        turn.setInitialBoard(board.clone());
    }

    public void setMove(Coord moveChoice) {
        if (!turn.getMovableSpacesCopy().contains(moveChoice)) {
            notifyAction();
            return;
        }

        //TODO: Implementare moveWithForce() nella Board
        board.getSpace(currentWorker.getPosition()).setUnoccupied();
        if (turn.getForcesCopy().containsKey(moveChoice)) {
            Coord forceDest = turn.getForcesCopy().get(moveChoice);
            board.workerMove(moveChoice, forceDest);
        }

        board.workerMove(currentWorker, moveChoice);
        notifyBoardChanged();

        turn.hasMoved();

        handlers.get(currentPlayer).generate(moveChoice, ActionType.MOVE);


    }

    public void setBuild(Coord buildChoice, Level level) {
        if (!turn.getBuildableSpacesCopy().get(level).contains(buildChoice)) {
            notifyAction();
            return;
        }
        board.workerBuild(currentWorker, buildChoice, level);
        notifyBoardChanged();

        turn.hasBuilt();

        handlers.get(currentPlayer).generate(buildChoice, ActionType.BUILD);
    }

    public void setEnd() {
        // Logica di aggiornamento del currentPlayer
        if (turn.hasEnded()) {
            turn.reset();
            handlers.get(currentPlayer).reset();
            currentWorker = null;
            nextPlayer();
        } else {
            turn.setEnded();
            Coord currPosition = currentWorker.getPosition();
            handlers.get(currentPlayer).generate(currPosition, ActionType.END);
        }
    }

    public void setWin(Player p) throws IllegalArgumentException {
        //Check that player p is part of the game
        if( !(this.queue.contains(p)) ){
            throw new IllegalArgumentException("Given Player is not part of the game");
        }

        p.win();
        //TODO: End of the Game
    }

    public void nextPlayer() {
        //Check the game is ready
        if( this.allPlayersArrived() ) {
            this.queue.remove(currentPlayer);
            this.queue.add(currentPlayer);
            currentPlayer = this.queue.get(0);
        }
    }

    //?????? perchè in player c'è flag isStartPlayer?? va contro principio di incapsulamento
    public boolean hasNewCycleBegun() {
        return currentPlayer.isStartPlayer();
    }

    public boolean hasNewTurnBegun() {
        return currentWorker == null;
    }

    void nextAction() {
        RequestHandler currHandler = handlers.get(currentPlayer);
        Coord currentPosition = currentWorker.getPosition();
        currHandler.getValidSpaces(currentPosition, board.clone(),
                turn.getMovableSpacesReference(), turn.getBuildableSpacesReference(),
                turn.getForcesReference());

        if (turn.getMovableSpacesCopy().isEmpty() &&
                turn.getBuildableSpacesCopy().values().isEmpty()) { // <-- this is wrong
            if (!turn.canEndTurn()) {
                board = turn.getInitialBoard();
                notifyBoardChanged();
                turn.reset();
                currHandler.reset();
                nextAction();
                return;
            }
            setEnd();
            if (hasNewTurnBegun()) {
                nextStep();
                return;
            } else {
                nextAction();
            }
        }

        notifyAction();

    }

    private void notifyAction() {
        ModelEventListener listener = getListenerByNickname(currentPlayer.getNickname());
        listener.onMyAction(turn.getMovableSpacesCopy(), turn.getBuildableSpacesCopy(),
                turn.canEndTurn());
    }

    private void notifyBoardChanged() {
        modelListeners.forEach(l -> l.onBoardChanged(board.clone()));
    }

    //INTERROGAZIONI DALLE VIEW   VANNO TOLTEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE !!!!!!!!!!!!!!!!!
    public Board getBoard(){
        return this.board.clone();
    }

    public ArrayList<Worker> getAllWorkers(){
        ArrayList<Worker> workers = new ArrayList<>();
        for(Player p: this.queue){
            workers.add(p.getWorker(0));
            workers.add(p.getWorker(1));
        }
        return workers;
    }

    public int getQueueState(){
        return queue.size();
    }

    public List<Color> getAvailableColors(){
        return this.colors;
    }

    @Override
    public void addListener(Listener listener) {
        if (!(listener instanceof ModelEventListener)) {
            throw new IllegalArgumentException("Tried to register a non-ModelEventListener to Model");
        }
        modelListeners.add((ModelEventListener) listener);
    }

    ModelEventListener getListenerByNickname(String nickname) {
        return modelListeners.stream()
                .filter(listener -> listener.getNickname().equals(nickname))
                .findFirst().orElse(null);
    }

    List<Worker> getSelectableWorkers() {
        RequestHandler currHandler = handlers.get(currentPlayer);
        List<Worker> selectableWorkers = new ArrayList<>();
        boolean selectable = false;
        for (Worker worker : currentPlayer.getWorkersList()) {
            Coord position = worker.getPosition();
            currHandler.getValidSpaces(position, board.clone(),
                    turn.getMovableSpacesReference(), turn.getBuildableSpacesReference(),
                    turn.getForcesReference());
            if (!turn.getMovableSpacesCopy().isEmpty()) {
                if (currentPlayer.getGod().getName().equals("Apollo")) {
                    selectable = turn.getMovableSpacesCopy().stream()
                            .map(c -> board.getSpace(c))
                            .anyMatch(s -> !s.isOccupied());
                    if (!selectable) {
                        for (Coord moveChoice : turn.getMovableSpacesCopy()) {
                            selectable = board.getSpacesAround(moveChoice).stream()
                                    .map(c -> board.getSpace(c))
                                    .anyMatch(s -> !s.isOccupied() && !s.isDome());
                            if (selectable) {
                                break;
                            }
                        }
                    }
                } else {
                    selectable = true;
                }
            }
            if (selectable) {
                selectableWorkers.add(worker);
            }
        }
        return selectableWorkers;
    }

    public void quandoPerdo() {
        // Assumptions: a player cannot destroy before moving
        // Under this assumption,
        // if he cannot move as first action, then he cannot move even if he first builds.
        // Therefore, let's check if he can move.
        List<Worker> selectableWorkers = new ArrayList<>();
        RequestHandler currHandler = handlers.get(currentPlayer);

        for (Worker worker : currentPlayer.getWorkersList()) {
            currentWorker = worker;
            Coord currentPosition = currentWorker.getPosition();
            Turn originalTurn = turn;
            turn = new Turn();
            currHandler.getValidSpaces(currentPosition, board.clone(),
                    turn.getMovableSpacesReference(), turn.getBuildableSpacesReference(),
                    turn.getForcesReference());
            if (turn.getMovableSpacesCopy().isEmpty()) {
                continue;
            }
            for (Coord moveChoice : turn.getMovableSpacesCopy()) {
                Board originalBoard = board;
                board = board.clone();
                setMove(moveChoice);
                Turn turnBeforeGenerate = turn;
                turn = turn.clone();
                currHandler.getValidSpaces(currentPosition, board.clone(),
                        turn.getMovableSpacesReference(), turn.getBuildableSpacesReference(),
                        turn.getForcesReference());
                // Assumption: a worker can't destroy before a real build
                // Under this assumption, if he can do a build, then it is a real build
                if (!turn.getBuildableSpacesCopy().values().isEmpty()) {
                    selectableWorkers.add(currentWorker);
                    break;
                }
                turn = turnBeforeGenerate;
            }
        }
    }
}

