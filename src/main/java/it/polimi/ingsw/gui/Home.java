package it.polimi.ingsw.gui;

import it.polimi.ingsw.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Home implements Initializable {

    private String lobbyToString(int maxNumPlayers, List<String> players, int number){
            String rep = number + " - ";
            for (String p: players){
                rep += "\"" + p + "\" ";
            }
            rep += "(" + players.size() + "/" + maxNumPlayers + ")";
            return rep;
    }

    public AnchorPane homePane, numPlayersPane, nicknamePane, lobbyListPane;
    public Label numPlayersError;
    public TextField numPlayersTextField, nicknameTextField;
    public ListView<String> lobbyList;
    public ImageView lobbyListNext;

    private boolean createGame = false;
    Map<Integer, List<String>> availableLobbies = new HashMap<>();
    Map<Integer, Integer> availableLobbiesMaxPlayers = new HashMap<>();
    String nickname;
    int numPlayers;

    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    private boolean isConnected;

    public Home(){
        try {
            socket = new Socket("127.0.0.1", 12345);
            socket.setKeepAlive(true);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            isConnected = true;
        } catch (IOException e) {
            System.out.println("Connection to the server failed.");
            return;
        }
    }

    public boolean isConnected(){ return this.isConnected; }

    public void close(){
        try {
            System.out.println("Connection Lost, closing...");
            this.socket.close();
            this.in.close();
            this.out.close();
            ((Stage)homePane.getScene().getWindow()).close();
        } catch (IOException e) { }
    }

    @FXML
    public void createGame(MouseEvent event){
        this.createGame = true;
        this.homePane.setVisible(false);
        this.nicknamePane.setVisible(true);
    }

    @FXML
    public void joinGame(MouseEvent event){
        this.createGame = false;
        this.homePane.setVisible(false);
        this.lobbyListPane.setVisible(true);
        this.lobbyList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.lobbyList.setEditable(false);
        this.lobbyListNext.setVisible(false);

        try {
            out.writeObject("?lobbies");
            out.flush();
            int numLobbies = (int)in.readObject();
            for (int i = 0; i < numLobbies; i++) {
                List<String> playersInLobby = (List<String>) in.readObject();
                int maxPlayers = (int) in.readObject();
                int lobbyNum = (int) in.readObject();
                this.availableLobbies.put(lobbyNum, playersInLobby);
                this.availableLobbiesMaxPlayers.put(lobbyNum, maxPlayers);
            }
            for (Integer i : availableLobbies.keySet()) {
                int maxP;
                maxP = availableLobbiesMaxPlayers.get(i);
                this.lobbyList.getItems().add(lobbyToString(maxP, availableLobbies.get(i), i+1));
                this.lobbyList.getSelectionModel().selectFirst();
                this.lobbyListNext.setVisible(true);
            }
        } catch (IOException | ClassNotFoundException e) {
            close();
        }

    }

    @FXML
    public void lobbyListNext(MouseEvent event){
        if(availableLobbies.size() > 0) {

            int lobbyIndex = this.lobbyList.getSelectionModel().getSelectedIndex() + 1;
            try {
                out.writeObject("lobbySelected");
                out.flush();
                out.writeObject(lobbyIndex);
                out.flush();
                String res = (String)in.readObject();
                if(res.equals("lobbySelectedOK")){
                    this.createGame = false;
                    this.homePane.setVisible(false);
                    this.lobbyListPane.setVisible(false);
                    this.nicknamePane.setVisible(true);
                }
                //only the confirm
                res = (String)in.readObject();
                res.toLowerCase();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void nicknameChosen(MouseEvent event){
        nickname = this.nicknameTextField.getText();
        if(!nickname.equals("")){
            this.nicknamePane.setVisible(false);

            if(this.createGame){
                this.numPlayersPane.setVisible(true);
                return;
            }
            try {
                out.writeObject("nicknameSelected");
                out.flush();
                out.writeObject(nickname);
                out.flush();

                String res = (String)in.readObject();
                if(res.equals("ok")){
                    System.out.println("lobby joined from GUI");
                }
                else{ //lobby is full
                    this.createGame = false;
                    this.homePane.setVisible(true);
                    this.nicknamePane.setVisible(false);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            //TODO SHow error
        }

    }

    @FXML
    public void opponentsChosen(MouseEvent event){
        int num = -1;
        try{
            num = Integer.parseInt(numPlayersTextField.getText());
        }catch (Exception e){
            numPlayersError.setVisible(true);
        }
        if(num == 1 || num == 2){
            numPlayersError.setVisible(false);
            num++;
            numPlayers = num;
            //TODO: create the lobby
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
