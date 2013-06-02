package com.mandar.mousecounter;

public class BehaviorEvent {

	private long startTime;
	private long endTime;
	private BehaviorEnum behavior;
	private int behaviorCount;
	private String newBehavior;
	private String newBehaviorCount;
	private boolean newBehaviorFlag;
	
	public void setStartTime(long time) {
		startTime = time;
		
	}

	public void setEndTime(long time) {
		endTime = time;
		
	}
	
	@Override
	public String toString(){
		return startTime +", "+ endTime +" "+ behavior ;
		
	}

	public void addBehavior(BehaviorEnum action) {
		this.behavior = action;
		
	}

	public void addNewBehavior(String newBehavior, String newBehaviorCount) {
		
		System.out.println(newBehavior);
		System.out.println(newBehaviorCount);
		
		this.newBehavior = newBehavior;
		this.newBehaviorCount = newBehaviorCount;
		this.newBehaviorFlag = true;
		
	}
	
}
