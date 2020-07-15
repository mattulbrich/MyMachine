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

import java.awt.*;

public class Output extends MachineElement {

    private static final String NAME = "#Output";
    private static final String[] ACTIONS =
            Util.r("automata.actions").split(", *");

    private int schokis = 0;

    public Output() {
        super(NAME, new Dimension(200, 50));
    }

    @Override
    public void uiConfig(Machine machine) {
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
