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


import edu.kit.iti.formal.mymachine.panel.MachineElement.PaintMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;

public class FrontEndPanel extends JComponent implements MouseListener, MouseMotionListener {

    private DesignPane designPane;

    /**
     * The object which is dragged around using Drag'n'Drop
     */
    private MachineElement draggedElement;

    /**
     * The position at which the current drag started or the last update to positions has been
     * performed. May be updated regularly.
     */
    private Point draggedPos;

    /**
     * If the mouse is pressed or when we are over a reactive component change this.
     */
    private PaintMode paintMode = PaintMode.NEUTRAL;

    public FrontEndPanel(DesignPane designPane) {
        this.designPane = designPane;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON));

        g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        g.setColor(Color.lightGray);
        g.fillRect(0,0,getWidth(),getHeight());

        for (MachineElement machineElement : designPane.getMachineElements()) {
            machineElement.paint(g2, paintMode);
            machineElement.paintLabel(g2);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        if(designPane.getMachine().isPlayMode()) {
            for (MachineElement element : designPane.getMachineElements()) {
                if (element.contains(mouseEvent.getPoint()) && element.isActive()) {
                    String command = element.getName();
                    if (command != null) {
                        designPane.getMachine().fire(command);
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
            element.setPosition(mouseEvent.getPoint());
            repaint();
            element.uiConfig();
            if (designPane.getMachineElement(element.getName()) != null) {

            }
            designPane.addMachineElement(element);
            designPane.finishAction();
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
                    paintMode = PaintMode.PRESSED;
                    repaint();
                    return;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        draggedElement = null;
        paintMode = PaintMode.NEUTRAL;
        repaint();
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
