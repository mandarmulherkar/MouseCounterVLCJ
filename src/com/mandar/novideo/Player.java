package com.mandar.novideo;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class Player {

	private static EmbeddedMediaPlayer mediaPlayer;
	
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

        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

        Canvas c = new Canvas();
        c.setSize(352, 240);
        c.setBackground(Color.black);
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(c, BorderLayout.CENTER);
        frame.add(p, BorderLayout.CENTER);


        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(c));
        frame.setLocation(100, 100);
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


    }
}