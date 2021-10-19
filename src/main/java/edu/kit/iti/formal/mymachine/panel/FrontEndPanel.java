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


import edu.kit.iti.formal.mymachine.util.Util;
import edu.kit.iti.formal.mymachine.panel.MachineElement.PaintMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collections;
import java.util.Iterator;

/**
 * This is the component that actually realises the machine's swing UI.
 */
public class FrontEndPanel extends JComponent implements MouseListener, MouseMotionListener {

    // private static final Icon BACKGROUND =
    //        Util.imageResource("metal.jpg");
	// private static final Icon BACKGROUND =
	// 	            Util.imageResource("paperboard.png");
	
	private static Icon BACKGROUND = Util.imageResource("background");
	
	private boolean toolTipActivated;

    private static final double SCALE_FACTOR;
    static {
        double f;
        try {
            f = Double.parseDouble(System.getProperty("mymachine.panel.scale", "1.0"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            f = 1.0;
        }
        SCALE_FACTOR = f;
    }


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

        if (SCALE_FACTOR != 1.0f) {
            g2.scale(SCALE_FACTOR, SCALE_FACTOR);
        }

        g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON));

        g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        for (int x = 0; x < getWidth()/SCALE_FACTOR; ) {
            for (int y = 0; y < getHeight()/SCALE_FACTOR; ) {
                BACKGROUND.paintIcon(this, g, x, y);
                y += BACKGROUND.getIconHeight();
            }
            x += BACKGROUND.getIconWidth();
        }
        
        // g.setColor(Color.lightGray);
        // g.fillRect(0,0,getWidth(),getHeight());

        for (MachineElement machineElement : designPane.getMachineElements()) {
            PaintMode localPaintMode = paintMode;
            if (draggedPos != null && !machineElement.contains(draggedPos) ||
                 !designPane.getMachine().isPlayMode()) {
                // only the element under the mouse is concerned
                // and only in play mode
                localPaintMode = PaintMode.NEUTRAL;
                
            }
            machineElement.paint(g2, localPaintMode);
            machineElement.paintLabel(g2);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        Point point = scalePoint(mouseEvent.getPoint());

        /* Wenn Automat ausgeführt wird */
        if(designPane.getMachine().isPlayMode()) {
            for (MachineElement element : designPane.getMachineElements()) {
                if (element.contains(point) && element.isActive()) {
                    designPane.getMachine().fire(element);
                }
            }
            return;
        }

        if(designPane.isDeleteMode()) {
            Iterator<MachineElement> it = designPane.getMachineElements().iterator();
            while (it.hasNext()) {
                MachineElement element = it.next();
                if (element.contains(point)) {
                    if(element.canBeDeleted(designPane.getMachine())) {
                        it.remove();
                        repaint();
                        
                        /*
                         * Nach dem Löschen eines Elements soll geprüft werden, ob ein Button aktiviert werden soll.
                         */
                        
                        for (Component component : Collections.list( designPane.buttonGroup.getElements() ) ) {
                    		
                    		if ( ((JToggleButton) component).getText().equals(Util.r("panel.display")) && element.getName().equals("#Display")) {
                    			component.setEnabled(true);
                    		}
                    		
                    		else if ( ((JToggleButton) component).getText().equals(Util.r("panel.output")) && element.getName().equals("#Output" ) ) {
                    	        	component.setEnabled(true);
                    	        }
                    		
                    		if (((JToggleButton) component).getText().equals(Util.r("panel.coinslot") )  && element.getName().equals("#Coin" ) ) {
                    			component.setEnabled(true);
                            }
                    		
                    	}
                        
                        /*
                         * 
                         */
                        
                        
                    } else {
                        JOptionPane.showMessageDialog(this,
                                Util.r("panel.cannotDelete"));
                    }
                    return;
                }
            }
            /*
             * Wenn im Löschen-Modus auf das Bedienfeld geklickt wird, soll der Löschen-Modus beendet werden. 
             */
            designPane.finishAction();
        }

        if(designPane.isAddMode()) {
            MachineElement element = designPane.createElement();
            element.setPosition(point);
            repaint();
            element.uiConfig(designPane);
            if (designPane.getMachineElement(element.toString()) != null) {
                JOptionPane.showMessageDialog(this,
                                Util.r("panel.nameAlreadyUsed"));
                return;
            }
            designPane.addMachineElement(element);
            designPane.finishAction();
            repaint();
        }
    }

