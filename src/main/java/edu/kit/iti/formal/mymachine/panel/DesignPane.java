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
package edu.kit.iti.formal.mymachine.panel;

import edu.kit.iti.formal.mymachine.Machine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Collection;

public class DesignPane extends JPanel {

    ButtonGroup group = new ButtonGroup();
    private Machine machine;
    private FrontEndPanel frontEndPanel;

    public DesignPane(Machine machine) {
        super(new BorderLayout());
        this.machine = machine;
        init();
    }

    private void init() {
        JToolBar selectionPanel = new JToolBar();
        {
            JToggleButton b = new JToggleButton("Knopf");
            b.setActionCommand("add Button");
            selectionPanel.add(b);
            group.add(b);
        }
        {
            JToggleButton b = new JToggleButton("Münzschlitz");
            b.setActionCommand("add Slot");
            selectionPanel.add(b);
            group.add(b);
        }
        {
            JToggleButton b = new JToggleButton("Bild");
            b.setEnabled(false);
            selectionPanel.add(b);
            group.add(b);
        }
        {
            JToggleButton b = new JToggleButton("Ausgabeschacht");
            b.setActionCommand("add Output");
            selectionPanel.add(b);
            group.add(b);
        }
        {
            JToggleButton b = new JToggleButton("LED");
            b.setActionCommand("add LED");
            selectionPanel.add(b);
            group.add(b);
        }
        {
            JToggleButton b = new JToggleButton("Display");
            b.setActionCommand("add Display");
            selectionPanel.add(b);
            group.add(b);
        }
        {
            JToggleButton b = new JToggleButton("Löschen");
            b.setActionCommand("delete");
            selectionPanel.add(b);
            group.add(b);
        }

        machine.getPlayModeObservable().addObserver((s, o) -> {
                    boolean playMode = machine.isPlayMode();
                    for (Component component : selectionPanel.getComponents()) {
                        component.setEnabled(!playMode);
                    }
                });

        add(selectionPanel, BorderLayout.NORTH);
        
        this.frontEndPanel = new FrontEndPanel(this);
        
        add(new JScrollPane(frontEndPanel));

        repaint();
    }

    public boolean isDeleteMode() {
        ButtonModel selection = group.getSelection();
        return selection != null && selection.getActionCommand().equals("delete");
    }

    public boolean isMoveMode() {
        return group.getSelection() == null;
    }

    public boolean isAddMode() {
        ButtonModel selection = group.getSelection();
        if (selection == null) {
            return false;
        }
        String actCommand = selection.getActionCommand();
        return actCommand.startsWith("add ");
    }

    public MachineElement createElement() {
        String actCommand = group.getSelection().getActionCommand();
        assert actCommand.startsWith("add ");
        String clssName = "edu.kit.iti.formal.mymachine.panel." + actCommand.substring(4);
        try {
            return (MachineElement) Class.forName(clssName).getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Machine getMachine() {
        return machine;
    }

    public Collection<MachineElement> getMachineElements() {
        return machine.getMachineElements();
    }

    public void finishAction() {
        group.clearSelection();
    }

    public void addMachineElement(MachineElement element) {
        machine.addMachineElement(element);
    }

    public MachineElement getMachineElement(String name) {
        return machine.getMachineElement(name);
    }
}
