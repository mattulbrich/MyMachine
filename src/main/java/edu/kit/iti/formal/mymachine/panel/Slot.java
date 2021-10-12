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

public class Slot extends MachineElement {

    // private static final Icon SLOT_NO_COIN = Util.imageResource("coinSlotEmpty.png");
	private static Icon SLOT_NO_COIN = Util.imageResource("coinSlotEmpty");
	
    // private static final Icon SLOT_COIN = Util.imageResource("coinSlotCoin.png");
	private static Icon SLOT_COIN = Util.imageResource("coinSlotCoin");

    public Slot() {
        super("#Coin", Util.getDimension(SLOT_COIN), true);
    }

    @Override
    public void uiConfig(Machine machine) {
    }

    @Override
    public void paint(Graphics2D g, PaintMode mode) {

        if (mode == PaintMode.PRESSED) {
            Util.drawCentered(g, getPosition(), SLOT_COIN);
        } else {
            Util.drawCentered(g, getPosition(), SLOT_NO_COIN);
        }
    }

    @Override
    public String toString() {
        return Util.r("panel.slot");
    }

	@Override
	public void changeDesign() {
		SLOT_NO_COIN = Util.imageResource("coinSlotEmpty");
		SLOT_COIN = Util.imageResource("coinSlotCoin");
	}

	@Override
	public int getElementHeightHalf() {
		return SLOT_COIN.getIconHeight()/2;
	}

	@Override
	public int getElementWidthHalf() {
		return SLOT_COIN.getIconWidth()/2;
	}

}
