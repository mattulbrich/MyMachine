/**
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
package edu.kit.iti.formal.mymachine.automata;

import edu.kit.iti.formal.mymachine.Machine;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AutomataEditor extends JPanel {

    ButtonGroup group = new ButtonGroup();
    private Machine frame;
    private AutomataPane panel;

    public AutomataEditor(Machine frame) {
        super(new BorderLayout());
        this.frame = frame;
        init();
    }

    private void init() {
        JToolBar selectionPanel = new JToolBar();

        {
            JToggleButton b = new JToggleButton(" L A U F ");
            selectionPanel.add(b);
            b.addActionListener(e -> frame.setPlayMode(b.isSelected()));
        }
        selectionPanel.add(new JSeparator());
        {
            JToggleButton b = new JToggleButton("Neuer Zustand");
            b.setActionCommand("addstate");
            selectionPanel.add(b);
            group.add(b);
        }
        {
            JToggleButton b = new JToggleButton("Neuer Übergang");
            b.setActionCommand("addtrans");
            b.addActionListener(e-> {
                this.panel.removeTransitionInfo();
            });
            selectionPanel.add(b);
            group.add(b);
        }
        {
            JToggleButton b = new JToggleButton("Bearbeiten");
            b.setActionCommand("edit");
            selectionPanel.add(b);
            group.add(b);
        }
        {
            JToggleButton b = new JToggleButton("Löschen");
            b.setActionCommand("delete");
            selectionPanel.add(b);
            group.add(b);
        }

        add(selectionPanel, BorderLayout.NORTH);

        this.panel = new AutomataPane(this);

        add(new JScrollPane(panel));

        repaint();
    }

    public boolean isDeleteMode() {
        return group.getSelection().getActionCommand().equals("delete");
    }

    public boolean isMoveMode() {
        return group.getSelection() == null;
    }

    public List<State> getStates() {
        return frame.getStates();
    }

    public State getActiveState() {
        return frame.getActiveState();
    }

    public String getMode() {
        ButtonModel selButton = group.getSelection();
        if (selButton == null) {
            return "move";
        }
        return selButton.getActionCommand();
    }

    public List<Transition> getTransitions() {
        return frame.getTransitions();
    }
}
