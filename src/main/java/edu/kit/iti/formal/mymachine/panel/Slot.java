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
import edu.kit.iti.formal.mymachine.util.Util;

import javax.swing.*;
import java.awt.*;

public class Slot extends MachineElement {

    private static final Icon SLOT_NO_COIN = Util.imageResource("coinSlotEmpty.png");
    private static final Icon SLOT_COIN = Util.imageResource("coinSlotCoin.png");

    public Slot() {
        super("#Coin", Util.getDimension(SLOT_COIN), true);
    }

    @Override
    public void uiConfig(Machine machine) {
    }

    @Override
    public void paint(Graphics2D g, PaintMode mode) {

        if (mode == PaintMode.PRESSED) {
            Util.drawCentered(g, getPosition(), SLOT_COIN);
        } else {
            Util.drawCentered(g, getPosition(), SLOT_NO_COIN);
        }
    }

    @Override
    public String toString() {
        return Util.r("panel.slot");
    }

}
