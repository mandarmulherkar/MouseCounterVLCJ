package com.mandar.mousecounter;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public enum PlayerState {

	PLAYING,
	PAUSED,
	RECORDING;
	
	private static PlayerState ps;
	
	public static boolean isPlaying(EmbeddedMediaPlayer mediaPlayer) {
		if(ps == PlayerState.PLAYING){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isRecording(EmbeddedMediaPlayer mediaPlayer) {
		if(ps == PlayerState.RECORDING){
			return true;
		}else{
			return false;
		}
		
	}

	public static boolean isPaused(EmbeddedMediaPlayer mediaPlayer) {
		if(ps == PlayerState.PAUSED){
			return true;
		}else{
			return false;
		}
	}
	
	public static void setPlayerState(PlayerState pState){
		ps = pState;
	}
}
