/*
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BooleanObservable {

    private List<Consumer<BooleanObservable>> observers = new ArrayList<>();

    private boolean value;

    public boolean get() {
        return value;
    }

    public void set(boolean value) {
        if (value != this.value) {
            this.value = value;
            for (Consumer<BooleanObservable> observer : observers) {
                observer.accept(this);
            }
        }
    }

    public void addObserver(Consumer<BooleanObservable> observer) {
        observers.add(observer);
    }

}
