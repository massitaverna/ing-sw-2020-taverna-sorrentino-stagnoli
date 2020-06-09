package it.polimi.ingsw.gui;

import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GodsPopup implements Initializable {

    public AnchorPane pane;
    private int numPlayers;

    public CheckBox apolloCheck, hephaestusCheck, artemisCheck, demeterCheck, atlasCheck, athenaCheck, minotaurCheck, panCheck, prometheusCheck;
    public ImageView okBtn;
    private final List<CheckBox> boxes = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boxes.add(apolloCheck);
        boxes.add(hephaestusCheck);
        boxes.add(artemisCheck);
        boxes.add(demeterCheck);
        boxes.add(atlasCheck);
        boxes.add(athenaCheck);
        boxes.add(minotaurCheck);
        boxes.add(panCheck);
        boxes.add(prometheusCheck);
        okBtn.setVisible(false);
    }

    public void setNumPlayers(int num){
        this.numPlayers = num;
    }

    public List<String> getChoices(){

        List<String> gods = new ArrayList<>();

        for (CheckBox box : boxes) {
            if (box.isSelected())
                gods.add(box.getText().trim());
        }

        return gods;
    }

    private int countChecked(){
        int count = 0;
        for (CheckBox box : boxes){
            if (box.isSelected())
                count++;
        }
        return count;
    }

    private void enableAll(){
        for (CheckBox box : boxes){
            box.setVisible(true);
        }
    }

    public void checkBoxClicked(MouseEvent mouseEvent) {
        if (countChecked() == numPlayers){
            for (CheckBox box : boxes){
                if (!box.isSelected())
                    box.setVisible(false);
            }
            okBtn.setVisible(true);
        } else {
            enableAll();
            okBtn.setVisible(false);
        }
    }

    public void confirm(MouseEvent mouseEvent) {
        ((Stage)pane.getScene().getWindow()).close();
    }
}
