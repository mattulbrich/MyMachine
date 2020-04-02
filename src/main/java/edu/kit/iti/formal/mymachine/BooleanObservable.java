/**
 * This file is part of the tool MyMachine.
 * https://github.com/mattulbrich/MyMachine
 *
 * MyMachine is a simple visualisation tool to learn finite state
 * machines.
 *
 * The system is protected by the GNU General Public License Version 3.
 * See the file LICENSE in the main directory of the project.
 *
 * (c) 2020 Karlsruhe Institute of Technology
 */
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
