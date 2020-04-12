package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Scanner;

public class MassiProvaCoseView {
    private int numPlayers;
    private Player player;
    private String god;
    private int color;
    private Connection connection;
    private PropertyChangeSupport support;

    private class EventsReceiver implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            //Unpack event
            //Set property
            support.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            //
        }
    }

    public MassiProvaCoseView(Player player, Connection connection) {
        this.player = player;
        this.connection = connection;
        this.support = new PropertyChangeSupport(this);
        connection.addPropertyChangeListener(new EventsReceiver());
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);

    }
    public Player getPlayer() {
        return player;
    }
    public String getNickname() {
        return player.getNickname(); // Needs changing: RemoteView shouldn't access Player
    }

}
