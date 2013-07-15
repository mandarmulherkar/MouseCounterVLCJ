package com.mandar.mousecounter.filewriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.mandar.mousecounter.behaviorevent.BehaviorEvent;
import com.mandar.mousecounter.Player;

public class AnalysisFileWriter {

	private static String eventsFile;
	private static BehaviorEvent behaviorEventToWrite;
	private static BufferedWriter bufferedWriter;
	private static File saveToFile;
	public static void writeToFile(BehaviorEvent behaviorEvent){
		behaviorEventToWrite = behaviorEvent;

		File file = Player.getCurrentlyPlayingVideoFile();
		saveToFile = Player.getCurrentSaveFile();
		
		if(null != file){
			
			if(saveToFile != null){
				eventsFile = saveToFile.getName()+".csv";
			} else{
				eventsFile = file.getAbsolutePath()+".csv";
			}
			
			System.out.println("Saving to File "+eventsFile);
			
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
					FileWriter fileWriter = new FileWriter(eventsFile, true);
					bufferedWriter = new BufferedWriter(fileWriter);
					if(bufferedWriter != null && behaviorEventToWrite != null){
						bufferedWriter.write(behaviorEventToWrite.toString());
						bufferedWriter.newLine();
						bufferedWriter.close();
						fileWriter.close();
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
