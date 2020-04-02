package edu.kit.iti.formal.mymachine.automata;

import javax.swing.*;
import java.awt.*;

public class State {
    public static final int STATE_SIZE = 100;
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
        g2.fillOval(position.x, position.y, STATE_SIZE, STATE_SIZE);
        g2.setColor(active ? Color.green : Color.black);
        g2.setStroke(new BasicStroke(3f));
        g2.drawOval(position.x, position.y, STATE_SIZE, STATE_SIZE);
        int strWidth = SwingUtilities.computeStringWidth(g2.getFontMetrics(), name);
        g2.drawString(name, position.x + (STATE_SIZE- strWidth)/2, position.y + STATE_SIZE/2);
    }

    public boolean contains(Point point) {
        return position.x <= point.x && point.x < position.x + STATE_SIZE &&
                position.y <= point.y && point.y < position.y + STATE_SIZE;
    }
}
