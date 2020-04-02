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

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class AutomataPane extends JComponent implements MouseMotionListener, MouseListener {
    private AutomataEditor automataEditor;
    private State firstTransPartner;
    private State draggedState;
    private Point dragStart;

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

        g.setColor(Color.white);
        g.fillRect(0,0,getWidth(),getHeight());

        State active = automataEditor.getActiveState();

        for (Transition transition : automataEditor.getTransitions()) {
            transition.paint(g2);
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
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch(automataEditor.getMode()) {
            case "addstate":
                String name = JOptionPane.showInputDialog("Name");
                if (name != null) {
                    automataEditor.getStates().add(new State(name, e.getPoint()));
                }
                repaint();
                break;

            case "addtrans":
                State state = findState(e.getPoint());
                if(state == null) {
                    return;
                }
                if(firstTransPartner == null) {
                    firstTransPartner = state;
                    JOptionPane.showMessageDialog(null, "Select 2nd transition partner");
                } else {
                    String in = JOptionPane.showInputDialog("Transition input");
                    String out = JOptionPane.showInputDialog("Transition output");
                    automataEditor.getTransitions().add(new Transition(firstTransPartner, state, in, out));
                    firstTransPartner = null;
                }
        }

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
        draggedState = findState(e.getPoint());
        dragStart = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        draggedState = null;
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
