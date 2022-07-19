package edu.kit.iti.formal.mymachine.panel.fixed;

import edu.kit.iti.formal.mymachine.Machine;
import edu.kit.iti.formal.mymachine.panel.LED;
import edu.kit.iti.formal.mymachine.panel.Button;
import edu.kit.iti.formal.mymachine.panel.Slot;

import java.awt.*;

public class FixedInterfaces {

    public static final int SECOND_COL = 450;
    private static final int LED_X_SEP = 80;
    private static final int BUTTON_Y_SEP = 140;
    private static final int FIRST_BUTTON_Y = 200;
    public static final int OUTPUT_HEIGHT = 650;

    public static void addFixedInterfaceElements(Machine machine) {
        {
            LED led = new LED();
            led.setName("1");
            led.setPosition(new Point(SECOND_COL - LED_X_SEP, 40));
            machine.addMachineElement(led);
        }
        {
            LED led = new LED();
            led.setName("2");
            led.setPosition(new Point(SECOND_COL, 40));
            machine.addMachineElement(led);
        }
        {
            LED led = new LED();
            led.setName("3");
            led.setPosition(new Point(SECOND_COL + LED_X_SEP, 40));
            machine.addMachineElement(led);
        }

        {
            Button button = new Button();
            button.setName("1");
            button.setPosition(new Point(SECOND_COL, FIRST_BUTTON_Y));
            machine.addMachineElement(button);

        }
        {
            Button button = new Button();
            button.setName("2");
            button.setPosition(new Point(SECOND_COL, FIRST_BUTTON_Y + BUTTON_Y_SEP));
            machine.addMachineElement(button);
        }
        {
            Button button = new Button();
            button.setName("3");
            button.setPosition(new Point(SECOND_COL, FIRST_BUTTON_Y + 2 * BUTTON_Y_SEP));
            machine.addMachineElement(button);
        }
        {
            ShowPicture picture = new ShowPicture();
            picture.setPosition(new Point(180, 340));
            machine.addMachineElement(picture);
        }
        {
            MultiTypeOutput output = new MultiTypeOutput();
            output.setPosition(new Point(180, OUTPUT_HEIGHT));
            machine.addMachineElement(output);
        }
        {
            Slot slot = new Slot();
            slot.setPosition(new Point(SECOND_COL - 21, OUTPUT_HEIGHT));
            machine.addMachineElement(slot);
        }
        {
            CoinTwoSlot slot = new CoinTwoSlot();
            slot.setPosition(new Point(SECOND_COL + 17, OUTPUT_HEIGHT));
            machine.addMachineElement(slot);
        }
    }
}
