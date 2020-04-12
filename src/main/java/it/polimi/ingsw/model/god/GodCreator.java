package it.polimi.ingsw.model.god;

import it.polimi.ingsw.model.god.God;

public class GodCreator {
    public God createGod(String godName) {
        switch (godName) {
            case "Apollo":
                return new Apollo();
            case "Athena":
                return new Athena();
            default:
                throw new IllegalArgumentException("Unknown god name.");
        }
    }
}
