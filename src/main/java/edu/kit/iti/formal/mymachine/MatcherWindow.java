package edu.kit.iti.formal.mymachine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MatcherWindow extends JLayeredPane {

    private static final Icon SOCKET = Util.imageResource("socket.png");
    private static final Icon LABEL = Util.imageResource("label.png");
    private static final Font FONT = new Font(Font.MONOSPACED, Font.BOLD, 26);

    private static final Point SOCKET_LOCATION = new Point(30, 30);

    private List<AlternativeComponent> alternatives;

    public MatcherWindow(Collection<String> strings) {
        alternatives = new ArrayList<>();

        SocketComponent socket = new SocketComponent();
        socket.setLocation(SOCKET_LOCATION);
        add(socket);
        setLayer(socket, 0);

        int y = 30;
        for (String string : strings) {
            AlternativeComponent alt = new AlternativeComponent(string);
            alt.setLocation(250, y);
            alternatives.add(alt);
            add(alt);
            setLayer(alt, 1);
            y += LABEL.getIconHeight();
        }

        JLabel label = new JLabel("LABEL");
        label.setLocation(200, 200);
        add(label);

        setPreferredSize(new Dimension(400, y + 20));

    }

    private static class SocketComponent extends JComponent {

        public SocketComponent() {
            setSize(SOCKET.getIconWidth(), SOCKET.getIconHeight());
        }

        @Override
        protected void paintComponent(Graphics g) {
            SOCKET.paintIcon(this, g, 0, 0);
        }
    }

    private static class AlternativeComponent extends JComponent {
        private final String string;
        private Point lastPos;
        private int socket = -1;

        private MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastPos = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                setLocation(getX() + e.getPoint().x - lastPos.x,
                        getY() + e.getPoint().y - lastPos.y);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("getLocation() = " + getLocation());
                Point loc = getLocation();
                loc.translate(- SOCKET_LOCATION.x - SOCKET.getIconWidth(),
                        - SOCKET_LOCATION.y);
                System.out.println("loc = " + loc);
                int H = LABEL.getIconHeight() - 4;
                for (int i = 0; i < 3; i++) {
                    if (-100 <= loc.x && loc.x <= 10 &&
                            -40 + i * H <= loc.y && loc.y <= 40 + i * H) {
                        loc.setLocation(SOCKET_LOCATION);
                        loc.translate(73, i * H);
                        setLocation(loc);
                        socket = i;
                        return;
                    }
                }

                socket = -1;
            }
        };

        public AlternativeComponent(String string) {
            this.string = string;
            setSize(LABEL.getIconWidth(), LABEL.getIconHeight());
            addMouseListener(mouse);
            addMouseMotionListener(mouse);
        }

        @Override
        protected void paintComponent(Graphics g) {
            LABEL.paintIcon(this, g, 0, 0);
            g.setFont(FONT);
            int add = g.getFontMetrics().getAscent() / 2;
            g.drawString(string, 50, getHeight() / 2 + add);
        }
    }


    public String[] getMatches() {
        String[] result = new String[3];

        for (AlternativeComponent c : alternatives) {
            if (c.socket != -1) {
                if (result[c.socket] != null) {
                    return null;
                }
                result[c.socket] = c.string;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("MATCHER");
        f.getContentPane().setLayout(new BorderLayout());
        MatcherWindow matcherWindow = new MatcherWindow(Arrays.asList("Looooooooooooong", "Short", "3", "4"));
        f.getContentPane().add(matcherWindow, BorderLayout.CENTER);
        JButton b = new JButton("OK");
        b.addActionListener(e -> System.out.println(Arrays.toString(matcherWindow.getMatches())));

        f.getContentPane().add(b, BorderLayout.SOUTH);
        f.pack();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }


}
