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

public class Button extends MachineElement {

    private static final Icon BUTTON_PRESSED =
            Util.imageResource("buttonPressed.png");

    private static final Icon BUTTON_RELEASED =
            Util.imageResource("buttonReleased.png");

    public Button() {
        super(Util.getDimension(BUTTON_PRESSED), true);
    }

    @Override
    public void uiConfig() {
        String res = JOptionPane.showInputDialog("Wie soll der Knopf benannt sein (1 Wort)");
        if (res != null) {
            setName(res);
        } else {
            throw new IllegalArgumentException("No name wanted --> abort");
        }
    }

    @Override
    public void paint(Graphics2D g, PaintMode mode) {
        if (mode == PaintMode.PRESSED) {
            Util.drawCentered(g, getPosition(), BUTTON_PRESSED);
        } else {
            Util.drawCentered(g, getPosition(), BUTTON_RELEASED);
        }
    }

}