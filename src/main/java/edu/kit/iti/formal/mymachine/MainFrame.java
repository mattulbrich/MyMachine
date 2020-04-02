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

import edu.kit.iti.formal.mymachine.automata.AutomataEditor;
import edu.kit.iti.formal.mymachine.automata.State;
import edu.kit.iti.formal.mymachine.automata.Transition;
import edu.kit.iti.formal.mymachine.panel.DesignPane;
import edu.kit.iti.formal.mymachine.panel.MachineElement;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
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

        JMenuBar menuBar = new JMenuBar();
        JMenu control = new JMenu("Steuerung");
        menuBar.add(control);

        JMenuItem load = new JMenuItem("Szenario laden ...");
        load.addActionListener(this::loadScenario);
        control.add(load);

        JMenuItem save = new JMenuItem("Szenario speichern ...");
        save.addActionListener(this::saveScenario);
        control.add(save);

        JMenuItem reset = new JMenuItem("Zurücksetzen");
        reset.addActionListener(e -> machine.reset());
        control.add(reset);

        control.add(new JSeparator());

        JMenuItem connect = new JMenuItem("Mit Anlage Verbinden ...");
        connect.setEnabled(false);
        control.add(connect);

        control.add(new JSeparator());

        JMenuItem exit = new JMenuItem("Beenden");
        exit.addActionListener(x -> System.exit(0));
        control.add(exit);

        setJMenuBar(menuBar);
    }

    private void loadScenario(ActionEvent actionEvent) {
        JFileChooser jfc = new JFileChooser(".");
        if (jfc.showDialog(this, "Laden") == JFileChooser.APPROVE_OPTION) {
            try {
                machine.loadScenario(jfc.getSelectedFile());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveScenario(ActionEvent actionEvent) {
        JFileChooser jfc = new JFileChooser(".");
        if (jfc.showDialog(this, "Speichern") == JFileChooser.APPROVE_OPTION) {
            try {
                machine.saveScenario(jfc.getSelectedFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
