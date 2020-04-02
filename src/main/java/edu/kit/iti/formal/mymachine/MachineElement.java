package edu.kit.iti.formal.mymachine;

import java.awt.*;

public abstract class MachineElement {

    public enum PaintMode { NEUTRAL, PRESSED, MOUSE_OVER;};


    private Point position;

    private final Dimension dimension;

    private final boolean active;
    private String name;
    public MachineElement(Dimension dimension) {
        this.dimension = dimension;
        this.active = false;
    }

    public MachineElement(String name, Dimension dimension, boolean active) {
        this.name = name;
        this.dimension = dimension;
        this.active = active;
    }

    public MachineElement(Dimension dimension, boolean active) {
        this.dimension = dimension;
        this.active = active;
    }

    public MachineElement(String name, Dimension dimension) {
        this.name = name;
        this.dimension = dimension;
        this.active = false;
    }

    public boolean contains(Point point) {
        int w2 = dimension.width / 2;
        int h2 = dimension.height / 2;
        return position.x - w2 <= point.x && point.x <= position.x + w2 &&
                position.y - h2 <= point.y && point.y <= position.y + h2;
    }

    public void setPosition(Point point) {
        position = point;
    }

    public Point getPosition() {
        return position;
    }

    public void drag(Point from, Point to) {
        position.x += to.x - from.x;
        position.y += to.y - from.y;
    }

    public abstract void uiConfig();

    public abstract void paint(Graphics2D g, PaintMode mode);

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void output(String out) {
        // by default do nothing
    }
}
