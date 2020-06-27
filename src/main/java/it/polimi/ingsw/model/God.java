package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;

public class God implements Serializable {

    private static final long serialVersionUID = 2L;

    private final String name;
    private final String description;

    public God(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Get the name of this god
     * @return
     */
    public String getName() { return name; }

    /**
     * Get the description of this god
     * @return
     */
    public String getDescription() { return description; }

    /**
     * Equality check
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof God){
            God other = (God)o;
            return other.getName().equals(this.getName());
        }
        return false;
    }

}
