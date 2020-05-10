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
    private final int messageIndex2;
    private final MachineElement output2;

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

}
