package edu.kit.iti.formal.mymachine;


import edu.kit.iti.formal.mymachine.MachineElement.PaintMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;

public class FrontEndPanel extends JComponent implements MouseListener, MouseMotionListener {

    private DesignPane designPane;
    private MachineElement draggedElement;
    private Point draggedPos;

    public FrontEndPanel(DesignPane designPane) {
        this.designPane = designPane;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g.setColor(Color.lightGray);
        g.fillRect(0,0,getWidth(),getHeight());

        for (MachineElement machineElement : designPane.getMachineElements()) {
            machineElement.paint(g2, PaintMode.NEUTRAL);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        if(designPane.getFrame().isPlayMode()) {
            for (MachineElement element : designPane.getMachineElements()) {
                if (element.contains(mouseEvent.getPoint())) {
                    String command = element.getCommand();
                    if (command != null) {
                        designPane.getFrame().fire(command);
                    }
                }
            }
            return;
        }

        if(designPane.isDeleteMode()) {
            Iterator<MachineElement> it = designPane.getMachineElements().iterator();
            while (it.hasNext()) {
                if (it.next().contains(mouseEvent.getPoint())) {
                    it.remove();
                    repaint();
                    return;
                }
            }
        }

        if(designPane.isAddMode()) {
            MachineElement element = designPane.createElement();
            element.uiConfig();
            element.setPosition(mouseEvent.getPoint());
            designPane.getMachineElements().add(element);
            designPane.unselectButton();
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if(designPane.isMoveMode()) {
            Iterator<MachineElement> it = designPane.getMachineElements().iterator();
            while (it.hasNext()) {
                MachineElement element = it.next();
                if (element.contains(mouseEvent.getPoint())) {
                    draggedElement = element;
                    draggedPos = mouseEvent.getPoint();
                    repaint();
                    return;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        draggedElement = null;
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if(draggedElement != null) {
            draggedElement.drag(draggedPos, mouseEvent.getPoint());
            draggedPos = mouseEvent.getPoint();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}
