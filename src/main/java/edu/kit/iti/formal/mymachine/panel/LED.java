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
package edu.kit.iti.formal.mymachine.panel;

import edu.kit.iti.formal.mymachine.Util;

import javax.swing.*;
import java.awt.*;
import java.util.NoSuchElementException;

public class LED extends MachineElement {

    private boolean on = false;

    private static final Icon ON_ICON = Util.imageResource("ledOn.png");
    private static final Icon OFF_ICON = Util.imageResource("ledOff.png");

    public LED() {
        super(Util.getDimension(ON_ICON));
    }

    @Override
    public void uiConfig() {
        String res = JOptionPane.showInputDialog("Wie soll die LED benannt werden (1 Wort)");
        if (res != null) {
            setName(res);
        } else {
            throw new NoSuchElementException("No name wanted --> abort");
        }
    }

    @Override
    public void paint(Graphics2D g, PaintMode neutral) {
        Point pos = getPosition();

        if(on) {
            ON_ICON.paintIcon(null, g, pos.x - ON_ICON.getIconWidth() / 2, pos.y - ON_ICON.getIconHeight() / 2);
        } else {
            OFF_ICON.paintIcon(null, g, pos.x - ON_ICON.getIconWidth() / 2, pos.y - ON_ICON.getIconHeight() / 2);
        }
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
