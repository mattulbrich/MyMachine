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

public class Output extends MachineElement {

    private static final String NAME = "#Output";
    private static final String[] ACTIONS =
            Util.r("automata.actions").split(", *");
    private static final Icon SLOT = Util.imageResource("output.png");
    private static final Icon BLOCK = Util.imageResource("block.png");
    private static final Icon COIN = Util.imageResource("returnCoin.png");

    private transient int output = 0;

    public Output() {
        super(NAME, Util.getDimension(SLOT));
    }

    @Override
    public void uiConfig(Machine machine) {
    }

    @Override
    public void paint(Graphics2D g, PaintMode mode) {

        if(mode == PaintMode.PRESSED) {
            output = 0;
        }

        Util.drawCentered(g, getPosition(), SLOT);
        Point pos = getPosition();
        // TODO Make this nice!
        if(output <= 4) {
            for (int i = 0; i < output; i++) {
                BLOCK.paintIcon(null, g, pos.x - 130 + i*50, pos.y - 15);
            }
        } else {
            for (int i = 0; i < output - 4; i++) {
                COIN.paintIcon(null, g, pos.x - 90 + i*50, pos.y + 15);
            }
        }
    }

    @Override
    public String[] getActions() {
        return ACTIONS;
    }

    @Override
    public void react(int messageIndex) {
        output = messageIndex + 1;
    }

    @Override
    public String toString() {
        return Util.r("panel.output");
    }
}
