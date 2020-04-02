package edu.kit.iti.formal.mymachine;

import edu.kit.iti.formal.mymachine.automata.AutomataEditor;
import edu.kit.iti.formal.mymachine.automata.State;
import edu.kit.iti.formal.mymachine.automata.Transition;
import edu.kit.iti.formal.mymachine.panel.DesignPane;
import edu.kit.iti.formal.mymachine.panel.MachineElement;

import javax.swing.*;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

    private DesignPane designPane;
    private Map<String, MachineElement> machineElements = new HashMap<>();
    private List<State> states = new ArrayList<>();
    private State activeState;
    private final BooleanObservable playModeObservable = new BooleanObservable();
    private List<Transition> transitions = new ArrayList<>();
    private Machine machine;

    public MainFrame(Machine machine) {
        super("MyMachine");
        this.machine = machine;
        init();
    }

    private void init() {
        setContentPane(new JTabbedPane());
        getContentPane().add(new DesignPane(machine), "G E H Ä U S E");
        getContentPane().add(new AutomataEditor(machine), "A U T O M A T");
    }
}
