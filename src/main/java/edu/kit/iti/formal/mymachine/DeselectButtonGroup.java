package edu.kit.iti.formal.mymachine;


import javax.swing.*;

/**
 * A button group that deselects a button if you click a second time.
 *
 * see https://stackoverflow.com/questions/37598206/how-to-deselect-already-selected-jradiobutton-by-clicking-on-it
 */
public class DeselectButtonGroup extends ButtonGroup {

    private ButtonModel prevModel;

    private boolean isAdjusting = false;

    @Override
    public void setSelected(ButtonModel m, boolean b) {
        if (isAdjusting) {
            return;
        }
        if (m.equals(prevModel)) {
            isAdjusting = true;
            clearSelection();
            isAdjusting = false;
        } else {
            super.setSelected(m, b);
        }
        prevModel = getSelection();
    }
}
