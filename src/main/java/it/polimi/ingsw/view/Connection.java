//mod
package it.polimi.ingsw.view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Connection {

    public Connection(PropertyChangeSupport support) {
        this.support = support;
    }
    private PropertyChangeSupport support;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);

    }

    // When receiving a PropertyChangeEvent evt from network, call:
    // support.firePropertyChange(evt);
}
