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

import edu.kit.iti.formal.mymachine.util.Util;
import edu.kit.iti.formal.mymachine.panel.MachineElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ResourceBundle;
import java.util.Vector;

public class AutomataPane extends JComponent implements MouseMotionListener, MouseListener {

    private static final Stroke DASHED_STROKE = new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);;
    private static final Stroke SOLID_STROKE = new BasicStroke(3f);
    private AutomataEditor automataEditor;
    private State firstTransPartner;
    private State draggedState;
    private Point dragStart;
    private ResourceBundle r = Util.RESOURCE_BUNDLE;

    public AutomataPane(AutomataEditor automataEditor) {
        this.automataEditor = automataEditor;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON));

        g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        g2.setColor(Color.white);
        g2.fillRect(0,0,getWidth(),getHeight());
        g2.setStroke(SOLID_STROKE);

        State active = automataEditor.getActiveState();

        TransitionPainter.INSTANCE.paintTransitions(g2, automataEditor.getTransitions());

        if(firstTransPartner != null) {
            Graphics2D g3 = (Graphics2D) g2.create();
            g3.setStroke(DASHED_STROKE);
            g3.setColor(Color.CYAN);
            Point pos = firstTransPartner.getPosition();
            g3.drawLine(pos.x, pos.y, dragStart.x, dragStart.y);
        }

        for (State state : automataEditor.getStates()) {
            state.paint(g2, state == active);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggedState != null) {
            Point pos = draggedState.getPosition();
            pos.x += e.getX() - dragStart.x;
            pos.y += e.getY() - dragStart.y;
            dragStart = e.getPoint();
            repaint();
        } else if (firstTransPartner != null) {
            dragStart = e.getPoint();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch(automataEditor.getMode()) {
            case "addstate":
                String name = JOptionPane.showInputDialog(r.getString("state.name"));
                if (name != null) {
                    if (automataEditor.getMachine().getState(name) != null) {
                        JOptionPane.showMessageDialog(this,
                                r.getString("state.name_taken"));
                    } else {
                        automataEditor.getMachine().addState(new State(name, e.getPoint()));
                    }
                }
                repaint();
                break;

            case "move":
                if (e.getClickCount() == 2) {
                    State state = findState(e.getPoint());
                    if (state != null) {
                        String newName = JOptionPane.showInputDialog(r.getString("state.rename"), state.getName());
                        if(newName != null && newName.trim().isEmpty()) {
                            boolean succ = automataEditor.getMachine().removeState(state);
                            if (!succ) {
                                JOptionPane.showMessageDialog(this,
                                        r.getString("state.still_in_use"));
                            }
                        } else if (newName != null && !newName.equals(state.getName())) {
                            if (automataEditor.getMachine().getState(newName) != null) {
                                JOptionPane.showMessageDialog(this, r.getString("state.name_taken"));
                            } else {
                                automataEditor.getMachine().renameState(state, newName);
                            }
                        }
                        repaint();
                        break;
                    }

                    Transition trans = findTransition(e.getPoint());
                    if (trans != null) {
                        TransitionEditor editor =
                                new TransitionEditor(trans.getFrom(), trans.getTo(),
                                        automataEditor.getMachine(), false);

                        editor.pack();
                        editor.setLocationRelativeTo(this);
                        editor.setVisible(true);
                        repaint();
                    }
                }
                break;
        }
    }

    private Transition findTransition(Point point) {
        for (Transition trans : automataEditor.getTransitions()) {
            if (trans.isNear(point)) {
                return trans;
            }
        }
        return null;
    }

    private State findState(Point point) {
        for (State state : automataEditor.getStates()) {
            if (state.contains(point)) {
                return state;
            }
        }
        return null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        State state = findState(e.getPoint());
        if(automataEditor.getMode().equals("addtrans")) {
            firstTransPartner = state;
        } else {
            draggedState = state;
        }
        dragStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        draggedState = null;
        if (firstTransPartner != null) {
            State partner = findState(e.getPoint());

            if (partner != null) {

                if(automataEditor.getMachine().getMachineElements().
                        stream().allMatch(x -> !x.isActive())) {
                    JOptionPane.showMessageDialog(this, Util.r("transedit.no_actions_error"),
                           Util.r("error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                TransitionEditor editor =
                        new TransitionEditor(firstTransPartner, partner,
                                automataEditor.getMachine(), true);

                editor.pack();
                editor.setLocationRelativeTo(this);
                editor.setVisible(true);
                repaint();
            }

            firstTransPartner = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void removeTransitionInfo() {
        firstTransPartner = null;
    }
}
