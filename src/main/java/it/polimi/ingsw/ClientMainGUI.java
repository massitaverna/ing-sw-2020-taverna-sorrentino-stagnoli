package it.polimi.ingsw;

import it.polimi.ingsw.gui.Board;
import it.polimi.ingsw.gui.Home;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientMainGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //load fxml file
        final String fxmlResource = "/home.fxml";
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlResource));
        Parent root = loader.load();

        //if loaded successfully
        if( ((Home)loader.getController()).isConnected() ){
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.sizeToScene();
            primaryStage.setTitle("Santorini Game");
            primaryStage.setOnCloseRequest(windowEvent -> {
                System.exit(0);
            });
            primaryStage.show();
        }

    }
}
