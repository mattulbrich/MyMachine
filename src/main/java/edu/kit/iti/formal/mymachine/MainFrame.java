package edu.kit.iti.formal.mymachine;

import edu.kit.iti.formal.mymachine.automata.AutomataEditor;
import edu.kit.iti.formal.mymachine.automata.State;
import edu.kit.iti.formal.mymachine.automata.Transition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class MainFrame extends JFrame {

//    private StateMachinePane stateMachinePane;
//    private PlayPane playPane;
    private DesignPane designPane;
    private List<MachineElement> machineElements = new ArrayList<>();
    private List<State> states = new ArrayList<>();
    private State activeState;
    private final BooleanObservable playModeObservable = new BooleanObservable();
    private List<Transition> transitions = new ArrayList<>();

    public MainFrame() {
        super("MyMachine");
        init();
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




    public List<MachineElement> getMachineElements() {
        return machineElements;
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
        for (MachineElement element : machineElements) {
            element.output(out);
        }
        repaint();
    }
}