    private static Point scalePoint(Point point) {
        if (SCALE_FACTOR != 1.0) {
            return new Point((int)(point.x / SCALE_FACTOR + .5), (int)(point.y / SCALE_FACTOR + .5));
        } else {
            return point;
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if(designPane.isMoveMode()) {
            Iterator<MachineElement> it = designPane.getMachineElements().iterator();
            Point point = scalePoint(mouseEvent.getPoint());
            while (it.hasNext()) {
                MachineElement element = it.next();
                if (element.contains(point)) {
                    draggedElement = element;
                    draggedPos = point;
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
        if(draggedElement != null && !designPane.getMachine().isPlayMode() &&
                !designPane.getMachine().isFixedInterface()) {
        	
        	/* Elemente sollen nicht außerhalb des Felds gezogen werden dürfen. */
        	int x_max = mouseEvent.getComponent().getParent().getBounds().width;
        	int y_max = mouseEvent.getComponent().getParent().getBounds().height;
    		
    		Point r = draggedElement.getPosition();
    		
    		if (r.x - draggedElement.getElementWidthHalf() < 0) {
    			draggedElement.setPosition(new Point(draggedElement.getElementWidthHalf(), draggedElement.getPosition().y));
    		}  
    		else if (r.x + draggedElement.getElementWidthHalf() > x_max) {
    			draggedElement.setPosition(new Point(x_max-draggedElement.getElementWidthHalf(), draggedElement.getPosition().y));
    		}
    		
    		
    		if (r.y - draggedElement.getElementHeightHalf() < 0) {
    			draggedElement.setPosition(new Point(draggedElement.getPosition().x, draggedElement.getElementHeightHalf()));
    		}  
    		else if (r.y + draggedElement.getElementHeightHalf() > y_max) {
    			draggedElement.setPosition(new Point(draggedElement.getPosition().x, y_max-draggedElement.getElementHeightHalf()));
    		}
    	
    		/* Ende */
            
        	
        	
            Point point = scalePoint(mouseEvent.getPoint());
            draggedElement.drag(draggedPos, point);
            draggedPos = point;
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
    	if (designPane.isPlayMode()) {
	    	boolean flag = false;
	    	Iterator<MachineElement> it = designPane.getMachineElements().iterator();
	        Point point = scalePoint(mouseEvent.getPoint());
	        while (it.hasNext()) {
	        	MachineElement element = it.next();
	            if ( (element instanceof Button || element instanceof Output ||  element instanceof Slot) && element.contains(point) ) {
	            	setCursor(new Cursor(Cursor.HAND_CURSOR));
	            	flag = true;
	            }
	        }
	        if (!flag) {
	            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	        }
    	}
    }
    
    /**
     * This method is called automatically when the mouse is over the component.
     * Based on the location of the event, we detect if we are over one of 
     * the circles. If so, we display some information relative to that circle
     * If the mouse is not over any circle we return the tooltip of the 
     * component.
     */
     @Override
     public String getToolTipText(MouseEvent event) {
    	 
         Point point = new Point(event.getX(), event.getY());
         
         Iterator<MachineElement> it = designPane.getMachineElements().iterator();
         
         while (it.hasNext()) {
             MachineElement element = it.next();
             if (element.contains(point)) {
            	 if (toolTipActivated) {
            		 return infoText(element);
            	 } else {
            		 return ""; 
            	 }
             }
         }
         
         return "";
         
     }
    
    public void changeDesign() {
    	BACKGROUND = Util.imageResource("background");
    }
    
    private String infoText(MachineElement element) {
    	String str = element.getClass().getSimpleName();
    	
    	switch(str) {
    		case "Button": return Util.r("tooltip.info_button");
    	
    		case "LED": return Util.r("tooltip.info_led");
    		
    		case "Output": return Util.r("tooltip.info_output");
    		
    		case "Slot": return Util.r("tooltip.info_coinslot");
    		
    		case "Display": return Util.r("tooltip.info_display");
    		
    		default: return "";
    	}
    	
    }
    
    public void activateToolTips() {
    	toolTipActivated = true;
    	setToolTipText("");
    }
    
    public void deactivateToolTips() {
    	toolTipActivated = false;
    	setToolTipText("");
    }
}
