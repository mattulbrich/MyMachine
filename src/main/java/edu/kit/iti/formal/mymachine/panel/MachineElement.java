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
import edu.kit.iti.formal.mymachine.automata.Transition;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public abstract class MachineElement implements Serializable {

    protected static final Font LABEL_FONT = new Font(Font.DIALOG, Font.PLAIN, 12);

    protected static final String[] NO_ACTIONS = {};

    public enum PaintMode { NEUTRAL, PRESSED, MOUSE_OVER}

    /**
     * Current position on screen.
     *
     * Note: This is the center point of this element!
     */
    private Point position;

    /**
     * The size of this element on the screen
     * (for mouse responsibility detection)
     */
    private final Dimension dimension;

    /**
     * Is this an active element.
     * Active elements fire an event when clicked on.
     */
    private final boolean active;

    /**
     * Every element has a unique name.
     */
    private String name;

    /**
     * Instantiate a new non-active machine element with null name.
     *
     * @param dimension size on screen
     */
    public MachineElement(Dimension dimension) {
        this.dimension = dimension;
        this.active = false;
    }

    /**
     * Instantiate a new named possibly active machine element
     * @param name name of the element
     * @param dimension size on screen
     * @param active true iff it is active
     */
    public MachineElement(String name, Dimension dimension, boolean active) {
        this.name = name;
        this.dimension = dimension;
        this.active = active;
    }

    /**
     * Instantiate a new unnamed possibly active machine element
     * @param dimension size on screen
     * @param active true iff it is active
     */
    public MachineElement(Dimension dimension, boolean active) {
        this.dimension = dimension;
        this.active = active;
    }

    /**
     * Instantiate a new named possibly non-active machine element
     * @param name name of the element
     * @param dimension size on screen
     */
    public MachineElement(String name, Dimension dimension) {
        this.name = name;
        this.dimension = dimension;
        this.active = false;
    }

    /**
     * Is the argument within the bounds of this element?
     *
     * @param point coordinates to check
     * @return true iff the point is within the rectangle occupied on
     * the screen by this element.
     */
    public boolean contains(Point point) {
        int w2 = dimension.width / 2;
        int h2 = dimension.height / 2;
        return position.x - w2 <= point.x && point.x <= position.x + w2 &&
                position.y - h2 <= point.y && point.y <= position.y + h2;
    }

    public void setPosition(Point point) {
        position = point;
    }

    public Point getPosition() {
        return position;
    }

    /**
     * Change the position of this element according to the difference of the two points.
     * The position is updated according to the difference between the two points.
     *
     * @param from the point that gives the start
     * @param to the point that gives the end
     */
    public void drag(Point from, Point to) {
        position.x += to.x - from.x;
        position.y += to.y - from.y;
    }

    /**
     * Every element is given the possibility to react to its creation.
     *
     * Here the name may be queried from the user or an exception be raised if a
     * name already exists etc.
     *
     * @throws java.util.NoSuchElementException if the methods decides the element
     * should not exist
     * @param machine
     */
    public abstract void uiConfig(Machine machine);

    /**
     * Paint this element at its position. Currently,
     * it is always painted in its neutral position
     *
     * @param g use this graphics to draw
     * @param mode the mode to paint it in
     */
    public abstract void paint(Graphics2D g, PaintMode mode);

    /**
     * Paint a label on a machine in the from end.
     * It paints its name (if not internal) beneath the image.
     *
     * @param g graphcs to draw with
     */
    public void paintLabel(Graphics2D g) {
        String name = getName();
        if (!name.startsWith("#")) {
            g.setColor(Color.black);
            g.setFont(LABEL_FONT);
            int sw = SwingUtilities.computeStringWidth(g.getFontMetrics(), name);
            g.drawString(name, getPosition().x - sw / 2,
                    getPosition().y + dimension.height / 2 + g.getFontMetrics().getHeight());
        }
    }

    /**
     * Get the string to be printed in transition labels for a given message index.
     *
     * By default, returns the name of the element, but this should be overridden by
     * machine elements that take parameters.
     *
     * @param messageIndex the index of the applied message
     * @return a non-null string
     */
    public String getOutputLabel(int messageIndex) {
        String[] actions = getActions();
        if(actions.length == 0) {
            return getName();
        } else {
            return actions[messageIndex];
        }
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getActions() {
        return NO_ACTIONS;
    }

    /**
     * React to an output action from a transition in the automaton.
     *
     * @param messageIndex an index into the set of possible actions
     */
    public void react(int messageIndex) {
        // by default do nothing
    }

    /**
     * Elements can only be deleted if they are not referenced from the automaton.
     *
     * @param machine the machine to look up elements
     *
     * @return true iff this element can be deleted
     */
    public boolean canBeDeleted(Machine machine) {
        for (Transition trans : machine.getTransitions()) {
            if (trans.getTrigger() == this ||
                    trans.getOutput() == this ||
                    trans.getOutput2() == this) {
                return false;
            }
        }
        return true;
    }

    public abstract String toString();

    public Dimension getDimension() {
        return dimension;
    }
}
