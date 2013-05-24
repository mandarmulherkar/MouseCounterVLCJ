package com.mandar.mousecounter;

import java.util.ArrayList;
import java.util.List;

public class EventsRecorder {

	private static List<BehaviorEvent> eventList = new ArrayList<BehaviorEvent>();

	public void addEvent(BehaviorEvent behaviorEvent) {
		
		eventList.add(behaviorEvent);
		for(BehaviorEvent event : eventList){
			System.out.print(" ("+event.toString()+") ");
		}
		
		
		
	}
	
	
}
