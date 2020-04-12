//mod
package it.polimi.ingsw.model.god;

import it.polimi.ingsw.model.Player;

public interface God {


    public String getDescription();

    // potrebbe voler prendere un array di player
    public void applyEffect(Player player);

    public boolean hasCondition();

    public String getName();

    //metodo per ottenere la condizione
}
