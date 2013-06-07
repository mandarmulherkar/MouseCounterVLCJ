package com.mandar.mousecounter;

import java.util.ArrayList;
import java.util.List;

public class EventsRecorder {

	private static List<BehaviorEvent> eventList = new ArrayList<BehaviorEvent>();

	public void addEvent(final BehaviorEvent behaviorEvent) {
		AnalysisFileWriter.writeToFile(behaviorEvent);		
		eventList.add(behaviorEvent);
		for(BehaviorEvent event : eventList){
			if(event != null){
				System.out.print(" ("+event.toString()+") ");
			}
		}
		
	}

	public void removeLastEvent() {
		if(eventList != null && eventList.size() > 0){
			eventList.remove(eventList.size() -1);
		}
	}
}
