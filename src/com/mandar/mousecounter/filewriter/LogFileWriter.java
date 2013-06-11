package com.mandar.mousecounter.filewriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogFileWriter {

	private static String eventsFile = "logs.txt";
	private static BufferedWriter bufferedWriter;
	private static String log;
	
	public static void writeToFile(String logStatement){
		log = logStatement;
		writeEventToFile();
		
	}

	private static void writeEventToFile() {
		
		
		Thread fileWriterThread = new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					FileWriter fileWriter = new FileWriter(eventsFile, true);
					bufferedWriter = new BufferedWriter(fileWriter);
					if(bufferedWriter != null && log != null){
						bufferedWriter.write(log);
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
