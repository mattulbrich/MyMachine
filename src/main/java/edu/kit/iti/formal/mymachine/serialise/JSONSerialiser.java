package edu.kit.iti.formal.mymachine.serialise;

import edu.kit.iti.formal.mymachine.Machine;
import edu.kit.iti.formal.mymachine.automata.State;
import edu.kit.iti.formal.mymachine.automata.Transition;
import edu.kit.iti.formal.mymachine.panel.MachineElement;
import edu.kit.iti.formal.mymachine.panel.fixed.FixedInterfaces;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * A more robust alternative serialisation format.
 */
public class JSONSerialiser implements MachineSerialiser {

    public static int FORMAT_VERSION = 1;

    //
    // DESERIALISE -------------
    //

    @Override
    public Machine deserialise(InputStream is) throws IOException {
        try {
            JSONObject json = new JSONObject(new JSONTokener(is));

            if(json.getInt("format") != FORMAT_VERSION) {
                throw new IllegalStateException("Expecting format version: " + FORMAT_VERSION);
            }

            Machine result = new Machine();

            if(json.getBoolean("fixed_interface")) {
                FixedInterfaces.addFixedInterfaceElements(result);
                result.resetFixedInterface();
            } else {
                readElements(result, json);
            }

            readStates(result, json);
            readTransitions(result, json);
            return result;

        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    private void readTransitions(Machine m, JSONObject json) {
        for (Object entry : json.getJSONArray("transitions")) {
            JSONObject jtrans = (JSONObject) entry;
            State from = m.getState(jtrans.getString("from"));
            State to = m.getState(jtrans.getString("to"));
            MachineElement trigger = m.getMachineElement(jtrans.getString("trigger"));
            MachineElement out1 = null;
            int mess1 = 0;
            if (jtrans.has("out1")) {
                out1 = m.getMachineElement(jtrans.getString("out1"));
                mess1 = jtrans.getInt("message1");
            }
            MachineElement out2 = null;
            int mess2 = 0;
            if (jtrans.has("out2")) {
                out2 = m.getMachineElement(jtrans.getString("out2"));
                mess2 = jtrans.getInt("message2");
            }
            Transition trans = new Transition(from, to, trigger, out1, mess1, out2, mess2);
            m.addTransition(trans);
        }
    }

    private void readStates(Machine m, JSONObject json) {
        for (Object entry : json.getJSONArray("states")) {
            JSONObject jstate = (JSONObject) entry;
            State state = new State(jstate.getString("name"),
                    readPosition(jstate.getJSONObject("pos")));
            m.addState(state);
        }
    }

    private void readElements(Machine m, JSONObject json) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (Object entry : json.getJSONArray("machine_elements")) {
            JSONObject jelem = (JSONObject) entry;
            MachineElement elem = (MachineElement) Class.forName(jelem.getString("class")).newInstance();
            elem.setPosition(readPosition(jelem.getJSONObject("pos")));
            elem.setName(jelem.getString("name"));
            m.addMachineElement(elem);
        }
    }

    private Point readPosition(JSONObject json) {
        Point result = new Point();
        result.x = json.getInt("x");
        result.y = json.getInt("y");
        return result;
    }

    //
    // SERIALISE -------------
    //

    @Override
    public void serialise(Machine m, OutputStream os) throws IOException {
        JSONObject result = new JSONObject();
        result.put("format", FORMAT_VERSION);
        result.put("fixed_interface", m.isFixedInterface());
        if (!m.isFixedInterface()) {
            result.put("machine_elements", makeMachineElements(m));
        }
        result.put("states", makeStates(m));
        result.put("transitions", makeTransisitons(m));

        os.write(result.toString(2).getBytes());
    }

    private JSONArray makeTransisitons(Machine m) {
        JSONArray result = new JSONArray();
        for (Transition transition : m.getTransitions()) {
            JSONObject j = new JSONObject().
                    put("from", transition.getFrom().getName()).
                    put("to", transition.getTo().getName()).
                    put("trigger", transition.getTrigger().toString());
            if (transition.getOutput() != null) {
                j.put("out1", transition.getOutput().getName()).
                        put("message1", transition.getMessageIndex());
            }
            if (transition.getOutput2() != null) {
                j.put("out2", transition.getOutput2().getName()).
                        put("message2", transition.getMessageIndex2());
            }
            result.put(j);
        }
        return result;
    }

    private JSONArray makeStates(Machine m) {
        JSONArray result = new JSONArray();
        for (State state : m.getStates()) {
            JSONObject j = new JSONObject().
                    put("pos", makePosition(state.getPosition())).
                    put("name", state.getName());
            result.put(j);
        }
        return result;
    }

    private JSONArray makeMachineElements(Machine m) {
        JSONArray result = new JSONArray();
        for (MachineElement element : m.getMachineElements()) {
            JSONObject j = new JSONObject().
                    put("class", element.getClass().getName()).
                    put("pos", makePosition(element.getPosition())).
                    put("name", element.getName());
            result.put(j);
        }
        return result;
    }

    private JSONObject makePosition(Point p) {
        JSONObject result = new JSONObject();
        result.put("x", p.x);
        result.put("y", p.y);
        return result;
    }
}
