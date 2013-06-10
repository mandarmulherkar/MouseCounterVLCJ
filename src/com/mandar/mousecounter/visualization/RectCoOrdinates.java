package com.mandar.mousecounter.visualization;

public class RectCoOrdinates {

	private int xl;
	private int yl;
	private int xr;
	private int yr;
	
	public RectCoOrdinates(int x1, int y1, int x2, int y2){
		
		this.xl = x1;
		this.yl = y1;
		this.xr = x2;
		this.yr = y2;
	}
	
	public RectCoOrdinates() {
		
	}

	public void setX1(int x){
		this.xl = x;
	}
	
	public void setX2(int x){
		this.xr = x;
	}
	
	
	public int getX1(){
		return xl;
	}
	
	public int getX2(){
		return xr;
	}

	public int getY2() {
		return 0;
	}

	public int getY1() {
		return 0;
	}

}
