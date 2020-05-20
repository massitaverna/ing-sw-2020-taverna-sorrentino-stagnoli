package it.polimi.ingsw.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientMainGUI extends Application{

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

        //if loaded succesfully
        if( ((Home)loader.getController()).isConnected() ){
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.sizeToScene();
            primaryStage.setTitle("Santorini Game");
            /*primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override public void handle(WindowEvent t) {
                    ((Home)loader.getController()).closeConnection();
                }
            });*/
            primaryStage.show();
        }

    }
}
