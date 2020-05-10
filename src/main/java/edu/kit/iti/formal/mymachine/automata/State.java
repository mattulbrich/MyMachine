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

import javax.swing.*;
import java.awt.*;

/**
 * A state in the state in the finite state machine.
 *
 * This class servers the purpose of visualisation and of abstraction during
 * execution of the FSM.
 */
public class State {
    public static final int STATE_SIZE_HALF = 40;
    public static final int STATE_SIZE = 2 * STATE_SIZE_HALF;

    /**
     * Every state must have a unique name.
     */
    private String name;

    private Point position;

    public String getName() {
        return name;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public State(String name, Point point) {
        this.name = name;
        position = new Point(point.x - STATE_SIZE/2, point.y-STATE_SIZE/2);
    }

    public void paint(Graphics2D g2, boolean active) {
        g2.setColor(Color.white);
        g2.fillOval(position.x - STATE_SIZE_HALF, position.y - STATE_SIZE_HALF,
                STATE_SIZE, STATE_SIZE);
        g2.setColor(active ? Color.green : Color.black);
        g2.drawOval(position.x - STATE_SIZE_HALF, position.y - STATE_SIZE_HALF,
                STATE_SIZE, STATE_SIZE);
        FontMetrics fontMetrics = g2.getFontMetrics();
        int strWidth = SwingUtilities.computeStringWidth(fontMetrics, name);
        g2.drawString(name, position.x - strWidth/2, position.y + fontMetrics.getAscent()/2);

        if(name.equals("Start")) {
            int x = position.x - STATE_SIZE_HALF;
            int y = position.y;
            g2.drawLine(x-40, y, x, y);
            g2.drawLine(x, y, x - 10, y - 10);
            g2.drawLine(x, y, x - 10, y + 10);
        }
    }

    public boolean contains(Point point) {
        int deltax = point.x - position.x;
        int deltay = point.y - position.y;

        return Math.hypot(deltax, deltay) <= STATE_SIZE_HALF;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "State{"+ name + '}';
    }
}
