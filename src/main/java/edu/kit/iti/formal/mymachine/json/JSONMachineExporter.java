package edu.kit.iti.formal.mymachine.json;

import edu.kit.iti.formal.mymachine.Machine;
import edu.kit.iti.formal.mymachine.automata.State;
import edu.kit.iti.formal.mymachine.automata.Transition;
import edu.kit.iti.formal.mymachine.panel.Button;
import edu.kit.iti.formal.mymachine.panel.Display;
import edu.kit.iti.formal.mymachine.panel.LED;
import edu.kit.iti.formal.mymachine.panel.MachineElement;
import edu.kit.iti.formal.mymachine.panel.Output;
import edu.kit.iti.formal.mymachine.panel.Slot;
import edu.kit.iti.formal.mymachine.panel.fixed.CoinTwoSlot;
import edu.kit.iti.formal.mymachine.panel.fixed.MultiTypeOutput;
import edu.kit.iti.formal.mymachine.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JSONMachineExporter {
    private final Machine machine;
    private final Component frame;
    private final List<State> orderedStates;

    public JSONMachineExporter(Machine machine, Component frame) {
        this.machine = machine;
        this.frame = frame;
        this.orderedStates = orderStates(machine.getStates());
    }

    private List<State> orderStates(Collection<State> states) {
        List<State> result = new ArrayList<>();
        for (State state : states) {
            if(state.getName().equals("Start")) {
                result.add(0, state);
            } else {
                result.add(state);
            }
        }
        return result;
    }

    public JSONObject export() {

        // check that it can be run at all. Do not export if something is wrong ...
        if(machine.getStartState() == null) {
            JOptionPane.showMessageDialog(frame, Util.r("automata.no_start"));
            return null;
        }

        if(!machine.isFixedInterface()) {
            JOptionPane.showMessageDialog(frame, Util.r("automata.not_fixed"));
            return null;
        }

        State indet = machine.findIndeterminism();
        if (indet != null) {
            JOptionPane.showMessageDialog(frame,
                    MessageFormat.format(Util.r("automata.name_clash"), indet.getName()));
            return null;
        }

        JSONObject result = new JSONObject();
        result.put("States", exportStates());
        result.put("Transitions", exportTransitions());
        return result;
    }

    private JSONArray exportStates() {
        JSONArray jsonStates = new JSONArray();
        // "Start" has been ensured to be there. Mention that first.
        for (State state : orderedStates) {
            String name = state.getName();
            jsonStates.put(name);
        }
        return jsonStates;
    }

    private JSONObject exportTransitions() {
        JSONObject jsonTrans = new JSONObject();
        Map<String, List<Transition>> aggTrans = aggregateTransitions();
        for (Entry<String, List<Transition>> en : aggTrans.entrySet()) {
            JSONObject transes = new JSONObject();
            for (Transition trans : en.getValue()) {
                exportTransition(trans, transes);
            }
            jsonTrans.put(en.getKey(), transes);
        }
        return jsonTrans;
    }

    private Map<String, List<Transition>> aggregateTransitions() {
        Map<String, List<Transition>> result = new HashMap<>();
        for (Transition transition : machine.getTransitions()) {
            String name = transition.getFrom().getName();
            List<Transition> list = result.computeIfAbsent(name, k -> new ArrayList<>());
            list.add(transition);
        }
        return result;
    }

    private void exportTransition(Transition transition, JSONObject json) {

        JSONArray actions = new JSONArray();
        addAction(transition.getOutput(), transition.getMessageIndex(), actions);
        addAction(transition.getOutput2(), transition.getMessageIndex2(), actions);

        JSONObject result = new JSONObject();
        result.put("Output", actions);
        result.put("NextState", orderedStates.indexOf(transition.getTo()));

        json.put(getTrigger(transition.getTrigger()), result);
    }

    private String getTrigger(MachineElement element) {
        if (element instanceof Button) {
            return "Knopf" + element.getName();
        }

        if (element instanceof Slot) {
            return "Münze1";
        }

        if (element instanceof CoinTwoSlot) {
            return "Münze2";
        }

        throw new IllegalStateException("Cannot export this machine element: " + element.getClass());
    }

    private void addAction(MachineElement element, int messageIndex, JSONArray actions) {
        if (element != null) {
            if (element instanceof LED) {
                String action = "LED" + element.getName();
                switch (messageIndex) {
                    case 0:
                        action += " aus";
                        break;
                    case 1:
                        action += " an";
                        break;
                    case 2:
                        action += " toggle";
                        break;
                    default:
                        throw new IllegalStateException("Unexpected LED message");
                }
                actions.put(action);
            } else if(element instanceof Output) {
                String action = "Item" + (messageIndex + 1);
                actions.put(action);
            } else if(element instanceof MultiTypeOutput) {
                if(messageIndex < 3) {
                    actions.put("Item" + (messageIndex + 1));
                } else {
                    actions.put("Münze" + (messageIndex - 2));
                }
            } else {
                throw new IllegalStateException("Cannot export this machine element: " + element.getClass());
            }
        }
    }
}
