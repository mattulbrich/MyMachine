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
import edu.kit.iti.formal.mymachine.util.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class Transition {
    private final State from;
    private final State to;
    private final MachineElement trigger;
    private final MachineElement output;
    private final int messageIndex;
    private final MachineElement output2;
    private final int messageIndex2;

    public Transition(State from, State to, MachineElement trigger,
                      MachineElement element, int messageNumber,
                      MachineElement element2, int messageNumber2) {
        this.from = from;
        this.to = to;
        this.trigger = trigger;
        this.output = element;
        this.messageIndex = messageNumber;
        this.output2 = element2;
        this.messageIndex2 = messageNumber2;
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

    public int getMessageIndex2() {
        return messageIndex2;
    }

    public MachineElement getOutput2() {
        return output2;
    }

    public int getMessageIndex() {
        return messageIndex;
    }

    public boolean isFromTo(State fromState, State toState) {
        return this.from == fromState && this.to == toState;
    }

    public boolean isNear(Point point) {

        if(from == to) {
            Point p = new Point(from.getPosition());
            p.translate(State.STATE_SIZE_HALF, State.STATE_SIZE_HALF);
            double distance = Math.abs(point.distance(p) - State.STATE_SIZE_HALF);
            // System.out.println("loop: " + from + "->" + to + " " + distance);
            return distance < 5.0;
        }

        Point fromPos = new Point(from.getPosition());
        Point toPos = new Point(to.getPosition());
        TransitionPainter.translatePoints(fromPos, toPos);

        // Projection onto line
        Vector dir = Vector.minus(toPos, fromPos);
        Vector dirNorm = dir.normalize();
        Vector pvec = Vector.minus(point, fromPos);
        double dotProd = dirNorm.dotProd(pvec);
        double distance;
        if (dotProd < 0.0) {
            distance = fromPos.distance(point);
        } else if(dotProd > dir.length()) {
            distance = toPos.distance(point);
        } else {
            Vector normal = dirNorm.normal();
            double prod = pvec.dotProd(normal);
            distance = Math.abs(prod);
        }

        // System.out.println("D: " + from + "->" + to + " " + distance);
        return distance < 5;
    }
}
