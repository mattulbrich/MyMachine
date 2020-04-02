package edu.kit.iti.formal.mymachine;

import java.util.Observable;

public class BooleanObservable extends Observable {

    private boolean value;

    public boolean get() {
        return value;
    }

    public void set(boolean value) {
        if (value != this.value) {
            this.value = value;
            setChanged();
            notifyObservers(Boolean.valueOf(value));
        }
    }

}
