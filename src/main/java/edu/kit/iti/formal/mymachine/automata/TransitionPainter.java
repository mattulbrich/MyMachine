package edu.kit.iti.formal.mymachine.automata;


import edu.kit.iti.formal.mymachine.Pair;
import edu.kit.iti.formal.mymachine.panel.MachineElement;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TransitionPainter {

    public static final TransitionPainter INSTANCE = new TransitionPainter();

    private static final Color OUTPUT_COLOR = Color.ORANGE.darker();
    private static final Color TRIGGER_COLOR = Color.GREEN.darker();
    private static final Color PARAM_COLOR = Color.gray;

    public void paintTransitions(Graphics2D g, List<Transition> transitions) {
        Map<Pair<State,State>, List<Transition>> collected = collectTransitions(transitions);

        for (List<Transition> value : collected.values()) {
            paintTransitionList(g, value);
        }
    }

    private Map<Pair<State, State>, List<Transition>> collectTransitions(List<Transition> transitions) {
        Map<Pair<State, State>, List<Transition>> result = new HashMap<>();
        for (Transition transition : transitions) {
            Pair<State, State> key = new Pair<>(transition.getFrom(), transition.getTo());
            List<Transition> list = result.get(key);
            if (list == null) {
                list = new LinkedList<>(Arrays.asList(transition));
                result.put(key, list);
            } else {
                list.add(transition);
            }
        }
        return result;
    }

    private void paintTransitionList(Graphics2D g, List<Transition> transitions) {

        assert !transitions.isEmpty();

        State from = transitions.get(0).getFrom();
        State to = transitions.get(0).getTo();

        if(from == to) {
            paintSelfLoops(g, transitions);
            return;
        }

        Point toPos = new Point(to.getPosition());
        Point fromPos = new Point(from.getPosition());

        // to make back and forth better distinguishable
        translatePoints(fromPos, toPos);

        // Draw line
        g.setColor(Color.black);
        g.drawLine(fromPos.x, fromPos.y, toPos.x, toPos.y);
        paintArrowHead(g, fromPos, toPos);

        int x2 = (toPos.x + fromPos.x) / 2;
        int y2 = (toPos.y + fromPos.y) / 2;
        Point middle = new Point(x2, y2);

        List<List<Pair<Color, String>>> textBlock = makeTextBlock(transitions);
        Dimension dim = computeDimension(g.getFontMetrics(), textBlock);

        if (toPos.x > fromPos.x) {
            // left to right
            middle.translate(0, -dim.height);
        }

        if (toPos.y < fromPos.y) {
            // bottom to top
            middle.translate(-dim.width, 0);
        }

        // debug: g.drawRect(middle.x,middle.y,dim.width,dim.height);
        paintTextBlock(g, middle, textBlock);
    }

    private void paintArrowHead(Graphics2D g, Point fromPos, Point toPos) {
        // Draw arrowhead
        double angle = Math.atan2(toPos.y-fromPos.y, toPos.x-fromPos.x);
        Graphics2D gc = (Graphics2D) g.create();
        gc.rotate(angle, toPos.x, toPos.y);
        gc.drawLine(toPos.x - State.STATE_SIZE_HALF, toPos.y,
                toPos.x- State.STATE_SIZE_HALF - 10, toPos.y - 10);
        gc.drawLine(toPos.x - State.STATE_SIZE_HALF, toPos.y,
                toPos.x- State.STATE_SIZE_HALF - 10, toPos.y + 10);
    }

    private void paintSelfLoops(Graphics2D g, List<Transition> transitions) {

        assert !transitions.isEmpty();

        State state = transitions.get(0).getFrom();

        // draw the loop:
        Point pos = state.getPosition();
        g.setColor(Color.black);
        g.drawOval(pos.x, pos.y, State.STATE_SIZE, State.STATE_SIZE);

        // draw arrow head
        Point virtualFromPos = new Point(pos.x+10, pos.y);
        paintArrowHead(g, virtualFromPos, pos);

        Point blockPos = new Point(pos.x + State.STATE_SIZE, pos.y + State.STATE_SIZE_HALF);
        List<List<Pair<Color, String>>> textBlock = makeTextBlock(transitions);
        paintTextBlock(g, blockPos, textBlock);
    }

    private void paintTextBlock(Graphics2D g, Point point, List<List<Pair<Color, String>>> textBlock) {
        int x = point.x;
        int y = point.y;
        FontMetrics fm = g.getFontMetrics();

        for (List<Pair<Color, String>> line : textBlock) {
            int asc = fm.getAscent();
            for (Pair<Color, String> part : line) {
                g.setColor(part.fst);
                g.drawString(part.snd, x, y + asc);
                x += SwingUtilities.computeStringWidth(fm, part.snd);
            }
            x = point.x;
            y += fm.getHeight();
        }

    }

    private Dimension computeDimension(FontMetrics fm, List<List<Pair<Color, String>>> textBlock) {
        int w = 0;
        int h = 0;
        for (List<Pair<Color, String>> pairs : textBlock) {
            StringBuilder sb = new StringBuilder();
            for (Pair<Color, String> elem : pairs) {
                sb.append(elem.snd);
            }
            w = Math.max(w, SwingUtilities.computeStringWidth(fm, sb.toString()));
            h += fm.getHeight();
        }
        return new Dimension(w, h);
    }

    private List<List<Pair<Color, String>>> makeTextBlock(List<Transition> transitions) {
        List<List<Pair<Color, String>>> result = new ArrayList<>();
        for (Transition transition : transitions) {
            result.add(Collections.singletonList(
                    new Pair<>(TRIGGER_COLOR, transition.getTrigger().toString())));

            MachineElement out = transition.getOutput();
            if(out != null) {
                List<Pair<Color, String>> line = new LinkedList<>();
                line.add(new Pair<>(OUTPUT_COLOR, " " + out));
                line.add(new Pair<>(PARAM_COLOR,
                        " " + out.getOutputLabel(transition.getMessageIndex())));
                result.add(line);
            }

            out = transition.getOutput2();
            if(out != null) {
                List<Pair<Color, String>> line = new LinkedList<>();
                line.add(new Pair<>(OUTPUT_COLOR, " " + out));
                line.add(new Pair<>(PARAM_COLOR,
                        " " + out.getOutputLabel(transition.getMessageIndex2())));
                result.add(line);
            }
        }

        return result;
    }

    private static void translatePoints(Point a, Point b) {
        int dx = b.x - a.x;
        int dy = b.y - a.y;
        double length = Math.hypot(dx, dy);
        a.translate((int)(10*dy/length), (int)(-10*dx/length));
        b.translate((int)(10*dy/length), (int)(-10*dx/length));
    }

}
