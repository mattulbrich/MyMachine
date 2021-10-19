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
import java.util.Collections;
import java.util.Enumeration;

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
        /*{
        	
        	Icon icon = Util.imageResource("mouse");
        	JToggleButton mouseButton = new JToggleButton(icon);
        	mouseButton.setActionCommand("move");
        	selectionPanel.add(mouseButton);
        	buttonGroup.add(mouseButton);
        }*/

        machine.addPlaymodeObserver(x -> checkButtonEnabling());
        machine.addFixedInterfaceObserver(x -> checkButtonEnabling());

        add(selectionPanel, BorderLayout.NORTH);
        
        this.frontEndPanel = new FrontEndPanel(this);
        
        add(new JScrollPane(frontEndPanel));

        repaint();
    }

    private void checkButtonEnabling() {
        boolean playmode = machine.isPlayMode();
        boolean enable = !playmode && !machine.isFixedInterface();
        for (Component component : Collections.list(buttonGroup.getElements())) {
        	/*
        	 * Alle Buttons werden im Spiel-Modus und bei einem festen Interface deaktiviert.
        	 */
            component.setEnabled(enable);
        }
        if (playmode) {
            buttonGroup.clearSelection();
            // reset all output labels to neutral
            for (MachineElement element : getMachineElements()) {
                element.react(getMachine(),-1);
            }
        }
    }

    public boolean isDeleteMode() {
        ButtonModel selection = buttonGroup.getSelection();
        return selection != null && selection.getActionCommand().equals("delete");
    }

    public boolean isMoveMode() {
    	ButtonModel selection = buttonGroup.getSelection();
        return buttonGroup.getSelection() == null; // || selection.getActionCommand().equals("move");
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
    /**
     * 
     * @return gibt eine Collection aller aktiven Elemente der Maschine zurück
     */
    public Collection<MachineElement> getMachineElements() {
        return machine.getMachineElements();
    }

    public void finishAction() {
        buttonGroup.clearSelection();
    }

    public void addMachineElement(MachineElement element) {
        machine.addMachineElement(element);
        
        /*
         * Nach dem Hinzufügen eines Elements soll geprüft werden, ob ein Button deaktiviert werden soll.
         */
        
    	for (Component component : Collections.list( buttonGroup.getElements() ) ) {
    		
    		if ( ((JToggleButton) component).getText().equals(Util.r("panel.display")) && element.getName().equals("#Display")) {
    			component.setEnabled(false);
    		}
    		
    		else if ( ((JToggleButton) component).getText().equals(Util.r("panel.output")) && element.getName().equals("#Output" ) ) {
    	        	component.setEnabled(false);
    	        }
    		
    		if (((JToggleButton) component).getText().equals(Util.r("panel.coinslot") )  && element.getName().equals("#Coin" ) ) {
    			component.setEnabled(false);
            }
    		
    	}
    	
    	/*
    	 * 
    	 */
        
    }

    public MachineElement getMachineElement(String name) {
        return machine.getMachineElement(name);
    }
    
    public void changeDesign() {
    	frontEndPanel.changeDesign();
    	repaint();
    }
    
    public void activateTooltipToButtons() {
    	Enumeration<AbstractButton> e = buttonGroup.getElements();
    	while(e.hasMoreElements()) {
    		AbstractButton b = e.nextElement();
    		if (b.getText().equals(Util.r("panel.delete"))) {
    			b.setToolTipText(Util.r("tooltip.delete_button"));
    		} else {
    			b.setToolTipText(Util.r("tooltip.element_buttons"));
    		}
    	}
    	
    	frontEndPanel.activateToolTips();
    }
    
    public void reset() {
    	Enumeration<AbstractButton> e = buttonGroup.getElements();
    	while(e.hasMoreElements()) {
    		AbstractButton b = e.nextElement();
    		b.setEnabled(true);
    	}
    }
    
    public boolean isPlayMode() {
    	return machine.isPlayMode();
    }
	
}
