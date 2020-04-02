package edu.kit.iti.formal.mymachine;

import javax.swing.*;
import java.awt.*;

public class Button extends MachineElement {

    private String name;

    public Button() {
        super(new Dimension(100, 100));
    }

    @Override
    public void uiConfig() {
        String res = JOptionPane.showInputDialog("Wie soll der Knopf benannt sein (1 Wort)");
        if (res != null) {
            name = res;
        } else {
            throw new IllegalArgumentException("No name wanted --> abort");
        }

    }

    @Override
    public void paint(Graphics2D g, PaintMode mode) {
        Point pos = getPosition();
        g.setColor(Color.red);
        g.fillOval(pos.x - 50, pos.y - 50, 100, 100);
        g.setColor(Color.black);
        g.drawString(name, pos.x, pos.y + 70);

        // if(mode) ...

    }

    @Override
    public String getCommand() {
        return name;
    }

}
