package edu.kit.iti.formal.mymachine;

import javax.swing.*;
import java.awt.*;

public class LED extends MachineElement {

    private boolean on = false;

    public LED() {
        super(new Dimension(20, 20));
    }

    @Override
    public void uiConfig() {
        String res = JOptionPane.showInputDialog("Wie soll die LED benannt werden (1 Wort)");
        if (res != null) {
            setName(res);
        } else {
            throw new IllegalArgumentException("No name wanted --> abort");
        }
    }

    @Override
    public void paint(Graphics2D g, PaintMode neutral) {

        Point pos = getPosition();

        g.setColor(on ? Color.red : Color.black);
        g.fillOval(pos.x - 10, pos.y - 10, 20, 20);

        g.setColor(Color.black);
        g.drawString(getName(), pos.x, pos.y + 25);
    }

    @Override
    public void output(String out) {
        switch(out) {
            case "an" : on = true; break;
            case "aus" : on = false; break;
            case "wechseln" : on = !on; break;
            // TODO default: Error!
        }
    }
}
