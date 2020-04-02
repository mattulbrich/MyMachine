package edu.kit.iti.formal.mymachine.automata;

import java.awt.*;

public class Transition {
    private final State from;
    private final State to;
    private final String in;
    private final String out;

    public Transition(State from, State to, String in, String out) {
        this.from = from;
        this.to = to;
        this.in = in;
        this.out = out;
    }

    public void paint(Graphics2D g2) {
        int x2 = (to.getPosition().x + from.getPosition().x) / 2 + State.STATE_SIZE/2;
        int y2 = (to.getPosition().y + from.getPosition().y) / 2 + State.STATE_SIZE/2;

        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(3f));
        g2.drawLine(from.getPosition().x + State.STATE_SIZE/2, from.getPosition().y+ State.STATE_SIZE/2,
                to.getPosition().x+ State.STATE_SIZE/2, to.getPosition().y+ State.STATE_SIZE/2 );
        g2.drawString(in, x2, y2);
        g2.drawString(out, x2, y2+20);
    }

    public State getFrom() {
        return from;
    }

    public State getTo() {
        return to;
    }

    public String getIn() {
        return in;
    }

    public String getOut() {
        return out;
    }
}
