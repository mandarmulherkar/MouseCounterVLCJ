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


public class BehaviorPanel extends JPanel implements ComponentListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int xLeft;
	private int xRight;
	
	private int squareX = 10;
    private int squareY = 10;
    private int squareW = 30;
    private int squareH = 30;

    private Dimension dimension;
    private List<RectCoOrdinates>rectanglesList = new LinkedList<RectCoOrdinates>();
    private RectCoOrdinates newXCoOrds;
    
	public BehaviorPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    	addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                moveSquare(e.getX(),e.getY());
                System.out.println(""+e.getX()+", "+e.getY());
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                moveSquare(e.getX(),e.getY());
                System.out.println(""+e.getX()+", "+e.getY());
            }
        });
        addComponentListener(this);
	}

	private void moveSquare(int x, int y) {
        int OFFSET = 1;
        if ((squareX!=x) || (squareY!=y)) {
            repaint(squareX,squareY,squareW+OFFSET,squareH+OFFSET);
            squareX=x;
            squareY=y;
            repaint(squareX,squareY,squareW+OFFSET,squareH+OFFSET);
        } 
    }

	public Dimension getPreferredSize() {
        return new Dimension(250,40);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);  
        g.setColor(Color.BLACK);
        if(newXCoOrds != null){
        	g.fillRect(newXCoOrds.getX1(), 10, newXCoOrds.getX2(), 20);
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
		System.out.println("x co ord"+x);
		newXCoOrds = new RectCoOrdinates();
		newXCoOrds.setX1(x);
	}  

	public void endVisualization(float f) {
		int x = (int) (dimension.getWidth() * f);
		System.out.println("x co ord"+x);
		if( newXCoOrds != null) {
			newXCoOrds.setX2(x);
			rectanglesList.add(newXCoOrds);
			
			repaint();
			//newXCoOrds.getX1(), newXCoOrds.getY1(), newXCoOrds.getX2(), newXCoOrds.getY2());
		}
		
	}
	
}
