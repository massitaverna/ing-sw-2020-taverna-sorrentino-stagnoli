package it.polimi.ingsw.view.listeners.Model;

import it.polimi.ingsw.view.View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;
import java.util.Scanner;

public class TurnListener implements PropertyChangeListener {

    private View view;
    private Scanner s;
    private PrintWriter output;

    public TurnListener(View view){
        this.view = view;
        s = new Scanner(System.in);
        output = new PrintWriter(System.out);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getNewValue().equals(view.getNick())){
            output.println("Where do you want to move? ");
            String choice = s.nextLine();
            view.setChoice(choice);
            output.println("Where do you want to build? ");
            choice = s.nextLine();
            view.setChoice(choice);
        }
    }
}
