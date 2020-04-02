package edu.kit.iti.formal.mymachine.automata;

import edu.kit.iti.formal.mymachine.FrontEndPanel;
import edu.kit.iti.formal.mymachine.MachineElement;
import edu.kit.iti.formal.mymachine.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AutomataEditor extends JPanel {

    ButtonGroup group = new ButtonGroup();
    private MainFrame frame;
    private AutomataPane panel;

    public AutomataEditor(MainFrame frame) {
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
            JToggleButton b = new JToggleButton("Bewegen");
            b.setActionCommand("move");
            b.setSelected(true);
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
        return group.getSelection().getActionCommand().equals("move");
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

    public List<State> getStates() {
        return frame.getStates();
    }

    public State getActiveState() {
        return frame.getActiveState();
    }

    public String getMode() {
        return group.getSelection().getActionCommand();
    }

    public List<Transition> getTransitions() {
        return frame.getTransitions();
    }
}
