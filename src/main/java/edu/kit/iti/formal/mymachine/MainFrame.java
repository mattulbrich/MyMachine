package edu.kit.iti.formal.mymachine;

import edu.kit.iti.formal.mymachine.automata.AutomataEditor;
import edu.kit.iti.formal.mymachine.automata.State;
import edu.kit.iti.formal.mymachine.automata.Transition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

    private DesignPane designPane;
    private Map<String, MachineElement> machineElements = new HashMap<>();
    private List<State> states = new ArrayList<>();
    private State activeState;
    private final BooleanObservable playModeObservable = new BooleanObservable();
    private List<Transition> transitions = new ArrayList<>();

    public MainFrame() {
        super("MyMachine");
        init();

        State s1 = new State("Z1", new Point(200, 200));
        states.add(s1);
        State s2 = new State("Z2", new Point(300, 230));
        states.add(s2);
        transitions.add(new Transition(s1, s2, "K", "#Display Hello World!"));

        {
            Button element = new Button();
            element.setPosition(new Point(300, 300));
            element.setName("K");
            addMachineElement(element);
        }
        {
            Display element = new Display();
            element.setPosition(new Point(300, 500));
            addMachineElement(element);
        }
    }

    private void init() {
        setContentPane(new JTabbedPane());
        getContentPane().add(new DesignPane(this), "G E H Ä U S E");
        getContentPane().add(new AutomataEditor(this), "A U T O M A T");

        playModeObservable.addObserver((s,o) -> {
            if ((Boolean) o) {
                activeState = states.get(0);
            } else {
                activeState = null;
            }
            revalidate();
            repaint();
        });
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
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
        repaint();
    }

    public void addMachineElement(MachineElement element) {
        machineElements.put(element.getName(), element);
    }

    public MachineElement getMachineElement(String name) {
        return machineElements.get(name);
    }
}
