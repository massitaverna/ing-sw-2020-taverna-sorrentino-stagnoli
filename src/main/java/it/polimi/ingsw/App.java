package it.polimi.ingsw;

import it.polimi.ingsw.controller.RealController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.view.PlayerView;

public class App {
    public static void main() {
        GameModel model = new GameModel();
        RealController controller = new RealController(model);
        //Wrong!!! views can't take model as constructor parameters...
        PlayerView view1 = new PlayerView(model);
        PlayerView view2 = new PlayerView(model);
        PlayerView view3 = new PlayerView(model);


    }
}
