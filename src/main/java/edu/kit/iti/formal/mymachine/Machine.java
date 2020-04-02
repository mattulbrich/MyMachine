package edu.kit.iti.formal.mymachine;

import edu.kit.iti.formal.mymachine.automata.AutomataEditor;
import edu.kit.iti.formal.mymachine.automata.State;
import edu.kit.iti.formal.mymachine.automata.Transition;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Machine {

    private MainFrame mainFrame;
    private Map<String, MachineElement> machineElements = new HashMap<>();
    private List<State> states = new ArrayList<>();
    private State activeState;
    private final BooleanObservable playModeObservable = new BooleanObservable();
    private List<Transition> transitions = new ArrayList<>();

    public Machine() {
        this.mainFrame = new MainFrame(this);
    }

    private void init() {
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
        String[] parts = out.split(" ", 2);
        MachineElement element = machineElements.get(parts[0]);
        if(element != null) {
            element.output(parts[1]);
        } else {
            throw new NoSuchElementException("Unknown element " + parts[0]);
        }
        mainFrame.repaint();
    }

    public void addMachineElement(MachineElement element) {
        machineElements.put(element.getName(), element);
    }

    public MachineElement getMachineElement(String name) {
        return machineElements.get(name);
    }
}
