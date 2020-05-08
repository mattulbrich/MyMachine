package edu.kit.iti.formal.mymachine;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;

public class Util {
    private static final Font FONT = new Font(Font.DIALOG, Font.BOLD, 20);
    private static final Icon FAIL_ICON = new Icon() {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(Color.red);
            g.setFont(FONT);
            g.drawString("XXX", x+4, y+30);
        }

        @Override
        public int getIconWidth() {
            return 30;
        }

        @Override
        public int getIconHeight() {
            return 40;
        }
    };

    public static Icon imageResource(String filename) {
        URL url = ClassLoader.getSystemResource("img/" + filename);
        if(url == null) {
            return FAIL_ICON;
        } else {
            return new ImageIcon(url);
        }
    }

    public static Dimension getDimension(Icon icon) {
        return new Dimension(icon.getIconWidth(), icon.getIconHeight());
    }
}
