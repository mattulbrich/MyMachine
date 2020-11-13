package edu.kit.iti.formal.mymachine.automata;

import edu.kit.iti.formal.mymachine.Machine;
import edu.kit.iti.formal.mymachine.util.Util;
import edu.kit.iti.formal.mymachine.panel.*;
import edu.kit.iti.formal.mymachine.panel.Button;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Vector;

public class TransitionEditor extends JDialog {
    private static final Font LABEL_FONT = new Font(Font.DIALOG, Font.PLAIN, 14);
    private JLabel fromLabel;
    private JLabel toLabel;
    private JTabbedPane transitions;

    private final State fromState;
    private final State toState;
    private final Machine machine;
    private boolean newTrans;

    public TransitionEditor(State fromState, State toState, Machine machine, boolean newTrans) {
        this.fromState = fromState;
        this.toState = toState;
        this.machine = machine;
        this.newTrans = newTrans;
        init();
    }

    private void init() {
        setModal(true);
        getContentPane().setLayout(new BorderLayout());
        initClose();
        initButtons();
        initMainPanel();
        initTransitions();
    }

    private void initTransitions() {
        transitions.removeAll();
        int number = 1;
        for (Transition transition : machine.getTransitions()) {
            if(transition.isFromTo(fromState, toState)) {
                transitions.add(makeTransitionPanel(transition),
                        Util.r("transedit.transition") + " " + number);
                number ++;
            }
        }
        if(newTrans) {
            JPanel panel = makeTransitionPanel(null);
            transitions.add(panel, Util.r("transedit.new_trans"));
            transitions.setSelectedComponent(panel);
        }
    }

    private JPanel makeTransitionPanel(Transition transition) {
        JPanel result = new JPanel();
        result.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        result.add(new JLabel(Util.r("transedit.input")), gbc);
        gbc.gridy++;
        result.add(new JLabel(Util.r("transedit.output")), gbc);
        gbc.gridy++;
        result.add(new JLabel(Util.r("transedit.output2")), gbc);
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        JComboBox<MachineElement> inputs = new JComboBox<>(mkInputs());
        Vector<Event> events = mkOutputs();
        JComboBox<Event> outputs = new JComboBox<>(events);
        JComboBox<Event> outputs2 = new JComboBox<>(events);
        result.add(inputs, gbc);
        gbc.gridy++;
        result.add(outputs, gbc);
        gbc.gridy++;
        result.add(outputs2, gbc);
        if (transition != null) {
            gbc.gridy++;
            gbc.fill = GridBagConstraints.NONE;
            JButton delete = new JButton(Util.r("transedit.trans_delete"));
            delete.addActionListener(e -> deleteTrans(transition));
            result.add(delete, gbc);
            inputs.setSelectedItem(transition.getTrigger());
            outputs.setSelectedItem(new Event(transition.getOutput(), transition.getMessageIndex(), ""));
            outputs2.setSelectedItem(new Event(transition.getOutput2(), transition.getMessageIndex2(), ""));
            result.putClientProperty("transition", transition);
        }
        result.putClientProperty("inputs", inputs);
        result.putClientProperty("outputs", outputs);
        result.putClientProperty("outputs2", outputs2);
        return result;
    }

    private void deleteTrans(Transition transition) {
        machine.removeTransition(transition);
        initTransitions();
    }

    private static class Event implements Comparable<Event> {
        final MachineElement element;
        final int messageNumber;
        final String string;

        private Event(MachineElement element, int messageNumber, String string) {
            this.element = element;
            this.messageNumber = messageNumber;
            this.string = string;
        }

        @Override
        public String toString() {
            return string;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Event event = (Event) o;
            return messageNumber == event.messageNumber &&
                    Objects.equals(element, event.element);
        }

        @Override
        public int hashCode() {
            return Objects.hash(element, messageNumber);
        }

        @Override
        public int compareTo(Event o) {
            return string.compareTo(o.string);
        }
    }

