package com.mandar.mousecounter;
/**
 * More info here 
 * http://svn.openstreetmap.org/applications/editors/josm/plugins/videomapping/src/org/openstreetmap/josm/plugins/videomapping/video/SimpleVideoPlayer.java?p=24624
 * 
 * VLC Command line (for audio and other uses) 
 * http://wiki.videolan.org/VLC_command-line_help
 * 
 * Interesting audio visualization
 * https://github.com/caprica/vlcj/blob/master/src/test/java/uk/co/caprica/vlcj/test/visualisation/VisualisationPlayer.java
 * 
 * Sample code
 * https://code.google.com/p/vlcj/wiki/SimpleExamples
 * https://code.google.com/p/vlcj/source/browse/trunk/vlcj/src/test/java/uk/co/caprica/vlcj/test/basic/PlayerControlsPanel.java
 * 
 * 
 */
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayer;
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
	private MenuClass menuClass;
	private JFrame mainFrame;
	private JButton btnPlaypauserecord;
	private EventsRecorder eventsRecorder = new EventsRecorder();
	private BehaviorEvent behaviorEvent;
	protected boolean mousePressedPlaying;
	private JSlider positionSlider;
	private String currentlyPlaying;
	private static File currentlyPlayingVideoFile = null;
	private float videoPlayingRate = 1.0f;
	private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	
	private JLabel timeLabel = new JLabel("  00:00:00  ");
	private JLabel lblStartTime = new JLabel("Start Time");
	private JLabel startTimeValue = new JLabel("");
	private JLabel lblStopTime = new JLabel("Stop Time");
	private JLabel stopTimeValue = new JLabel("");
	private JLabel lblTotalTime = new JLabel("Total Time");
	private JLabel totalBehaviorTime = new JLabel("");
	private JLabel lblBehavior = new JLabel("Behavior");
	private JLabel behaviorValue = new JLabel("");
	private JLabel lblValue = new JLabel("Value");
	private JLabel numberOfTimes = new JLabel("");

	private JSpinner spinner = new JSpinner();
	private BehaviorPanel behaviorPanel;
	
    public static void main(final String[] args) {
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "MacOS/lib");
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Player(args);
            }
        });
    }

	private Player(String[] args) {
		mainFrame = createMainFrame();
    	JPanel playerPanel = new JPanel(new BorderLayout());
    	Canvas canvas = createCanvas(mainFrame, playerPanel);
        createGUI(mainFrame, playerPanel);
        createMediaPlayer(mainFrame, canvas);
        if(null != mediaPlayer){
        	executorService.scheduleAtFixedRate(new updateRunnable(mediaPlayer), 0L, 1L, TimeUnit.SECONDS);
        }
	}

	public static File getCurrentlyPlayingVideoFile(){
		return currentlyPlayingVideoFile;
	}
	
	private final class updateRunnable implements Runnable {
		private final MediaPlayer mediaPlayer;
		private updateRunnable(MediaPlayer mediaPlayer){
			this.mediaPlayer = mediaPlayer;
		}

		@Override
		public void run() {
	
			final long time = mediaPlayer.getTime();		      
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					if(mediaPlayer.isPlaying()){
						updateTime(time);
						updatePosition();
					}
				}
			});
		}

	}
	
	protected void updatePosition() {
		final int position = (int)(mediaPlayer.getPosition() * 1000.0f);
		System.out.println("pos: "+position+" mediaplayer position"+mediaPlayer.getPosition());
		positionSlider.setValue(position);
	}

	protected void updateTime(long millis) {
		String formattedTime = String.format("  %02d:%02d:%02d  ",
			TimeUnit.MILLISECONDS.toHours(millis),
			TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), 
			TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
		);
		timeLabel.setText(formattedTime);
		
	}
	
	private void createMediaPlayer(JFrame frame, Canvas canvas) {
		// Set some options for libvlc
				String[] libvlcArgs = {"--vout=macosx",
		                "--no-plugins-cache",
		                "--no-video-title-show",
		                "--no-snapshot-preview",
		                "--quiet",
		                "--quiet-synchro",
		                "--intf",
		                "dummy"};
				//"--audio-visual=visual",
		// Create a factory instance (once), you can keep a reference to this
		//uncomment
		mediaPlayerFactory = new MediaPlayerFactory(libvlcArgs);
		FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(frame);
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(fullScreenStrategy);
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));

	}

	private Canvas createCanvas(JFrame frame, JPanel playerPanel) {
		playerPanel.setBorder(BorderFactory.createTitledBorder("VLC Player"));
    	Canvas canvas = new Canvas();
        canvas.setFont(new Font("Helvetica", Font.PLAIN, 12));
        canvas.setSize(640, 480);
        canvas.setBackground(Color.black);
        playerPanel.add(canvas);
        frame.getContentPane().add(playerPanel);
		return canvas;
	}

	private JFrame createMainFrame() {

		JFrame frame = new JFrame("MouseCounter");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Player.class.getResource("/icons/Rat.ico")));
    	frame.getContentPane().setFont(new Font("Helvetica", Font.PLAIN, 13));
    	frame.setFont(new Font("Helvetica", Font.PLAIN, 12));
    	
    	menuClass = new MenuClass(frame, this);
    	menuClass.buildMenu();
    	
		return frame;
	}

	private void createGUI(final JFrame frame, JPanel playerPanel) {
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
        gbc_panel_2.gridheight = 8;
        gbc_panel_2.gridwidth = 2;
        gbc_panel_2.insets = new Insets(0, 0, 5, 0);
        gbc_panel_2.fill = GridBagConstraints.BOTH;
        gbc_panel_2.gridx = 1;
        gbc_panel_2.gridy = 0;
        panel.add(activityButtonsPanel, gbc_panel_2);
        GridBagLayout gbl_activityButtonsPanel = new GridBagLayout();
        gbl_activityButtonsPanel.columnWidths = new int[]{0, 0, 0, 0, 0};
        gbl_activityButtonsPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_activityButtonsPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_activityButtonsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        activityButtonsPanel.setLayout(gbl_activityButtonsPanel);
        
        JButton lickButton = new JButton("Lick");
        lickButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if(null != behaviorEvent){
        			behaviorEvent.addBehavior(BehaviorEnum.LICK);
        			behaviorValue.setText(BehaviorEnum.LICK.toString());
        		}
        	}
        });
        lickButton.setIcon(new ImageIcon(Player.class.getResource("/icons/icecream.png")));
        GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
        gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton_1.gridx = 1;
        gbc_btnNewButton_1.gridy = 0;
        activityButtonsPanel.add(lickButton, gbc_btnNewButton_1);
        
        JButton flinchButton = new JButton("Flinch");
        flinchButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(null != behaviorEvent){
        			behaviorEvent.addBehavior(BehaviorEnum.FLINCH);
        			behaviorValue.setText(BehaviorEnum.FLINCH.toString());
        		}
        	}
        });
        flinchButton.setIcon(new ImageIcon(Player.class.getResource("/icons/flinch.png")));
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton.gridx = 2;
        gbc_btnNewButton.gridy = 0;
        activityButtonsPanel.add(flinchButton, gbc_btnNewButton);
        
        JButton scratchButton = new JButton("Scratch");
        scratchButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(null != behaviorEvent){
        			behaviorEvent.addBehavior(BehaviorEnum.SCRATCH);
        			behaviorValue.setText(BehaviorEnum.SCRATCH.toString());
        			
        		}
        	}
        });
        scratchButton.setIcon(new ImageIcon(Player.class.getResource("/icons/scratch.png")));
        GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
        gbc_btnNewButton_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton_2.gridx = 1;
        gbc_btnNewButton_2.gridy = 1;
        activityButtonsPanel.add(scratchButton, gbc_btnNewButton_2);
        
        JButton sniffButton = new JButton("Sniff");
        sniffButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(null != behaviorEvent){
        			behaviorEvent.addBehavior(BehaviorEnum.SNIFF);
        			behaviorValue.setText(BehaviorEnum.SNIFF.toString());
        		}
        	}
        });
        sniffButton.setIcon(new ImageIcon(Player.class.getResource("/icons/rose.png")));
        GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
        gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton_3.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton_3.gridx = 2;
        gbc_btnNewButton_3.gridy = 1;
        activityButtonsPanel.add(sniffButton, gbc_btnNewButton_3);
        
        JButton guardButton = new JButton("Guard");
        guardButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(null != behaviorEvent){
        			behaviorEvent.addBehavior(BehaviorEnum.GUARD);
        			behaviorValue.setText(BehaviorEnum.GUARD.toString());
        		}
        	}
        });
        guardButton.setIcon(new ImageIcon(Player.class.getResource("/icons/guard.png")));
        GridBagConstraints gbc_btnGuard = new GridBagConstraints();
        gbc_btnGuard.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnGuard.insets = new Insets(0, 0, 5, 5);
        gbc_btnGuard.gridx = 1;
        gbc_btnGuard.gridy = 2;
        activityButtonsPanel.add(guardButton, gbc_btnGuard);
        
        JButton liftButton = new JButton("Lift");
        liftButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(null != behaviorEvent){	
        			behaviorEvent.addBehavior(BehaviorEnum.LIFT);
        			behaviorValue.setText(BehaviorEnum.LIFT.toString());
        		}
        		
        	}
        });
        liftButton.setIcon(new ImageIcon(Player.class.getResource("/icons/lift.png")));
        GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
        gbc_btnNewButton_4.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton_4.gridx = 2;
        gbc_btnNewButton_4.gridy = 2;
        activityButtonsPanel.add(liftButton, gbc_btnNewButton_4);
        
        JButton wipeButton = new JButton("Wipe");
        wipeButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(null != behaviorEvent){
        			behaviorEvent.addBehavior(BehaviorEnum.WIPE);
        			behaviorValue.setText(BehaviorEnum.WIPE.toString());
        		}
        	}
        });
        wipeButton.setIcon(new ImageIcon(Player.class.getResource("/icons/wipe.png")));
        GridBagConstraints gbc_btnSomething = new GridBagConstraints();
        gbc_btnSomething.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnSomething.insets = new Insets(0, 0, 5, 5);
        gbc_btnSomething.gridx = 1;
        gbc_btnSomething.gridy = 3;
        activityButtonsPanel.add(wipeButton, gbc_btnSomething);
        
        JButton btnNewBehavior = new JButton("New");
        btnNewBehavior.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		String newBehavior = (String) JOptionPane.showInputDialog(
        				frame, 
        				"Add New Behavior:\n",
	                    "New Behavior",
	                    JOptionPane.PLAIN_MESSAGE,
	                    null,
	                    null,
	                    "NewBehavior");
        		//Read new behavior 
        		if(null != newBehavior){
        				behaviorEvent.addNewBehavior(newBehavior);
        				behaviorValue.setText(newBehavior);
        		}else{
        			if(behaviorEvent != null){
        				behaviorEvent.addNewBehavior("NoName");
    					behaviorValue.setText("NoName");
        			}
        		}
        	}
        });
        btnNewBehavior.setIcon(new ImageIcon(Player.class.getResource("/icons/new.png")));
        GridBagConstraints gbc_btnNewBehavior = new GridBagConstraints();
        gbc_btnNewBehavior.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewBehavior.insets = new Insets(0, 0, 5, 0);
        gbc_btnNewBehavior.gridwidth = 3;
        gbc_btnNewBehavior.gridx = 2;
        gbc_btnNewBehavior.gridy = 3;
        activityButtonsPanel.add(btnNewBehavior, gbc_btnNewBehavior);
        
        JLabel lblCount = new JLabel("Count");
        GridBagConstraints gbc_lblCount = new GridBagConstraints();
        gbc_lblCount.insets = new Insets(0, 0, 5, 5);
        gbc_lblCount.gridx = 1;
        gbc_lblCount.gridy = 4;
        activityButtonsPanel.add(lblCount, gbc_lblCount);
        
        GridBagConstraints gbc_spinner = new GridBagConstraints();
        gbc_spinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner.insets = new Insets(0, 0, 5, 5);
        gbc_spinner.gridx = 2;
        gbc_spinner.gridy = 4;
        spinner.setToolTipText("Type and Enter");
        activityButtonsPanel.add(spinner, gbc_spinner);
        
        spinner.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent arg0) {
				numberOfTimes.setText(spinner.getValue()+"");
				if(behaviorEvent != null){
					behaviorEvent.setTotalCount(Integer.parseInt(spinner.getValue()+""));
				}
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				numberOfTimes.setText(spinner.getValue()+"");
				if(behaviorEvent != null){
					behaviorEvent.setTotalCount(Integer.parseInt(spinner.getValue()+""));
				}
			}
        	
        });
        
        spinner.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("click");
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				System.out.println("enter");
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				System.out.println("exit");
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				System.out.println("press");
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        spinner.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				numberOfTimes.setText(spinner.getValue()+"");
				if(behaviorEvent != null){
					behaviorEvent.setTotalCount(Integer.parseInt(spinner.getValue()+""));
				}
			}
        });
        
        btnPlaypauserecord = new JButton("Play");
        btnPlaypauserecord.setIcon(new ImageIcon(Player.class.getResource("/icons/play.png")));
        btnPlaypauserecord.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		playPauseRecord();
        	}
        });
        
        GridBagConstraints gbc_btnPlaypauserecord = new GridBagConstraints();
        gbc_btnPlaypauserecord.insets = new Insets(0, 0, 5, 5);
        gbc_btnPlaypauserecord.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnPlaypauserecord.gridwidth = 2;
        gbc_btnPlaypauserecord.gridx = 1;
        gbc_btnPlaypauserecord.gridy = 5;
        activityButtonsPanel.add(btnPlaypauserecord, gbc_btnPlaypauserecord);
        
        JButton button = new JButton("");
        button.setIcon(new ImageIcon(Player.class.getResource("/icons/slow.png")));
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		// slow it down!
        		videoPlayingRate /= 2;
        		mediaPlayer.setRate((float) videoPlayingRate);
        	}
        });
        
        GridBagConstraints gbc_button = new GridBagConstraints();
        gbc_button.fill = GridBagConstraints.HORIZONTAL;
        gbc_button.insets = new Insets(0, 0, 5, 5);
        gbc_button.gridx = 1;
        gbc_button.gridy = 6;
        activityButtonsPanel.add(button, gbc_button);
        
        JButton btnNewButton = new JButton("");
        btnNewButton.setIcon(new ImageIcon(Player.class.getResource("/icons/fast.png")));
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// speed it up!
        		videoPlayingRate *= 2;
        		mediaPlayer.setRate((float) videoPlayingRate);
        	}
        });
        GridBagConstraints gbc_btnNewButton1 = new GridBagConstraints();
        gbc_btnNewButton1.fill = GridBagConstraints.BOTH;
        gbc_btnNewButton1.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton1.gridx = 2;
        gbc_btnNewButton1.gridy = 6;
        activityButtonsPanel.add(btnNewButton, gbc_btnNewButton1);
        
        JButton btnStop = new JButton("Pause");
        btnStop.setIcon(new ImageIcon(Player.class.getResource("/icons/pause.png")));
        GridBagConstraints gbc_btnStop = new GridBagConstraints();
        gbc_btnStop.fill = GridBagConstraints.BOTH;
        gbc_btnStop.insets = new Insets(0, 0, 5, 5);
        gbc_btnStop.gridx = 1;
        gbc_btnStop.gridy = 7;
        activityButtonsPanel.add(btnStop, gbc_btnStop);
        
        JButton btnHistory = new JButton("Last");
        btnHistory.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if(behaviorEvent != null){
        			eventsRecorder.removeLastEvent();
        			resetLabels();
        		}
        	}
        });
        btnHistory.setIcon(new ImageIcon(Player.class.getResource("/icons/cancel.png")));
        GridBagConstraints gbc_btnHistory = new GridBagConstraints();
        gbc_btnHistory.fill = GridBagConstraints.BOTH;
        gbc_btnHistory.insets = new Insets(0, 0, 5, 5);
        gbc_btnHistory.gridx = 2;
        gbc_btnHistory.gridy = 7;
        activityButtonsPanel.add(btnHistory, gbc_btnHistory);
        
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
        gbl_currentValuesPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
        gbl_currentValuesPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        gbl_currentValuesPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_currentValuesPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        currentValuesPanel.setLayout(gbl_currentValuesPanel);
        
        GridBagConstraints gbc_lblStartTime = new GridBagConstraints();
        gbc_lblStartTime.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblStartTime.insets = new Insets(0, 0, 5, 5);
        gbc_lblStartTime.gridx = 0;
        gbc_lblStartTime.gridy = 0;
        currentValuesPanel.add(lblStartTime, gbc_lblStartTime);

        GridBagConstraints gbc_startTimeValue = new GridBagConstraints();
        gbc_startTimeValue.anchor = GridBagConstraints.WEST;
        gbc_startTimeValue.gridwidth = 3;
        gbc_startTimeValue.insets = new Insets(0, 0, 5, 5);
        gbc_startTimeValue.gridx = 1;
        gbc_startTimeValue.gridy = 0;
        currentValuesPanel.add(startTimeValue, gbc_startTimeValue);
        
        GridBagConstraints gbc_lblStopTime = new GridBagConstraints();
        gbc_lblStopTime.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblStopTime.insets = new Insets(0, 0, 5, 5);
        gbc_lblStopTime.gridx = 0;
        gbc_lblStopTime.gridy = 1;
        currentValuesPanel.add(lblStopTime, gbc_lblStopTime);

        GridBagConstraints gbc_stopTimeValue = new GridBagConstraints();
        gbc_stopTimeValue.anchor = GridBagConstraints.WEST;
        gbc_stopTimeValue.gridwidth = 3;
        gbc_stopTimeValue.insets = new Insets(0, 0, 5, 5);
        gbc_stopTimeValue.gridx = 1;
        gbc_stopTimeValue.gridy = 1;
        currentValuesPanel.add(stopTimeValue, gbc_stopTimeValue);
        
        GridBagConstraints gbc_lblTotalTime = new GridBagConstraints();
        gbc_lblTotalTime.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblTotalTime.insets = new Insets(0, 0, 5, 5);
        gbc_lblTotalTime.gridx = 0;
        gbc_lblTotalTime.gridy = 2;
        currentValuesPanel.add(lblTotalTime, gbc_lblTotalTime);
        
        GridBagConstraints gbc_totalBehaviorTime = new GridBagConstraints();
        gbc_totalBehaviorTime.anchor = GridBagConstraints.WEST;
        gbc_totalBehaviorTime.gridwidth = 3;
        gbc_totalBehaviorTime.insets = new Insets(0, 0, 5, 5);
        gbc_totalBehaviorTime.gridx = 1;
        gbc_totalBehaviorTime.gridy = 2;
        currentValuesPanel.add(totalBehaviorTime, gbc_totalBehaviorTime);
        
        GridBagConstraints gbc_lblBehavior = new GridBagConstraints();
        gbc_lblBehavior.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblBehavior.insets = new Insets(0, 0, 5, 5);
        gbc_lblBehavior.gridx = 0;
        gbc_lblBehavior.gridy = 3;
        currentValuesPanel.add(lblBehavior, gbc_lblBehavior);
        
        GridBagConstraints gbc_behaviorValue = new GridBagConstraints();
        gbc_behaviorValue.anchor = GridBagConstraints.WEST;
        gbc_behaviorValue.gridwidth = 3;
        gbc_behaviorValue.insets = new Insets(0, 0, 5, 5);
        gbc_behaviorValue.gridx = 1;
        gbc_behaviorValue.gridy = 3;
        currentValuesPanel.add(behaviorValue, gbc_behaviorValue);
        
        GridBagConstraints gbc_lblValue = new GridBagConstraints();
        gbc_lblValue.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblValue.insets = new Insets(0, 0, 0, 5);
        gbc_lblValue.gridx = 0;
        gbc_lblValue.gridy = 4;
        currentValuesPanel.add(lblValue, gbc_lblValue);
        
        GridBagConstraints gbc_numberOfTimes = new GridBagConstraints();
        gbc_numberOfTimes.anchor = GridBagConstraints.WEST;
        gbc_numberOfTimes.gridwidth = 3;
        gbc_numberOfTimes.insets = new Insets(0, 0, 0, 5);
        gbc_numberOfTimes.gridx = 1;
        gbc_numberOfTimes.gridy = 4;
        currentValuesPanel.add(numberOfTimes, gbc_numberOfTimes);
        
        JPanel videoControlPanel = new JPanel();
        videoControlPanel.setBorder(BorderFactory.createTitledBorder("Video Controls"));
        frame.getContentPane().add(videoControlPanel, BorderLayout.SOUTH);
        videoControlPanel.setLayout(new BorderLayout(0, 0));
        
        JPanel panel_1 = new JPanel();
        videoControlPanel.add(panel_1, BorderLayout.NORTH);
        panel_1.setLayout(new BorderLayout(0, 0));
        
        positionSlider = new JSlider();
        panel_1.add(positionSlider, BorderLayout.CENTER);
        positionSlider.setMaximum(1000);
        positionSlider.setValue(0);
        panel_1.add(timeLabel, BorderLayout.WEST);
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        behaviorPanel = new BehaviorPanel();
        behaviorPanel.setToolTipText("Experimental!");
        panel_1.add(behaviorPanel, BorderLayout.NORTH);
        behaviorPanel.setLayout(new BorderLayout(0, 0));
        positionSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
              if(mediaPlayer.isPlaying()) {
                mousePressedPlaying = true;
                mediaPlayer.pause();
              }
              else {
                mousePressedPlaying = false;
              }
              setSliderBasedPosition();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
              setSliderBasedPosition();
              updateUIState();
            }
          });
        
