package com.mandar.mousecounter;
/**
 * More info here 
 * http://svn.openstreetmap.org/applications/editors/josm/plugins/videomapping/src/org/openstreetmap/josm/plugins/videomapping/video/SimpleVideoPlayer.java?p=24624
 */
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Button;

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

		// Set some options for libvlc
		String[] libvlcArgs = {"--vout=macosx",
                "--no-plugins-cache",
                "--no-video-title-show",
                "--no-snapshot-preview",
                "--quiet",
                "--quiet-synchro",
                "--audio-visual=visual",
                "--intf",
                "dummy"};
		 
		// Create a factory instance (once), you can keep a reference to this
		mediaPlayerFactory = new MediaPlayerFactory(libvlcArgs);
		   
		JFrame frame = new JFrame("MouseCounter");
    	frame.getContentPane().setFont(new Font("Helvetica", Font.PLAIN, 13));
    	frame.setFont(new Font("Helvetica", Font.PLAIN, 12));
    	JPanel playerPanel = new JPanel(new BorderLayout());
    	playerPanel.setBorder(BorderFactory.createTitledBorder("VLC Player"));
    	
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
        playerPanel.add(panel, BorderLayout.EAST);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{0, 0, 0, 0};
        gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);
        
        JPanel activityButtonsPanel = new JPanel();
        activityButtonsPanel.setBorder(BorderFactory.createTitledBorder("Behavior"));
        GridBagConstraints gbc_panel_2 = new GridBagConstraints();
        gbc_panel_2.gridheight = 7;
        gbc_panel_2.gridwidth = 3;
        gbc_panel_2.insets = new Insets(0, 0, 5, 0);
        gbc_panel_2.fill = GridBagConstraints.BOTH;
        gbc_panel_2.gridx = 0;
        gbc_panel_2.gridy = 1;
        panel.add(activityButtonsPanel, gbc_panel_2);
        GridBagLayout gbl_activityButtonsPanel = new GridBagLayout();
        gbl_activityButtonsPanel.columnWidths = new int[]{0, 0, 0, 0};
        gbl_activityButtonsPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
        gbl_activityButtonsPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_activityButtonsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        activityButtonsPanel.setLayout(gbl_activityButtonsPanel);
        
        JButton btnNewButton_1 = new JButton("Lick");
        btnNewButton_1.setIcon(new ImageIcon(Player.class.getResource("/icons/icecream.png")));
        GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
        gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton_1.gridx = 1;
        gbc_btnNewButton_1.gridy = 0;
        activityButtonsPanel.add(btnNewButton_1, gbc_btnNewButton_1);
        
        JButton btnNewButton = new JButton("Flinch");
        btnNewButton.setIcon(new ImageIcon(Player.class.getResource("/icons/flinch.png")));
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewButton.gridx = 2;
        gbc_btnNewButton.gridy = 0;
        activityButtonsPanel.add(btnNewButton, gbc_btnNewButton);
        
        JButton btnNewButton_2 = new JButton("Scratch");
        btnNewButton_2.setIcon(new ImageIcon(Player.class.getResource("/icons/scratch.png")));
        GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
        gbc_btnNewButton_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton_2.gridx = 1;
        gbc_btnNewButton_2.gridy = 1;
        activityButtonsPanel.add(btnNewButton_2, gbc_btnNewButton_2);
        
        JButton btnNewButton_3 = new JButton("Sniff");
        btnNewButton_3.setIcon(new ImageIcon(Player.class.getResource("/icons/rose.png")));
        GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
        gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewButton_3.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton_3.gridx = 2;
        gbc_btnNewButton_3.gridy = 1;
        activityButtonsPanel.add(btnNewButton_3, gbc_btnNewButton_3);
        
        JButton btnGuard = new JButton("Guard");
        btnGuard.setIcon(new ImageIcon(Player.class.getResource("/icons/guard.png")));
        GridBagConstraints gbc_btnGuard = new GridBagConstraints();
        gbc_btnGuard.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnGuard.insets = new Insets(0, 0, 5, 5);
        gbc_btnGuard.gridx = 1;
        gbc_btnGuard.gridy = 2;
        activityButtonsPanel.add(btnGuard, gbc_btnGuard);
        
        JButton btnNewButton_4 = new JButton("Lift");
        btnNewButton_4.setIcon(new ImageIcon(Player.class.getResource("/icons/lift.png")));
        GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
        gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewButton_4.gridx = 2;
        gbc_btnNewButton_4.gridy = 2;
        activityButtonsPanel.add(btnNewButton_4, gbc_btnNewButton_4);
        
        JButton btnSomething = new JButton("Wipe");
        btnSomething.setIcon(new ImageIcon(Player.class.getResource("/icons/wipe.png")));
        GridBagConstraints gbc_btnSomething = new GridBagConstraints();
        gbc_btnSomething.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnSomething.insets = new Insets(0, 0, 5, 5);
        gbc_btnSomething.gridx = 1;
        gbc_btnSomething.gridy = 3;
        activityButtonsPanel.add(btnSomething, gbc_btnSomething);
        
        JSpinner spinner = new JSpinner();
        GridBagConstraints gbc_spinner = new GridBagConstraints();
        gbc_spinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner.insets = new Insets(0, 0, 5, 0);
        gbc_spinner.gridx = 2;
        gbc_spinner.gridy = 3;
        activityButtonsPanel.add(spinner, gbc_spinner);
        
        JButton btnNewBehavior = new JButton("New Behavior");
        GridBagConstraints gbc_btnNewBehavior = new GridBagConstraints();
        gbc_btnNewBehavior.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewBehavior.gridwidth = 2;
        gbc_btnNewBehavior.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewBehavior.gridx = 1;
        gbc_btnNewBehavior.gridy = 4;
        activityButtonsPanel.add(btnNewBehavior, gbc_btnNewBehavior);
        
        JButton btnPlaypauserecord = new JButton("play/pause/record");
        btnPlaypauserecord.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if(mediaPlayer.isPlaying()){
        			mediaPlayer.pause();
        		}else{
        			mediaPlayer.play();
        		}
        	}
        });
        GridBagConstraints gbc_btnPlaypauserecord = new GridBagConstraints();
        gbc_btnPlaypauserecord.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnPlaypauserecord.gridwidth = 2;
        gbc_btnPlaypauserecord.gridx = 1;
        gbc_btnPlaypauserecord.gridy = 5;
        activityButtonsPanel.add(btnPlaypauserecord, gbc_btnPlaypauserecord);
        
        JLabel label = new JLabel("");
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.gridx = 0;
        gbc_label.gridy = 2;
        panel.add(label, gbc_label);
        
        JPanel currentValuesPanel = new JPanel();
        currentValuesPanel.setBorder(BorderFactory.createTitledBorder("Current Values"));
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.gridheight = 5;
        gbc_panel_1.gridwidth = 3;
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 8;
        panel.add(currentValuesPanel, gbc_panel_1);
        GridBagLayout gbl_currentValuesPanel = new GridBagLayout();
        gbl_currentValuesPanel.columnWidths = new int[]{0, 0, 0, 0, 0};
        gbl_currentValuesPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        gbl_currentValuesPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_currentValuesPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        currentValuesPanel.setLayout(gbl_currentValuesPanel);
        
        JLabel lblNewLabel_1 = new JLabel("Start Time");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 0;
        gbc_lblNewLabel_1.gridy = 0;
        currentValuesPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
        
        JLabel startTimeValue = new JLabel("0");
        GridBagConstraints gbc_startTimeValue = new GridBagConstraints();
        gbc_startTimeValue.insets = new Insets(0, 0, 5, 5);
        gbc_startTimeValue.gridx = 2;
        gbc_startTimeValue.gridy = 0;
        currentValuesPanel.add(startTimeValue, gbc_startTimeValue);
        
        JLabel lblNewLabel_2 = new JLabel("Stop Time");
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_2.gridx = 0;
        gbc_lblNewLabel_2.gridy = 1;
        currentValuesPanel.add(lblNewLabel_2, gbc_lblNewLabel_2);
        
        JLabel stopTimeValue = new JLabel("0");
        GridBagConstraints gbc_stopTimeValue = new GridBagConstraints();
        gbc_stopTimeValue.insets = new Insets(0, 0, 5, 5);
        gbc_stopTimeValue.gridx = 2;
        gbc_stopTimeValue.gridy = 1;
        currentValuesPanel.add(stopTimeValue, gbc_stopTimeValue);
        
        JLabel lblTotalTime = new JLabel("Total Time");
        GridBagConstraints gbc_lblTotalTime = new GridBagConstraints();
        gbc_lblTotalTime.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblTotalTime.insets = new Insets(0, 0, 5, 5);
        gbc_lblTotalTime.gridx = 0;
        gbc_lblTotalTime.gridy = 2;
        currentValuesPanel.add(lblTotalTime, gbc_lblTotalTime);
        
        JLabel totalBehaviorTime = new JLabel("0");
        GridBagConstraints gbc_totalBehaviorTime = new GridBagConstraints();
        gbc_totalBehaviorTime.insets = new Insets(0, 0, 5, 5);
        gbc_totalBehaviorTime.gridx = 2;
        gbc_totalBehaviorTime.gridy = 2;
        currentValuesPanel.add(totalBehaviorTime, gbc_totalBehaviorTime);
        
        JLabel lblBehavior = new JLabel("Behavior");
        GridBagConstraints gbc_lblBehavior = new GridBagConstraints();
        gbc_lblBehavior.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblBehavior.insets = new Insets(0, 0, 5, 5);
        gbc_lblBehavior.gridx = 0;
        gbc_lblBehavior.gridy = 3;
        currentValuesPanel.add(lblBehavior, gbc_lblBehavior);
        
        JLabel label_4 = new JLabel("0");
        GridBagConstraints gbc_label_4 = new GridBagConstraints();
        gbc_label_4.insets = new Insets(0, 0, 5, 5);
        gbc_label_4.gridx = 2;
        gbc_label_4.gridy = 3;
        currentValuesPanel.add(label_4, gbc_label_4);
        
        JLabel lblValue = new JLabel("Value");
        GridBagConstraints gbc_lblValue = new GridBagConstraints();
        gbc_lblValue.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblValue.insets = new Insets(0, 0, 0, 5);
        gbc_lblValue.gridx = 0;
        gbc_lblValue.gridy = 4;
        currentValuesPanel.add(lblValue, gbc_lblValue);
        
        JPanel videoControlPanel = new JPanel();
        videoControlPanel.setBorder(BorderFactory.createTitledBorder("Video Controls"));
        frame.getContentPane().add(videoControlPanel, BorderLayout.SOUTH);
        videoControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        JLabel lblTime = new JLabel("Time");
        videoControlPanel.add(lblTime);
        
        JSlider slider = new JSlider();
        videoControlPanel.add(slider);
        
        Button button = new Button("<< 2x");
        videoControlPanel.add(button);
        
        Button button_2 = new Button("||");
        videoControlPanel.add(button_2);
        
        Button button_1 = new Button("2x >>");
        videoControlPanel.add(button_1);
        
        
        mediaPlayer.setPlaySubItems(true);
        
        frame.setLocation(100, 100);
        frame.setSize(720, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}