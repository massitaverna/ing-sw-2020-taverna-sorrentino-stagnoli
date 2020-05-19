package it.polimi.ingsw.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable {

    public AnchorPane homePane, numPlayersPane, nicknamePane, lobbyListPane;

    private boolean createGame = false;

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
    }

    @FXML
    public void nicknameChosen(MouseEvent event){
        this.nicknamePane.setVisible(false);
        if(this.createGame){
            this.numPlayersPane.setVisible(true);
        }else{
            //TODO: enter the lobby
        }
    }

    @FXML
    public void opponentsChosen(MouseEvent event){
        //TODO: create the lobby
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
