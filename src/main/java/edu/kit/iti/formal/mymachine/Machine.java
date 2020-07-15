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

import com.thoughtworks.xstream.XStream;
import edu.kit.iti.formal.mymachine.automata.State;
import edu.kit.iti.formal.mymachine.automata.Transition;
import edu.kit.iti.formal.mymachine.panel.MachineElement;
import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;


/**
 * This is the main model class that holds the business date:
 * - panel elements
 * - states
 * - transitions
 * - the current transition.
 *
 * Operations on these data are defined here. This class is also used for serialisation.
 */
public class Machine implements Serializable {

    /**
     * A link to the frame of this application.
     * Not to be serialised
     */
    private transient MainFrame mainFrame;

    /**
     * A collection of all machine (=panel) elements. They are indexed by their name.
     */
    private Map<String, MachineElement> machineElements = new HashMap<>();

    /**
     * A collection of all automata states. They are indexed by their name.
     */
    private Map<String, State> states = new HashMap<>();

    /**
     * The currently active state while in play mode. Should be null while not in
     * play mode.
     */
    private State activeState;

    /**
     * The collection of transitions. not indexed.
     */
    private List<Transition> transitions = new ArrayList<>();

    /**
     * The flag for the play mode. Observers can be added to the flag.
     */
    private transient final BooleanObservable playModeObservable = new BooleanObservable();

    private final List<String> displayStrings = new ArrayList<>();

    /**
     * Make a new, empty model.
     */
    public Machine() {
        this.mainFrame = new MainFrame(this);
        addPlaymodeObserver(playmode -> {
            if (playmode.get()) {
                activeState = getStartState();
            } else {
                activeState = null;
            }
            mainFrame.revalidate();
            mainFrame.repaint();
        });
    }

    /**
     * The entry point of this application.
     *
     * @param args command line args
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Machine machine = new Machine();

        if (args.length > 0) {
            machine.loadScenario(new File(args[0]));
        }

        machine.mainFrame.setSize(700,700);
        machine.mainFrame.setLocationRelativeTo(null);
        machine.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        machine.mainFrame.setVisible(true);
    }

    /**
     * Get an immutable collection of all existing machine elements
     *
     * @return an immutable, but iterable list.
     */
    public Collection<MachineElement> getMachineElements() {
        return machineElements.values();
    }

    /**
     * Get an immutable collection of all existing automaton states
     *
     * @return an immutable, but iterable list.
     */
    public Collection<State> getStates() {
        return states.values();
    }

    /**
     * Get the currently active state in playmode, null otherwise.
     *
     * @return a state in {@link #getStates()} or null.
     */
    public State getActiveState() {
        return activeState;
    }

    /**
     * Get the start state.
     * The start state is the state named "Start".
     *
     * @return start state, or null if non-existent.
     */
    public State getStartState() {
        return states.get("Start");
    }

    /**
     * Get an immutable collection of all existing machine elements.
     *
     * @return an immutable, but iterable list.
     */
    public List<Transition> getTransitions() {
        return Collections.unmodifiableList(transitions);
    }

    /**
     * Are we in play mode?
     *
     * @return true iff we are in play mode.
     */
    public boolean isPlayMode() {
        return playModeObservable.get();
    }

    /**
     * Set the playmode.
     *
     * If this is to be set to true, make sure that:
     * 1. there is a start state
     * 2. There is no indeterminism in the automaton
     *
     * @param playMode true iff playmode should be entered
     */
    public void setPlayMode(boolean playMode) {
        assert !playMode || (getStartState() != null && findIndeterminism() == null);
        playModeObservable.set(playMode);
    }

    /**
     * Find a state such that there are indeterministic transitions.
     *
     * Indeterminism happens if two transitions from the same state have the same
     * triggering event.
     *
     * @return null if deterministic, a culprit state otherwise.
     */
    public State findIndeterminism() {
        // TODO make this real ...
        for (Transition t1 : transitions) {
            for (Transition t2 : transitions) {
                if(t1 != t2 && t1.getFrom() == t2.getFrom() &&
                        t1.getTrigger() == t2.getTrigger())
                    return t1.getFrom();
            }
        }

        return null;
    }

