package com.mandar.mousecounter.visualization;

import com.mandar.mousecounter.behaviorevent.BehaviorEnum;

public class BehaviorInfo {

	private int xl;
	private int xr;
	
	private BehaviorEnum behaviorEnum;
	
	public BehaviorInfo(int x1, int y1, int x2, int y2){
		
		this.xl = x1;
		this.xr = x2;
	}
	
	public BehaviorInfo() {
		
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

	public BehaviorEnum getBehaviorEnum() {
		return behaviorEnum;
	}

	public void setBehaviorEnum(BehaviorEnum behaviorEnum) {
		this.behaviorEnum = behaviorEnum;
	}

}
