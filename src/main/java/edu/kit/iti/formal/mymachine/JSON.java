//package edu.kit.iti.formal.mymachine;
//
//import edu.kit.iti.formal.mymachine.automata.State;
//import edu.kit.iti.formal.mymachine.automata.Transition;
//import edu.kit.iti.formal.mymachine.panel.MachineElement;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//
//public class JSON {
//
//    public static interface JSONable {
//        JSONObject toJSON();
//        void fromJSON(JSONObject json);
//    }
//
//    private final Collection<State> states;
//    private final Collection<Transition> transitions;
//    private final Collection<MachineElement> elements;
//    private final Collection<String> displayStrings;
//
//    public JSON(Machine machine) {
//        this.states = machine.getStates();
//        this.transitions = machine.getTransitions();
//        this.elements = machine.getMachineElements();
//        this.displayStrings = machine.getDisplayStrings();
//    }
//
//    public void save(File file) {
//        JSONObject save = new JSONObject();
//        save.put("date", new Date().toString());
//        save.put("version", "1");
//        save.put("states", toJSON(states));
////        save.put("transitions", toJSON(transitions));
////        save.put("elements", toJSON(elements));
////        save.put("displayStrings", toJSON(displayStrings));
//    }
//
//    private JSONArray toJSON(Collection<? extends JSONable> list) {
//        ArrayList<JSONObject> result = new ArrayList<JSONObject>();
//        for (JSONable x : list) {
//            JSONObject e = x.toJSON();
//            e.put("__class", x.getClass().getName());
//            result.add(e);
//        }
//        return new JSONArray(result);
//    }
//
//}
