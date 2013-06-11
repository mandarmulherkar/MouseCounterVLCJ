package com.mandar.mousecounter.visualization;

import com.mandar.mousecounter.behaviorevent.BehaviorEnum;

public class BehaviorInfo {

	private float xl;
	private float xr;
	
	private BehaviorEnum behaviorEnum;
	
	public BehaviorInfo(float x1, int y1, float x2, int y2){
		
		this.xl = x1;
		this.xr = x2;
	}
	
	public BehaviorInfo() {
		
	}

	public void setX1(float x){
		this.xl = x;
	}
	
	public void setX2(float x){
		this.xr = x;
	}
	
	
	public float getX1(){
		return xl;
	}
	
	public float getX2(){
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
