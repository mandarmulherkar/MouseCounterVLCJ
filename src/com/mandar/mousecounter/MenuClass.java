package com.mandar.mousecounter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MenuClass {

	private static JFrame frame;
	private static Player player;
	private File currentFilename;
	
	public MenuClass(JFrame frame, Player player){
		this.frame = frame;
		this.player = player;
	}


	public void buildMenu() {
		
    	JMenuBar menuBar = new JMenuBar();
    	frame.setJMenuBar(menuBar);
    	
    	JMenu menuFile = new JMenu("File");
    	menuBar.add(menuFile);
    	
    	JMenuItem menuItemOpen = new JMenuItem("Open");
    	menuFile.add(menuItemOpen);
    	menuItemOpen.addActionListener(new ActionListener(){
    		@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
    	});
	}

	protected void openFile() {
		JFileChooser fileChooser = new JFileChooser();
		int retVal = fileChooser.showOpenDialog(frame);
		if(retVal == JFileChooser.APPROVE_OPTION){
			currentFilename = fileChooser.getSelectedFile();
			System.out.println(""+currentFilename.getAbsolutePath());
			player.playSelectedVideoFile(currentFilename);
		}else{
			System.out.println("File open cancelled.");
		}
	}
}
