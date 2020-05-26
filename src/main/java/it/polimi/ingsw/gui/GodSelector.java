package it.polimi.ingsw.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class GodSelector implements Initializable {

    public ImageView godImage, activeImage;
    private boolean clicked;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        activeImage.setVisible(false);
    }

    @FXML
    public void toggleSelection(MouseEvent event){
        activeImage.setVisible(!clicked);
        clicked = !clicked;
    }

    public void setGodImage(Image img){
        this.godImage.setImage(img);
    }

    public boolean isClicked(){
        return clicked;
    }


}
