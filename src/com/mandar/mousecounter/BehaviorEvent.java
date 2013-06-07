package com.mandar.mousecounter;

public class BehaviorEvent {

	private long startTime;
	private long endTime;
	private long totalTime;
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
		return startTime +", "+ endTime +", "+ behavior +", "+totalTime+ ", "+behaviorCount;
		
	}

	public void addBehavior(BehaviorEnum action) {
		this.behavior = action;
		
	}

	public void addNewBehavior(String newBehavior) {
		System.out.println(newBehavior);
		this.newBehavior = newBehavior;
		this.newBehaviorFlag = true;
		
	}

	public void setTotalCount(int value) {
		this.behaviorCount = value;
		
	}

	public void setDifference() {
		this.totalTime = endTime - startTime;
	}

	public long getTotalTime() {
		return totalTime;
	}
	
}
