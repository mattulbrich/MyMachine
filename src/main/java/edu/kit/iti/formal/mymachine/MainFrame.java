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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

    private static final boolean DEBUG = true;

    private DesignPane designPane;
    private Machine machine;
    private AutomataEditor automataEditor;

    public MainFrame(Machine machine) {
        super(Util.r("title"));
        this.machine = machine;
        init();
    }

    private void init() {
        designPane = new DesignPane(machine);
        automataEditor = new AutomataEditor(machine);
        showAsTabs();

        JMenuBar menuBar = new JMenuBar();
        JMenu control = new JMenu(Util.r("menu.control"));
        menuBar.add(control);

        JMenuItem load = new JMenuItem(Util.r("menu.control.load"));
        load.addActionListener(this::loadScenario);
        control.add(load);

        JMenuItem save = new JMenuItem(Util.r("menu.control.save"));
        save.addActionListener(this::saveScenario);
        control.add(save);

        JMenuItem reset = new JMenuItem(Util.r("menu.control.reset"));
        reset.addActionListener(e -> { machine.reset(); repaint(); });
        control.add(reset);

        control.add(new JSeparator());

        JMenuItem connect = new JMenuItem(Util.r("menu.control.connect"));
        connect.setEnabled(false);
        control.add(connect);

        control.add(new JSeparator());

        JMenuItem exit = new JMenuItem(Util.r("menu.control.exit"));
        exit.addActionListener(x -> System.exit(0));
        control.add(exit);

        JMenu setup = new JMenu(Util.r("menu.setup"));
        menuBar.add(setup);

        JMenuItem strings = new JMenuItem(Util.r("menu.setup.display_strings"));
        strings.addActionListener(e -> editStrings());
        setup.add(strings);

        JMenu view = new JMenu(Util.r("menu.view"));
        menuBar.add(view);

        ButtonGroup bg = new ButtonGroup();
        JRadioButtonMenuItem tabs = new JRadioButtonMenuItem(Util.r("menu.view.asTabs"));
        bg.add(tabs);
        tabs.setSelected(true);
        tabs.addActionListener(e -> showAsTabs());
        view.add(tabs);

        JRadioButtonMenuItem split = new JRadioButtonMenuItem(Util.r("menu.view.split"));
        bg.add(split);
        split.addActionListener(e -> showAsSplitPane());
        view.add(split);

        if(DEBUG) {
            JMenu debug = new JMenu("Debug");
            menuBar.add(debug);

            JMenuItem item = new JMenuItem("Dump machine");
            item.addActionListener(e -> machine.dump());
            debug.add(item);
        }

        setJMenuBar(menuBar);
    }

    private void editStrings() {
        String[] columName = { Util.r("setup.display_string") };
        TableModel tm = new DefaultTableModel(columName, 10);
        int r = 0;
        for (String str : machine.getDisplayStrings()) {
            tm.setValueAt(str, r++, 0);
        }

        JTable table = new JTable(tm);
        table.setFont(table.getFont().deriveFont(14.f));

        int response = JOptionPane.showConfirmDialog(this,
                new JScrollPane(table), Util.r("setup.display_strings"),
                JOptionPane.OK_CANCEL_OPTION);


        if (response == JOptionPane.OK_OPTION) {
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < tm.getRowCount(); i++) {
                Object value = tm.getValueAt(i, 0);
                if (value == null) {
                    value = "";
                }
                strings.add(value.toString());
            }
            machine.setDisplayStrings(strings);
        }

    }

    private void showAsSplitPane() {
        JSplitPane pane = new JSplitPane();
        pane.setLeftComponent(designPane);
        pane.setRightComponent(automataEditor);
        setContentPane(pane);
    }

    private void showAsTabs() {
        setContentPane(new JTabbedPane());
        getContentPane().add(designPane, Util.r("body"));
        getContentPane().add(automataEditor, Util.r("automaton"));
    }

    private void loadScenario(ActionEvent actionEvent) {
        JFileChooser jfc = new JFileChooser(".");
        if (jfc.showDialog(this, Util.r("file.load")) == JFileChooser.APPROVE_OPTION) {
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
        if (jfc.showDialog(this, Util.r("file.save")) == JFileChooser.APPROVE_OPTION) {
            try {
                machine.saveScenario(jfc.getSelectedFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
