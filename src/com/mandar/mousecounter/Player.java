package com.mandar.mousecounter;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
	
	
    public static void main(final String[] args) {
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "MacOS/lib");
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Player(args);

                boolean test = mediaPlayer.playMedia("/Users/mulherka/Downloads/bailey.mpg");
                System.out.println(""+test);
            }
        });
        
    }

    private Player(String[] args) {

    	JFrame frame = new JFrame("MouseCounter");
    	JPanel playerPanel = new JPanel(new BorderLayout());
    	playerPanel.setBorder(BorderFactory.createTitledBorder("VLC Player"));
    	
    	mediaPlayerFactory = new MediaPlayerFactory();
    	
        Canvas canvas = new Canvas();
        canvas.setSize(640, 480);
        canvas.setBackground(Color.black);
        playerPanel.add(canvas);
        frame.getContentPane().add(playerPanel);

        FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(frame);

        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(fullScreenStrategy);
        

        mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));
        
        JPanel panel = new JPanel(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.EAST);
        JButton button = new JButton("Click me!");
        panel.add(button);
        
        JPanel panel1 = new JPanel(new BorderLayout());
        frame.getContentPane().add(panel1, BorderLayout.SOUTH);
        JButton button1 = new JButton("Click me!");
        panel1.add(button1);
        
        
        mediaPlayer.setPlaySubItems(true);
        
        frame.setLocation(100, 100);
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}