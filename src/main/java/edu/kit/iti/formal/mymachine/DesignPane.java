package edu.kit.iti.formal.mymachine;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class DesignPane extends JPanel {

    ButtonGroup group = new ButtonGroup();
    private MainFrame frame;
    private FrontEndPanel frontEndPanel;

    DesignPane(MainFrame frame) {
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
        return group.getSelection().getActionCommand().equals("delete");
    }

    public boolean isMoveMode() {
        return group.getSelection() == null;
    }

    public boolean isAddMode() {
        String actCommand = group.getSelection().getActionCommand();
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

    public MainFrame getFrame() {
        return frame;
    }

    public List<MachineElement> getMachineElements() {
        return frame.getMachineElements();
    }

    public void unselectButton() {
        group.clearSelection();
    }
}
