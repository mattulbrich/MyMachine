package edu.kit.iti.formal.mymachine.automata;

import javax.swing.*;
import java.awt.*;

public class State {
    public static final int STATE_SIZE_HALF = 40;
    public static final int STATE_SIZE = 2 * STATE_SIZE_HALF;
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
        g2.setStroke(new BasicStroke(3f));
        g2.drawOval(position.x - STATE_SIZE_HALF, position.y - STATE_SIZE_HALF,
                STATE_SIZE, STATE_SIZE);
        FontMetrics fontMetrics = g2.getFontMetrics();
        int strWidth = SwingUtilities.computeStringWidth(fontMetrics, name);
        g2.drawString(name, position.x - strWidth/2, position.y + fontMetrics.getAscent()/2);
    }

    public boolean contains(Point point) {
        int deltax = point.x - position.x;
        int deltay = point.y - position.y;

        return -STATE_SIZE_HALF <= deltax && deltax < STATE_SIZE_HALF &&
                -STATE_SIZE_HALF <= deltay && deltay < STATE_SIZE_HALF;
    }
}
