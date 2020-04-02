package edu.kit.iti.formal.mymachine;

import java.awt.*;

public class Output extends MachineElement {

    public Output() {
        super(new Dimension(200, 50));
    }

    public int schokis = 0;

    @Override
    public void uiConfig() {
    }

    @Override
    public void paint(Graphics2D g, PaintMode neutral) {

        Point pos = getPosition();
        g.setColor(Color.black);
        g.fillRect(pos.x - 100, pos.y - 25, 200, 50);
        g.setColor(Color.white);
        g.setStroke(new BasicStroke(5f));
        g.drawRect(pos.x - 100, pos.y - 25, 200, 50);

        g.setColor(Color.red);
        for (int i = 0; i < schokis; i++) {
            g.fillRoundRect(pos.x - 80 + i*30, pos.y-10,
                    25, 20, 5, 5);
        }

    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public void output(String out) {
        try {
            schokis = Integer.parseInt(out);
        } catch (NumberFormatException ex) {
            schokis = 0;
        }
    }
}
