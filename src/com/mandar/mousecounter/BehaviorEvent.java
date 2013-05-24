package com.mandar.mousecounter;

public class BehaviorEvent {

	private long startTime;
	private long endTime;
	
	public void setStartTime(long time) {
		startTime = time;
		
	}

	public void setEndTime(long time) {
		endTime = time;
		
	}
	
	@Override
	public String toString(){
		return startTime +", "+ endTime;
		
	}
	
}
