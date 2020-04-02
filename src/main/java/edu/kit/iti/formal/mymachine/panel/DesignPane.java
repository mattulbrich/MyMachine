package edu.kit.iti.formal.mymachine.panel;

import edu.kit.iti.formal.mymachine.Machine;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class DesignPane extends JPanel {

    ButtonGroup group = new ButtonGroup();
    private Machine frame;
    private FrontEndPanel frontEndPanel;

    DesignPane(Machine frame) {
        super(new BorderLayout());
        this.frame = frame;
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

        frame.getPlayModeObservable().addObserver((s,o) -> {
                    boolean playMode = frame.isPlayMode();
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
        String clssName = "edu.kit.iti.formal.mymachine." + actCommand.substring(4);
        try {
            return (MachineElement) Class.forName(clssName).getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Machine getFrame() {
        return frame;
    }

    public Collection<MachineElement> getMachineElements() {
        return frame.getMachineElements();
    }

    public void finishAction() {
        group.clearSelection();
    }

    public void addMachineElement(MachineElement element) {
        frame.addMachineElement(element);
    }

    public MachineElement getMachineElement(String name) {
        return frame.getMachineElement(name);
    }
}
