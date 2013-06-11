package com.mandar.mousecounter.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


public class VisualizationPanel extends JPanel implements ComponentListener{

	private static final int PANEL_PADDING = 15;
	private static final int BEHAVIOR_PANEL_WIDTH = 633;
	private static final int BEHAVIOR_PANEL_HEIGHT = 40;

	private static final long serialVersionUID = 1L;
	
    private Dimension dimension = new Dimension(BEHAVIOR_PANEL_WIDTH,BEHAVIOR_PANEL_HEIGHT);
    private List<RectCoOrdinates>rectanglesList = new LinkedList<RectCoOrdinates>();
    private RectCoOrdinates newXCoOrds;
    
    private static boolean startX = true;
	public VisualizationPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        addComponentListener(this);
	}

	public Dimension getPreferredSize() {
        return new Dimension(BEHAVIOR_PANEL_WIDTH,BEHAVIOR_PANEL_HEIGHT);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);  
        g.setColor(Color.BLACK);
        if(newXCoOrds != null){
        	if(startX){
        		g.drawLine(newXCoOrds.getX1(), 0, newXCoOrds.getX1(), 40);
        	}
    		for(RectCoOrdinates coords : rectanglesList){
    			g.fillRect(coords.getX1(), 0, coords.getX2() - coords.getX1(), 40);
    		}
        	
        }
    }

	@Override
	public void componentHidden(ComponentEvent arg0) {
		System.out.println("Hidden");
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		System.out.println("Moved");
		
	}

	@Override
	public void componentResized(ComponentEvent ce) {
		
		System.out.println("Resized");
		dimension = ce.getComponent().getSize();
		System.out.println(""+dimension.getHeight()+", "+dimension.getWidth());
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		System.out.println("Shown");
		
	}

	public void startVisualization(float f) {
		int x = (int) (dimension.getWidth() * f);
		System.out.println("x1 "+x);
		newXCoOrds = new RectCoOrdinates();
		newXCoOrds.setX1(x);
		startX = true;
		repaint();
	}  

	public void endVisualization(float f) {
		int x = (int) (dimension.getWidth() * f);
		System.out.println("x2 "+x);
		if( newXCoOrds != null) {
			newXCoOrds.setX2(x);
			rectanglesList.add(newXCoOrds);
			startX = false;
			repaint();
			//newXCoOrds.getX1(), newXCoOrds.getY1(), newXCoOrds.getX2(), newXCoOrds.getY2());
		}
		
	}
	
}
