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
import java.awt.event.ActionEvent;

public class Output extends MachineElement {

    private static final String NAME = "#Output";
    private static final String[] ACTIONS =
            Util.r("automata.actions").split(", *");
    private static final Icon SLOT = Util.imageResource("output");
    private static final Icon BLOCK = Util.imageResource("block");
    private static final Icon COIN = Util.imageResource("returnCoin");

    private transient int output = 0;
    private int animationOffset;

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

        Point pos = getPosition();
        Util.drawCentered(g, pos, SLOT);

        g = (Graphics2D) g.create();
        g.setClip(pos.x - SLOT.getIconWidth()/2, pos.y - SLOT.getIconHeight()/2+5,
                SLOT.getIconWidth(), SLOT.getIconHeight());

        // TODO Make this nice!
        System.out.println(output);
        if(output <= 4) {
            for (int i = 0; i < output; i++) {
                BLOCK.paintIcon(null, g, pos.x - output*50/2 + i*50, pos.y - 15 - animationOffset);
            }
        } else {
            for (int i = 0; i < output - 4; i++) {
                COIN.paintIcon(null, g, pos.x - (output-4)*50/2 + i*50 + 8, pos.y + 15 - animationOffset);
            }
        }
    }

    @Override
    public String[] getActions() {
        return ACTIONS;
    }

    @Override
    public void react(Machine machine, int messageIndex) {
    	/* if (messageIndex == 8) {
    		// Ausgabe wird geleert
    		messageIndex = -1;
    	} */
    	
        output = messageIndex + 1;
        animationOffset = 100;
        Timer t = new Timer(20, e -> animationStep(e, machine));
        t.start();
    }

    @Override
    public String toString() {
        return Util.r("panel.output");
    }

    private void animationStep(ActionEvent e, Machine machine) {
        if(animationOffset > 0) {
            animationOffset -= 10;
            machine.repaint();
        } else {
            animationOffset = 0;
            ((Timer)e.getSource()).stop();
        }
    }

	@Override
	public void changeDesign() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getElementHeightHalf() {
		return SLOT.getIconHeight()/2;
	}

	@Override
	public int getElementWidthHalf() {
		return SLOT.getIconWidth()/2;
	}
	
	public void setOutput(int output) {
		this.output = output;
	}

	
}
