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
import java.util.ArrayList;
import java.util.List;

public class Display extends MachineElement {

    private static final Font FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);
    private static final String NAME = "#Display";
    private static final Dimension DIMENSION = new Dimension(400, 40);
    private static final Color DARKISH_GRAY = new Color(90, 89, 89);

    private String text = "";
    private Machine machine;

    public Display() {
        super(NAME, DIMENSION);
    }

    @Override
    public void uiConfig(Machine machine) {
        this.machine = machine;
    }

    @Override
    public void paint(Graphics2D g, PaintMode neutral) {
        Point pos = getPosition();
        g.setColor(DARKISH_GRAY);
        g.fillRect(pos.x - DIMENSION.width/2, pos.y - DIMENSION.height/2,
                DIMENSION.width, DIMENSION.height);

        g.setColor(Color.BLACK);
        g.fillRect(pos.x - DIMENSION.width/2, pos.y + DIMENSION.height/2,
                DIMENSION.width, 2);
        g.fillRect(pos.x + DIMENSION.width/2, pos.y - DIMENSION.height/2,
                2, DIMENSION.height + 2);

        g.setColor(Color.lightGray);
        g.fillRect(pos.x - DIMENSION.width/2, pos.y - DIMENSION.height/2,
                DIMENSION.width, 2);
        g.fillRect(pos.x - DIMENSION.width/2, pos.y - DIMENSION.height/2,
                2, DIMENSION.height + 2);

        g.setColor(Color.white);
        g.setFont(FONT);
        g = (Graphics2D) g.create();
        g.setClip(pos.x - DIMENSION.width/2, pos.y - DIMENSION.height/2,
                DIMENSION.width, DIMENSION.height);
        g.drawString(text, pos.x - DIMENSION.width/2 + 10, pos.y + g.getFontMetrics().getAscent()/2);
    }

    @Override
    public String[] getActions() {
        List<String> list = new ArrayList<>();
        list.add(Util.r("panel.clear_display"));
        list.addAll(machine.getDisplayStrings());
        return list.toArray(new String[list.size()]);
    }

    @Override
    public void react(int messageIndex) {
        List<String> displayStrings = machine.getDisplayStrings();
        if(messageIndex <= 0 || messageIndex > displayStrings.size()) {
            // 0 means clear display
            text = "";
        } else {
            text = displayStrings.get(messageIndex - 1);
        }
    }

    @Override
    public String toString() {
        return Util.r("panel.display");
    }
}
