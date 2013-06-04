package com.mandar.mousecounter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AnalysisFileWriter {

	private static String eventsFile = "results.csv";
	private static BehaviorEvent behaviorEventToWrite;
	private static BufferedWriter bufferedWriter;
	
	public static void writeToFile(BehaviorEvent behaviorEvent){
		behaviorEventToWrite = behaviorEvent;
		File file = Player.getCurrentlyPlayingVideoFile();
		
		if(null != file){
			writeEventToFile();
		}else{
			return;
		}
		
	}

	private static void writeEventToFile() {
		
		
		Thread fileWriterThread = new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					FileWriter fileWriter = new FileWriter(eventsFile);
					bufferedWriter = new BufferedWriter(fileWriter);
					if(bufferedWriter != null){
						bufferedWriter.write(behaviorEventToWrite.toString());
						bufferedWriter.newLine();
						bufferedWriter.close();
					}else {
						System.out.println("Buffered Writer NULL!");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
			}
			
		});
		
		fileWriterThread.start();

		
	}
}
