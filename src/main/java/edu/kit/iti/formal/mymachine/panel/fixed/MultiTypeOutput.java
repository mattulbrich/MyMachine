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
package edu.kit.iti.formal.mymachine.panel.fixed;

import edu.kit.iti.formal.mymachine.Machine;
import edu.kit.iti.formal.mymachine.panel.MachineElement;
import edu.kit.iti.formal.mymachine.util.Util;

import javax.swing.*;
import java.awt.*;

public class MultiTypeOutput extends MachineElement {

    private static final String NAME = "#MultiOutput";
    private static final String[] ACTIONS =
            Util.r("multi.output.actions").split(", *");
    private static final Icon SLOT = Util.imageResource("output.png");
    private static final Icon BLOCK1 = Util.imageResource("block.png");
    private static final Icon BLOCK2 = Util.imageResource("block2.png");
    private static final Icon BLOCK3 = Util.imageResource("block3.png");
    private static final Icon COIN1 = Util.imageResource("returnCoin.png");
    private static final Icon COIN2 = Util.imageResource("returnCoin2.png");

    private transient int output = 0;

    public MultiTypeOutput() {
        super(NAME, Util.getDimension(SLOT));
    }

    @Override
    public void uiConfig(Machine machine) {
    }

    @Override
    public void paint(Graphics2D g, PaintMode mode) {

        Util.drawCentered(g, getPosition(), SLOT);
        Point pos = getPosition();
        switch(output) {
            case 0:
                BLOCK1.paintIcon(null, g, pos.x - 130, pos.y - 15);
                break;
            case 1:
                BLOCK2.paintIcon(null, g, pos.x - 130, pos.y - 15);
                break;
            case 2:
                BLOCK3.paintIcon(null, g, pos.x - 130, pos.y - 15);
                break;
            case 3:
                COIN1.paintIcon(null, g, pos.x - 90, pos.y + 15);
                break;
            case 4:
                COIN2.paintIcon(null, g, pos.x - 90, pos.y + 15);
                break;
        }
    }

    @Override
    public String[] getActions() {
        return ACTIONS;
    }

    @Override
    public void react(int messageIndex) {
        output = messageIndex;
    }

    @Override
    public String toString() {
        return Util.r("panel.output");
    }
}
