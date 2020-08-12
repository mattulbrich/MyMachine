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

import edu.kit.iti.formal.mymachine.Machine;
import edu.kit.iti.formal.mymachine.Util;

import javax.swing.*;
import java.awt.*;
import java.util.NoSuchElementException;

public class LED extends MachineElement {

    private static final String[] ACTIONS = Util.r("panel.led_actions").split(",");
    static {
        assert ACTIONS.length == 3;
    }

    private transient boolean on = false;

    private static final Icon ON_ICON = Util.imageResource("ledOn.png");
    private static final Icon OFF_ICON = Util.imageResource("ledOff.png");

    public LED() {
        super(Util.getDimension(ON_ICON));
    }

    @Override
    public void uiConfig(Machine machine) {
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
            Util.drawCentered(g, pos, ON_ICON);
        } else {
            Util.drawCentered(g, pos, OFF_ICON);
        }
    }

    @Override
    public String[] getActions() {
        return ACTIONS;
    }

    @Override
    public void react(int messageIndex) {
        switch(messageIndex) {
            case 0 : on = true; break;
            case 1 : on = false; break;
            case 2 : on = !on; break;
            default:
                throw new IllegalArgumentException("" + messageIndex);
        }
    }

    @Override
    public String toString() {
        return Util.r("panel.led") + " " + getName();
    }
}
