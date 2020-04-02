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

import com.thoughtworks.xstream.XStream;
import edu.kit.iti.formal.mymachine.automata.State;
import edu.kit.iti.formal.mymachine.automata.Transition;
import edu.kit.iti.formal.mymachine.panel.MachineElement;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Machine {

    private transient MainFrame mainFrame;
    private Map<String, MachineElement> machineElements = new HashMap<>();
    private List<State> states = new ArrayList<>();
    private State activeState;
    private transient final BooleanObservable playModeObservable = new BooleanObservable();
    private List<Transition> transitions = new ArrayList<>();

    public Machine() {
        this.mainFrame = new MainFrame(this);
        playModeObservable.addObserver((s,o) -> {
            if ((Boolean) o) {
                activeState = states.get(0);
            } else {
                activeState = null;
            }
            mainFrame.revalidate();
            mainFrame.repaint();
        });
    }

    public static void main(String[] args) {
        Machine machine = new Machine();
        machine.mainFrame.setSize(700,700);
        machine.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        machine.mainFrame.setVisible(true);
    }

    public Collection<MachineElement> getMachineElements() {
        return machineElements.values();
    }

    public List<State> getStates() {
        return states;
    }

    public State getActiveState() {
        return activeState;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public boolean isPlayMode() {
        return playModeObservable.get();
    }

    public void setPlayMode(boolean playMode) {
        playModeObservable.set(playMode);
    }

    public Observable getPlayModeObservable() {
        return playModeObservable;
    }

    public void fire(String command) {
        if(activeState != null) {
            for (Transition trans : getTransitions()) {
                if (trans.getFrom() == activeState && trans.getIn().equals(command)) {
                    activeState = trans.getTo();
                    output(trans.getOut());
                }
            }
        }
    }

    private void output(String out) {
        if (out.isEmpty()) {
            return;
        }
        String[] parts = out.split(" ", 2);
        MachineElement element = machineElements.get(parts[0]);
        if(element != null) {
            element.output(parts[1]);
        } else {
            throw new NoSuchElementException("Unknown element in command '" + out + "'");
        }
        mainFrame.repaint();
    }

    public void addMachineElement(MachineElement element) {
        machineElements.put(element.getName(), element);
    }

    public MachineElement getMachineElement(String name) {
        return machineElements.get(name);
    }

    public void loadScenario(File file) throws IOException, ClassNotFoundException {
        XStream xstream = new XStream();
        xstream.setMode(XStream.ID_REFERENCES);
        try (ObjectInputStream ois = xstream.createObjectInputStream(new FileInputStream(file))) {
            Machine newMachine = (Machine) ois.readObject();
            this.activeState = null;
            this.states = newMachine.states;
            this.machineElements = newMachine.machineElements;
            this.transitions = newMachine.transitions;
            mainFrame.repaint();
        }
    }

    public void saveScenario(File file) throws IOException {
        XStream xstream = new XStream();
        xstream.setMode(XStream.ID_REFERENCES);
        try(ObjectOutputStream oos = xstream.createObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
        }
    }
}
