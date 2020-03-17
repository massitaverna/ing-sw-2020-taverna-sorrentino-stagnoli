package it.polimi.ingsw.model;

public interface God {

    public String getDescription();

    // potrebbe voler prendere un array di player
    public void applyEffect(Player player);

    public boolean hasCondition();

    //metodo per ottenere la condizione
}
