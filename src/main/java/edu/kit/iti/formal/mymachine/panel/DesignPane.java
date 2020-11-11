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

package edu.kit.iti.formal.mymachine.panel;

import edu.kit.iti.formal.mymachine.util.DeselectButtonGroup;
import edu.kit.iti.formal.mymachine.Machine;
import edu.kit.iti.formal.mymachine.util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * That is the button panel for the machine front end.
 * Plus all the actions associated within it.
 */
public class DesignPane extends JPanel {

    ButtonGroup buttonGroup = new DeselectButtonGroup();
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
            JToggleButton b = new JToggleButton(Util.r("panel.button"));
            b.setActionCommand("add Button");
            selectionPanel.add(b);
            buttonGroup.add(b);
        }
        {
            JToggleButton b = new JToggleButton(Util.r("panel.coinslot"));
            b.setActionCommand("add Slot");
            selectionPanel.add(b);
            buttonGroup.add(b);
        }
        /*{
            JToggleButton b = new JToggleButton(Util.r("panel.image"));
            b.setEnabled(false);
            selectionPanel.add(b);
            buttonGroup.add(b);
        }*/
        {
            JToggleButton b = new JToggleButton(Util.r("panel.output"));
            b.setActionCommand("add Output");
            selectionPanel.add(b);
            buttonGroup.add(b);
        }
        {
            JToggleButton b = new JToggleButton("LED");
            b.setActionCommand("add LED");
            b.addActionListener(e -> {
                System.out.println(b);
            });
            selectionPanel.add(b);
            buttonGroup.add(b);
        }
        {
            JToggleButton b = new JToggleButton(Util.r("panel.display"));
            b.setActionCommand("add Display");
            selectionPanel.add(b);
            buttonGroup.add(b);
        }
        {
            JToggleButton b = new JToggleButton(Util.r("panel.delete"));
            b.setActionCommand("delete");
            selectionPanel.add(b);
            buttonGroup.add(b);
        }

        machine.addPlaymodeObserver(playmodeObs -> {
            boolean playmode = playmodeObs.get();
            boolean enable = !playmode && !machine.isFixedInterface();
            for (Component component : selectionPanel.getComponents()) {
                component.setEnabled(enable);
            }
            if (playmode) {
                buttonGroup.clearSelection();
                // reset all output labels to neutral
                for (MachineElement element : getMachineElements()) {
                    element.react(-1);
                }
            }
        });

        add(selectionPanel, BorderLayout.NORTH);
        
        this.frontEndPanel = new FrontEndPanel(this);
        
        add(new JScrollPane(frontEndPanel));

        repaint();
    }

    public boolean isDeleteMode() {
        ButtonModel selection = buttonGroup.getSelection();
        return selection != null && selection.getActionCommand().equals("delete");
    }

    public boolean isMoveMode() {
        return buttonGroup.getSelection() == null;
    }

    public boolean isAddMode() {
        ButtonModel selection = buttonGroup.getSelection();
        if (selection == null) {
            return false;
        }
        String actCommand = selection.getActionCommand();
        return actCommand.startsWith("add ");
    }

    public MachineElement createElement() {
        String actCommand = buttonGroup.getSelection().getActionCommand();
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
        buttonGroup.clearSelection();
    }

    public void addMachineElement(MachineElement element) {
        machine.addMachineElement(element);
    }

    public MachineElement getMachineElement(String name) {
        return machine.getMachineElement(name);
    }
}
