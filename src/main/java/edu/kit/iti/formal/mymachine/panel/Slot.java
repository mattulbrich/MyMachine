package edu.kit.iti.formal.mymachine.panel;

import java.awt.*;

public class Slot extends MachineElement {

    public Slot() {
        super("#Coinslot", new Dimension(20, 100), true);
    }

    @Override
    public void uiConfig() {
    }

    @Override
    public void paint(Graphics2D g, PaintMode mode) {
        Point pos = getPosition();
        g.setColor(Color.black);
        g.fillRect(pos.x - 10, pos.y - 50, 20, 100);
        g.setColor(Color.darkGray);
        g.setStroke(new BasicStroke(5f));
        g.drawRect(pos.x - 10, pos.y - 50, 20, 100);

        if(mode == PaintMode.MOUSE_OVER) {
            g.setColor(Color.yellow);
            g.fillOval(pos.x-50, pos.y-25, 50, 50);
        }
    }

}