    private Vector<MachineElement> mkInputs() {
        Vector<MachineElement> result = new Vector<>();
        for (MachineElement element : machine.getMachineElements()) {
            if(element.isActive()) {
                result.add(element);
            }
        }
        return result;
    }

    private Vector<Event> mkOutputs() {
        Vector<Event> result = new Vector<>();
        result.add(new Event(null, 0, Util.r("transedit.no_action")));
        for (MachineElement element : machine.getMachineElements()) {
            if(!element.isActive()) {
                String[] actions = element.getActions();
                if(actions.length > 0) {
                    for (int i = 0; i < actions.length; i++) {
                        String action = actions[i];
                        result.add(new Event(element, i, element + " " + action));
                    }
                } else {
                    result.add(new Event(element, 0, element.toString()));
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    private void initClose() {
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        ((JComponent)getContentPane()).registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initMainPanel() {

        JPanel mainPanel = new JPanel(new GridBagLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        this.fromLabel = new JLabel(fromState.getName());
        fromLabel.setFont(LABEL_FONT);
        fromLabel.setBorder(makeBorder(Util.r("transedit.from")));
        mainPanel.add(fromLabel, gbc);

        gbc.gridy++;
        this.toLabel = new JLabel(toState.getName());
        toLabel.setBorder(makeBorder(Util.r("transedit.to")));
        toLabel.setFont(LABEL_FONT);
        mainPanel.add(toLabel, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        this.transitions = new JTabbedPane();
        mainPanel.add(transitions, gbc);

    }

    private static CompoundBorder makeBorder(String title) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(5,5,5,5));
    }

    private void initButtons() {
        JPanel buttons = new JPanel();
        JButton buttonOK = new JButton(UIManager.getString("OptionPane.okButtonText"));
        buttonOK.addActionListener(this::onOK);
        buttons.add(buttonOK);
        getRootPane().setDefaultButton(buttonOK);

        JButton buttonCancel = new JButton(UIManager.getString("OptionPane.cancelButtonText"));
        buttonCancel.addActionListener(e->onCancel());
        buttons.add(buttonCancel);

        getContentPane().add(buttons, BorderLayout.SOUTH);
    }

    private void onOK(ActionEvent e) {

        for(int i = 0; i < transitions.getComponentCount(); i++) {
            JComponent comp = (JComponent) transitions.getComponent(i);

            JComboBox<?> inputs = (JComboBox<?>) comp.getClientProperty("inputs");
            JComboBox<?> outputs = (JComboBox<?>) comp.getClientProperty("outputs");
            JComboBox<?> outputs2 = (JComboBox<?>) comp.getClientProperty("outputs2");

            MachineElement input = (MachineElement) inputs.getSelectedItem();
            Event event = (Event) outputs.getSelectedItem();
            Event event2 = (Event) outputs2.getSelectedItem();

            Transition newTrans = new Transition(fromState, toState,
                    input, event.element, event.messageNumber,
                    event2.element, event2.messageNumber);

            Transition trans = (Transition) comp.getClientProperty("transition");
            if (trans != null) {
                machine.removeTransition(trans);
            }

            machine.addTransition(newTrans);
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Machine machine = new Machine();
        Point noPoint = new Point(100, 100);
        State s0 = new State("Start", noPoint);
        State s1 = new State("S1", noPoint);

        LED led = new LED();
        led.setName("Leer");
        machine.addMachineElement(led);

        machine.addMachineElement(new Slot());

        Display display = new Display();
        display.uiConfig(machine);
        machine.addMachineElement(display);

        Output output = new Output();
        machine.addMachineElement(output);

        Button button = new Button();
        button.setName("Ausgabe");
        machine.addMachineElement(button);

        machine.getDisplayStrings().add("Hallo Welt");
        machine.getDisplayStrings().add("langer, langer, langer Text!");

        machine.addState(s0);
        machine.addState(s1);
        machine.addTransition(new Transition(s0, s1, led, output, 2, null, 1));
        machine.addTransition(new Transition(s0, s1, button, display,0, output, 2));

        TransitionEditor dialog = new TransitionEditor(s0, s1, machine, true);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

}
