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

package edu.kit.iti.formal.mymachine.automata;

import edu.kit.iti.formal.mymachine.panel.MachineElement;

import javax.swing.*;
import java.awt.*;

public class Transition {
    private final State from;
    private final State to;
    private final MachineElement trigger;
    private final MachineElement output;
    private final int messageIndex;

    public Transition(State from, State to, MachineElement trigger, MachineElement output, int messageIndex) {
        this.from = from;
        this.to = to;
        this.trigger = trigger;
        this.output = output;
        this.messageIndex = messageIndex;
    }

    public void paint(Graphics2D g2) {
        Point toPos = to.getPosition();
        Point fromPos = from.getPosition();
        int x2 = (toPos.x + fromPos.x) / 2;
        int y2 = (toPos.y + fromPos.y) / 2;

        g2.setColor(Color.black);
        g2.drawLine(fromPos.x, fromPos.y, toPos.x, toPos.y);
        g2.drawString(trigger.toString(), x2, y2);
        g2.drawString(output.toString(), x2, y2+20);

        // Draw arrowhead
        double angle = Math.atan2(toPos.y-fromPos.y, toPos.x-fromPos.x);
        Graphics2D gc = (Graphics2D) g2.create();
        gc.rotate(angle, toPos.x, toPos.y);
        gc.drawLine(toPos.x - State.STATE_SIZE_HALF, toPos.y,
                toPos.x- State.STATE_SIZE_HALF - 10, toPos.y - 10);
        gc.drawLine(toPos.x - State.STATE_SIZE_HALF, toPos.y,
                toPos.x- State.STATE_SIZE_HALF - 10, toPos.y + 10);
    }

    public State getFrom() {
        return from;
    }

    public State getTo() {
        return to;
    }

    public MachineElement getTrigger() {
        return trigger;
    }

    public MachineElement getOutput() {
        return output;
    }

    public int getMessageIndex() {
        return messageIndex;
    }

    public boolean isFromTo(State fromState, State toState) {
        return this.from == fromState && this.to == toState;
    }
}