    /**
     * Add an observer to the playmode
     *
     * @param observer a listener which is called whenever the value changes.
     */
    public void addPlaymodeObserver(Consumer<BooleanObservable> observer) {
        playModeObservable.addObserver(observer);
    }

    /**
     * Trigger an action from the panel. The user has clicked on a machine element e.g.
     *
     * This finds the transition which belongs to this action and changes the active state.
     *
     * @param element the trigger action to perform.
     */
    public void fire(MachineElement element) {
        if(activeState != null) {
            for (Transition trans : getTransitions()) {
                if (trans.getFrom() == activeState && trans.getTrigger() == element) {
                    activeState = trans.getTo();
                    MachineElement output = trans.getOutput();
                    if (output != null) {
                        output.react(trans.getMessageIndex());
                    }
                    output = trans.getOutput2();
                    if (output != null) {
                        output.react(trans.getMessageIndex2());
                    }
                    // Command has been processed. Do not look further.
                    mainFrame.repaint();
                    break;
                }
            }
        }
    }

    public void addMachineElement(MachineElement element) {
        machineElements.put(element.getName(), element);
    }

    public MachineElement getMachineElement(String name) {
        return machineElements.get(name);
    }

    /**
     * Load the business data from an xml file.
     *
     * There are corrupt xml files which may make this crash horribly.
     * Please only use xml files saved with the very same version of this
     * application.
     *
     * @param file the file to load from
     * @throws IOException if reading fails
     * @throws ClassNotFoundException if a class is missing.
     */
    public void loadScenario(File file) throws IOException, ClassNotFoundException {
        XStream xstream = new XStream();
        xstream.setMode(XStream.ID_REFERENCES);
        try (ObjectInputStream ois = xstream.createObjectInputStream(new FileInputStream(file))) {
            Machine newMachine = (Machine) ois.readObject();
            this.activeState = null;
            this.states = newMachine.states;
            this.machineElements = newMachine.machineElements;
            this.transitions = newMachine.transitions;
            this.setPlayMode(false);
            mainFrame.repaint();
        }
    }

    /**
     * Save the business data to a file.
     *
     * The data itself is not modified.
     *
     * @param file the file to save to
     * @throws IOException if file writing fails.
     */
    public void saveScenario(File file) throws IOException {
        XStream xstream = new XStream();
        xstream.setMode(XStream.ID_REFERENCES);
        try(ObjectOutputStream oos = xstream.createObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
        }
    }

    public void addState(State state) {
        assert !states.containsKey(state.getName());
        states.put(state.getName(), state);
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);
    }

    public State getState(String name) {
        return states.get(name);
    }

    public List<String> getDisplayStrings() {
        return displayStrings;
    }

    /**
     * Reset the business data of this object. All transitions, states,
     * panel elements are removed. Remove the active state and leave the play mode.
     */
    public void reset() {
        this.transitions.clear();
        this.states.clear();
        this.machineElements.clear();
        this.displayStrings.clear();
        this.activeState = null;
        this.setPlayMode(false);
    }

    public void removeTransition(Transition trans) {
        this.transitions.remove(trans);
    }

    public void dump() {
        System.out.println("States:");
        System.out.println("  " + states.values());

        System.out.println("Active state: " + activeState);

        System.out.println("Transitions:");
        for (Transition transition : transitions) {
            System.out.println("  " + transition.getFrom() + " -> " + transition.getTo());
            System.out.println("     " + transition.getOutput() + " " + transition.getMessageIndex());
            System.out.println("     " + transition.getOutput2() + " " + transition.getMessageIndex2());
        }
    }

    public void setDisplayStrings(Collection<String> strings) {
        displayStrings.clear();
        displayStrings.addAll(strings);
    }
}
