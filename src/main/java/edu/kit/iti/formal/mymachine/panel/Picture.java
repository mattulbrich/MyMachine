package edu.kit.iti.formal.mymachine.panel;

import edu.kit.iti.formal.mymachine.Machine;
import edu.kit.iti.formal.mymachine.util.Util;

import java.awt.*;

public class Picture extends MachineElement {

    private transient final Image image;

    public Picture(String name, Image image) {
        super(name, getDim(image));
        this.image = image;
    }

    private static Dimension getDim(Image image) {
        return new Dimension(image.getWidth(null), image.getHeight(null));
    }

    @Override
    public void uiConfig(Machine machine) {
    }

    @Override
    public void paint(Graphics2D g, PaintMode neutral) {
        Point pos = getPosition();
        Dimension d = getDimension();
        g.drawImage(image, pos.x - d.width/2, pos.y - d.height/2, null);
    }

    @Override
    public String toString() {
        return Util.r("panel.picture") + " " + getName();
    }
}