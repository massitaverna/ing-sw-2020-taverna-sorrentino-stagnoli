package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Player;

public class God {

    private final String name;
    private final String description;

    public God(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }


}
