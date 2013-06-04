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
}
