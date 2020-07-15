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

public class Output extends MachineElement {

    private static final String NAME = "#Output";
    private static final String[] ACTIONS =
            Util.r("automata.actions").split(", *");
    private static final Icon SLOT = Util.imageResource("output.png");;

    private int schokis = 0;

    public Output() {
        super(NAME, new Dimension(200, 50));
    }

    @Override
    public void uiConfig(Machine machine) {
    }

    @Override
    public void paint(Graphics2D g, PaintMode mode) {

        Util.drawCentered(g, getPosition(), SLOT);

        // TODO Make this nice!
        Point pos = getPosition();
        g.setColor(Color.red);
        for (int i = 0; i < schokis; i++) {
            g.fillRoundRect(pos.x - 80 + i * 30, pos.y - 10,
                    25, 20, 5, 5);
        }
    }

    @Override
    public String[] getActions() {
        return ACTIONS;
    }

    @Override
    public void react(int messageIndex) {
        schokis = messageIndex;
    }

    @Override
    public String toString() {
        return Util.r("panel.output");
    }
}