//        Canvas behaviorCanvas = new Canvas();
//        behaviorPanel.add(behaviorCanvas, BorderLayout.CENTER);
//        behaviorCanvas.setBackground(Color.LIGHT_GRAY);
//        behaviorCanvas.setSize(640, 20);
//        
//        JSlider videoslider = new JSlider();
//        videoslider.setValue(0);
//        behaviorPanel.add(videoslider, BorderLayout.SOUTH);
        
        frame.setLocation(100, 100);
        frame.setSize(720, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}
	
	
	protected void updateUIState() {
		if(!mediaPlayer.isPlaying()){
			mediaPlayer.play();
			
			if(!mousePressedPlaying) {
                try {
                        // Half a second probably gets an iframe
                        Thread.sleep(500);  
                } 
                catch(InterruptedException e) {
                        // Don't care if unblocked early
                }
                mediaPlayer.pause();
        }
    }                       
		long time = mediaPlayer.getTime();
		int chapter = mediaPlayer.getChapter();
		int chapterCount = mediaPlayer.getChapterCount();
		System.out.println("chapter "+chapter +" "+chapterCount);
		updateTime(time);
		updatePosition();
			
	}

	protected void setSliderBasedPosition() {
		if(!mediaPlayer.isSeekable()){
			return;
		}else {
			float positionValue = (float)positionSlider.getValue() / 1000.0f;
			
			// Avoid eof freeze up
			if(positionValue > 0.99f) {
				positionValue = 0.99f;
			}
			mediaPlayer.setPosition(positionValue);
		}
		
	}

	private void playPauseRecord() {
		if(PlayerState.isPlaying(mediaPlayer)){
			System.out.println("isPlaying?");
			startRecording();
		}else if(PlayerState.isRecording(mediaPlayer)){
			System.out.println("isRecording?");
			pausePlaying();
		}else if(PlayerState.isPaused(mediaPlayer)){
			System.out.println("isPaused?");
			resumePlaying();
		}else{
			System.out.println("Error State");
		}
	}
	
	public void playSelectedVideoFile(File file){
		currentlyPlayingVideoFile = file;
		currentlyPlaying = file.getAbsolutePath();
		System.out.println("Playing");
		mediaPlayer.prepareMedia(currentlyPlaying, new String[] {});
		mediaPlayer.play();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.pause();
		PlayerState.setPlayerState(PlayerState.PAUSED);
	}
	
	private void startRecording() {
		PlayerState.setPlayerState(PlayerState.RECORDING);
		btnPlaypauserecord.setText("End Record");
		btnPlaypauserecord.setIcon(new ImageIcon(Player.class.getResource("/icons/pause.png")));
		System.out.println("Re"+mediaPlayer.getTime());

		behaviorEvent = new BehaviorEvent();
		behaviorEvent.setStartTime(mediaPlayer.getTime());
		startTimeValue.setText(mediaPlayer.getTime()+"");
		behaviorPanel.startVisualization(mediaPlayer.getPosition());
	}

	private void pausePlaying() {
		System.out.println("Pausing the Player");
		mediaPlayer.pause();
		PlayerState.setPlayerState(PlayerState.PAUSED);
		btnPlaypauserecord.setText("Play");
		btnPlaypauserecord.setIcon(new ImageIcon(Player.class.getResource("/icons/play.png")));
		System.out.println("Pa"+mediaPlayer.getTime());
		if(null != behaviorEvent){
			behaviorEvent.setEndTime(mediaPlayer.getTime());
			stopTimeValue.setText(mediaPlayer.getTime()+"");
			behaviorEvent.setDifference();
			totalBehaviorTime.setText(behaviorEvent.getTotalTime()+"");
			behaviorPanel.endVisualization(mediaPlayer.getPosition());
		}
	}

	private void resumePlaying() {
		PlayerState.setPlayerState(PlayerState.PLAYING);
		resetLabels();
		btnPlaypauserecord.setText("Start Record");
		btnPlaypauserecord.setIcon(new ImageIcon(Player.class.getResource("/icons/record.png")));
		System.out.println("Pl"+mediaPlayer.getTime());
		eventsRecorder.addEvent(behaviorEvent);
		mediaPlayer.play();
	}

	private void resetLabels() {
		
		startTimeValue.setText("");
		stopTimeValue.setText("");
		totalBehaviorTime.setText("");
		behaviorValue.setText("");
		numberOfTimes.setText("");
		spinner.setValue(0);
		
	}
}