package com.mandar.mousecounter;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class Player {

	private static EmbeddedMediaPlayer mediaPlayer = null;
	private MediaPlayerFactory mediaPlayerFactory = null;
	private static Label statusLabel = new Label("Status");
    
	
    public static void main(final String[] args) {
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "MacOS/lib");
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Player(args);

                boolean test = mediaPlayer.playMedia("/Users/mulherka/Downloads/bailey.mpg");
                System.out.println(""+test);
                setLabel();
            }
        });
        
    }

    protected static void setLabel() {
    	
    	Thread thread = new Thread(new Runnable(){

    		
    		
    		
			@Override
			public void run() {
				
			
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while(mediaPlayer.isPlaying())
		        {
		        	long millis=mediaPlayer.getTime();
		        	String s = String.format("%02d:%02d:%02d", //dont know why normal Java date utils doesn't format the time right
				      TimeUnit.MILLISECONDS.toHours(millis),
				      TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), 
				      TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
				    );
		            //setTitle(ms.format(new Time(sec)));
		        	statusLabel.setText(s);
//		            syncTimeline=true;
//		            timeline.setValue(Math.round(mp.getPosition()*100));
//		            syncTimeline=false;
//		            notifyObservers(mp.getTime());
		        }
				
				
				
				
			}
    	});
    	
    	thread.start();
		
	}

	private Player(String[] args) {

    	JFrame frame = new JFrame("MouseCounter");
    	frame.getContentPane().setFont(new Font("Helvetica", Font.PLAIN, 13));
    	frame.setFont(new Font("Helvetica", Font.PLAIN, 12));
    	JPanel playerPanel = new JPanel(new BorderLayout());
    	playerPanel.setBorder(BorderFactory.createTitledBorder("VLC Player"));
    	
    	mediaPlayerFactory = new MediaPlayerFactory();
    	
        Canvas canvas = new Canvas();
        canvas.setFont(new Font("Helvetica", Font.PLAIN, 12));
        canvas.setSize(640, 480);
        canvas.setBackground(Color.black);
        playerPanel.add(canvas);
        frame.getContentPane().add(playerPanel);

        FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(frame);

        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(fullScreenStrategy);
        

        mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));
        
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.EAST);
        JButton button = new JButton("click me!");
        button.setFont(new Font("Helvetica", Font.PLAIN, 13));
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if(mediaPlayer.canPause()){
        			mediaPlayer.pause();
        		}else{
        			mediaPlayer.play();
        		}
        	}
        });
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        statusLabel.setFont(new Font("Helvetica", Font.PLAIN, 12));
        panel.add(statusLabel);
        panel.add(button);
        
        JPanel panel1 = new JPanel();
        frame.getContentPane().add(panel1, BorderLayout.SOUTH);
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        JSlider slider = new JSlider();
        panel1.add(slider);
        
        
        mediaPlayer.setPlaySubItems(true);
        
        frame.setLocation(100, 100);
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}