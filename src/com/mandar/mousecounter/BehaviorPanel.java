package com.mandar.mousecounter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BehaviorPanel extends JPanel implements ComponentListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int squareX = 10;
    private int squareY = 10;
    private int squareW = 30;
    private int squareH = 30;

    private Dimension dimension;
    
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
        g.drawLine(squareX, squareY, squareX, squareH);
        
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
		squareX =x;
		squareY =10;
		squareX =x;
		squareH = 30;
		
		repaint(x, 10, x, 30);
	}  

}
