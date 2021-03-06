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

import edu.kit.iti.formal.mymachine.automata.State;
import edu.kit.iti.formal.mymachine.automata.Transition;
import edu.kit.iti.formal.mymachine.panel.fixed.FixedInterfaces;
import edu.kit.iti.formal.mymachine.json.JSONMachineExporter;
import edu.kit.iti.formal.mymachine.panel.MachineElement;
import edu.kit.iti.formal.mymachine.serialise.MachineSerialiser;
import edu.kit.iti.formal.mymachine.serialise.XStreamSerialiser;
import edu.kit.iti.formal.mymachine.util.BooleanObservable;
import org.json.JSONObject;

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

    public static final MachineSerialiser SERIALISER = new XStreamSerialiser();

    /**
     * A link to the frame of this application.
     * Not to be serialised
     */
    private transient MainFrame mainFrame;

    /**
     * A collection of all machine (=panel) elements. They are indexed by their name.
     */
    private Map<String, MachineElement> machineElements = new LinkedHashMap<>();

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
     * Is this machine meant to be executed on the Hardware component?
     */
    private BooleanObservable fixedInterface = new BooleanObservable();

    /**
     * The collection of transitions. not indexed.
     */
    private List<Transition> transitions = new ArrayList<>();

    /**
     * The flag for the play mode. Observers can be added to the flag.
     */
    private transient final BooleanObservable playModeObservable = new BooleanObservable();

    private List<String> displayStrings = new ArrayList<>();

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
     * Has this machine the fixed hardware interface?
     *
     * @return true iff this corresponds to the hardwired interface
     */
    public boolean isFixedInterface() {
        return fixedInterface.get();
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
     * Add an observer to the fixedMode
     *
     * @param observer a listener which is called whenever the value changes.
     */
    public void addFixedInterfaceObserver(Consumer<BooleanObservable> observer) {
        fixedInterface.addObserver(observer);
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
                        output.react(this, trans.getMessageIndex());
                    }
                    output = trans.getOutput2();
                    if (output != null) {
                        output.react(this, trans.getMessageIndex2());
                    }
                    // Command has been processed. Do not look further.
                    mainFrame.repaint();
                    break;
                }
            }
        }
    }

    public void addMachineElement(MachineElement element) {
        machineElements.put(element.toString(), element);
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
        loadScenario(new FileInputStream(file));
    }

    /**
     * Load the business data from an xml file.
     *
     * There are corrupt xml files which may make this crash horribly.
     * Please only use xml files saved with the very same version of this
     * application.
     *
     * @param istream the stream to load from
     * @throws IOException if reading fails
     * @throws ClassNotFoundException if a class is missing.
     */
    public void loadScenario(InputStream istream) throws IOException, ClassNotFoundException {
        Machine newMachine = SERIALISER.deserialise(istream);
        this.activeState = null;
        this.states = newMachine.states;
        this.machineElements = newMachine.machineElements;
        this.transitions = newMachine.transitions;
        this.displayStrings = newMachine.displayStrings;
        this.fixedInterface.set(newMachine.isFixedInterface());
        this.setPlayMode(false);
        mainFrame.repaint();
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
        SERIALISER.serialise(this, new FileOutputStream(file));
    }

    /**
     * Export the machine to a json file which can be interpreted by the
     * hardware machine.
     *
     * The data itself is not modified.
     *
     * @param file the file to save to
     * @throws IOException if file writing fails.
     */
    public void exportScenario(File file) throws IOException {

        JSONMachineExporter exporter = new JSONMachineExporter(this, mainFrame);
        JSONObject json = exporter.export();

        if(json != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(json.toString(2));
            }
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
        this.fixedInterface.set(false);
        this.setPlayMode(false);
    }

    /**
     * Make this machine comply to the fixed interface mode.
     */
    public void resetFixedInterface() throws IOException, ClassNotFoundException {
        this.reset();
        // Add the default elements from the reference file.
        FixedInterfaces.addFixedInterfaceElements(this);
        this.fixedInterface.set(true);
        this.setPlayMode(false);
        this.mainFrame.repaint();
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

    public void renameState(State state, String newName) {
        states.remove(state.getName());
        state.setName(newName);
        states.put(newName, state);
    }

    public boolean removeState(State state) {
        for (Transition transition : transitions) {
            if (transition.getFrom() == state || transition.getTo() == state) {
                return false;
            }
        }
        states.remove(state.getName());
        return true;
    }

    public void repaint() {
        mainFrame.repaint();
    }

}
