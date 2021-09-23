package edu.kit.iti.formal.mymachine.panel.fixed;

import edu.kit.iti.formal.mymachine.Machine;
import edu.kit.iti.formal.mymachine.panel.MachineElement;
import edu.kit.iti.formal.mymachine.panel.Slot;
import edu.kit.iti.formal.mymachine.util.Util;

import javax.swing.*;
import java.awt.*;

public class CoinTwoSlot extends MachineElement {


    // private static final Icon SLOT_NO_COIN = Util.imageResource("coin2SlotEmpty.png");
	private static Icon SLOT_NO_COIN = Util.imageResource("coin2SlotEmpty");
	
	// private static final Icon SLOT_COIN = Util.imageResource("coin2SlotCoin.png");
	private static Icon SLOT_COIN = Util.imageResource("coin2SlotCoin");

    public CoinTwoSlot() {
        super("#Coin2", Util.getDimension(SLOT_NO_COIN), true);
    }

    @Override
    public void uiConfig(Machine machine) {
    }

    @Override
    public void paint(Graphics2D g, PaintMode mode) {

        if (mode == PaintMode.PRESSED) {
            draw(g, SLOT_COIN);
        } else {
            draw(g, SLOT_NO_COIN);
        }
    }

    private void draw(Graphics2D g, Icon icon) {
        // HACK The "-2" avoids a nasty graphics gap :)
        icon.paintIcon(null, g, getPosition().x - icon.getIconWidth() +  getDimension().width/2-2,
                getPosition().y - getDimension().height/2);
    }


    @Override
    public String toString() {
        return Util.r("panel.slot_coin2");
    }

	@Override
	public void changeDesign() {
		SLOT_NO_COIN = Util.imageResource("coin2SlotEmpty");
		SLOT_COIN = Util.imageResource("coin2SlotCoin");
	}

}
