package com.mandar.mousecounter.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.mandar.mousecounter.behaviorevent.BehaviorEnum;


public class VisualizationPanel extends JPanel implements ComponentListener{

	private static final int PANEL_PADDING = 15;
	private static final int BEHAVIOR_PANEL_WIDTH = 633;
	private static final int BEHAVIOR_PANEL_HEIGHT = 40;

	private static final long serialVersionUID = 1L;
	
    private Dimension dimension = new Dimension(BEHAVIOR_PANEL_WIDTH,BEHAVIOR_PANEL_HEIGHT);
    private List<BehaviorInfo>rectanglesList = new LinkedList<BehaviorInfo>();
    private BehaviorInfo behaviorInfo;
    
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
        if(behaviorInfo != null){
        	if(startX){
        		g.drawLine(behaviorInfo.getX1(), 0, behaviorInfo.getX1(), 40);
        	}
    		for(BehaviorInfo coords : rectanglesList){
    			g.setColor(getMappedColor(coords));
    			g.fillRect(coords.getX1(), 0, coords.getX2() - coords.getX1(), 40);
    		}
        }
    }

	private Color getMappedColor(BehaviorInfo coords) {

		if(null == coords) {
			return Color.black;
		}
		if(null == coords.getBehaviorEnum()){
			return Color.black;
		}
		switch(coords.getBehaviorEnum()){
		case LICK:
			return Color.cyan;
		case SCRATCH: 
			return Color.green;
		case GUARD:
			return Color.magenta;
		case WIPE: 
			return Color.orange;
		case FLINCH:
			return Color.red;
		case SNIFF: 
			return Color.yellow;
		case LIFT:
			return Color.pink;
		case NONE:
			return Color.darkGray;
		default:
			return Color.black;
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
		behaviorInfo = new BehaviorInfo();
		behaviorInfo.setX1(x);
		startX = true;
		repaint();
	}  

	public void endVisualization(float f) {
		int x = (int) (dimension.getWidth() * f);
		System.out.println("x2 "+x);
		if( behaviorInfo != null) {
			behaviorInfo.setX2(x);
			rectanglesList.add(behaviorInfo);
			startX = false;
			repaint();
			//newXCoOrds.getX1(), newXCoOrds.getY1(), newXCoOrds.getX2(), newXCoOrds.getY2());
		}
	}
		
	public void recordBehavior(BehaviorEnum be) {
		if(null != behaviorInfo){
			behaviorInfo.setBehaviorEnum(be);
		}
		repaint();
			
	}
			
}
