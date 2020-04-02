package edu.kit.iti.formal.mymachine;

import java.awt.*;

public abstract class MachineElement {

    public enum PaintMode { NEUTRAL, PRESSED, MOUSE_OVER;};

    private Point position;
    private Dimension dimension;

    public MachineElement(Dimension dimension) {
        this.dimension = dimension;
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

    public abstract String getCommand();

    public void output(String out) {
        // by default do nothing
    }
}
