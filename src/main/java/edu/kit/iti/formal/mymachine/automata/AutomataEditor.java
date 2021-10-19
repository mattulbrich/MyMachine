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
package edu.kit.iti.formal.mymachine.automata;

import edu.kit.iti.formal.mymachine.Machine;
import edu.kit.iti.formal.mymachine.util.DeselectButtonGroup;
import edu.kit.iti.formal.mymachine.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

public class AutomataEditor extends JPanel {

    private ButtonGroup buttonGroup = new DeselectButtonGroup();
    private Machine machine;
    private AutomataPane panel;
    
    private JToggleButton automataRun;
    private JToggleButton newState;
    private JToggleButton newTransition;
    private JToggleButton mouseButton;

    public AutomataEditor(Machine machine) {
        super(new BorderLayout());
        this.machine = machine;
        init();
    }

    private void init() {
        JToolBar selectionPanel = new JToolBar();
        {
        	automataRun = new JToggleButton(Util.r("automata.run"));
            selectionPanel.add(automataRun);
            automataRun.addActionListener(this::run);
        }
        selectionPanel.add(new JSeparator());
        {
            newState = new JToggleButton(Util.r("automata.new_state"));
            newState.setActionCommand("addstate");
            newState.addActionListener(e -> {
            	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            });
            selectionPanel.add(newState);
            buttonGroup.add(newState);
        }
        {
            newTransition = new JToggleButton(Util.r("automata.new_transition"));
            newTransition.setActionCommand("addtrans");
            newTransition.addActionListener(e-> {
                this.panel.removeTransitionInfo();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            });
            selectionPanel.add(newTransition);
            buttonGroup.add(newTransition);
        }
        {
        	
        	Icon icon = Util.imageResource("mouse");
        	mouseButton = new JToggleButton(icon);
        	mouseButton.setActionCommand("edit");
        	mouseButton.addActionListener(e -> {
        		setCursor(new Cursor(Cursor.HAND_CURSOR));
        	});
        	selectionPanel.add(mouseButton);
        	buttonGroup.add(mouseButton);
        }
        
        /*{
            JToggleButton b = new JToggleButton(Util.r("automata.edit"));
            b.setActionCommand("edit");
            selectionPanel.add(b);
            buttonGroup.add(b);
        }*/
        /*{
            JToggleButton b = new JToggleButton(Util.r("automata.delete"));
            b.setActionCommand("delete");
            selectionPanel.add(b);
            buttonGroup.add(b);
        }*/

        add(selectionPanel, BorderLayout.NORTH);

        this.panel = new AutomataPane(this);

        add(new JScrollPane(panel));

        repaint();

        machine.addPlaymodeObserver(playmode -> {
            Enumeration<AbstractButton> en = buttonGroup.getElements();
            while (en.hasMoreElements()) {
                en.nextElement().setEnabled(!playmode.get());
            }
            if (playmode.get()) {
                buttonGroup.clearSelection();
            }
        });
    }

    private void run(ActionEvent e) {
        JToggleButton button = (JToggleButton) e.getSource();
        if(button.isSelected()) {
            if(machine.getStartState() == null) {
                JOptionPane.showMessageDialog(this,
                        Util.r("automata.no_start"));
                button.setSelected(false);
                return;
            }

            State indet = machine.findIndeterminism();
            if (indet != null) {
                JOptionPane.showMessageDialog(this,
                        MessageFormat.format(Util.r("automata.name_clash"), indet.getName()));
                button.setSelected(false);
                return;
            }
            machine.setPlayMode(true);
        } else {
            machine.setPlayMode(false);
        }
    }

    public boolean isMoveMode() {
        return buttonGroup.getSelection() == null;
    }

    public Collection<State> getStates() {
        return machine.getStates();
    }

    public State getActiveState() {
        return machine.getActiveState();
    }

    public String getMode() {
        ButtonModel selButton = buttonGroup.getSelection();
        if (selButton == null) {
            return "move";
        }
        return selButton.getActionCommand();
    }

    public List<Transition> getTransitions() {
        return machine.getTransitions();
    }

    public Machine getMachine() {
        return machine;
    }
    
    public void activateToolTips() {
    	automataRun.setToolTipText(Util.r("automata.tooltip.run"));
    	newState.setToolTipText(Util.r("automata.tooltip.newState"));;
    	newTransition.setToolTipText(Util.r("automata.tooltip.newTransition"));
    	mouseButton.setToolTipText(Util.r("automata.tooltip.mouseButton"));
    	
    	
    }
    
    public void clearSelection() {
    	buttonGroup.clearSelection();
    }
    
    
}
