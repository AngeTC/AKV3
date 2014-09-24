package vamixUI;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JPanel;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Panel which contains the Media Player. 
 * (A singleton class.)
 */
@SuppressWarnings("serial")
public class VLCPlayerPane extends JPanel {

	private static VLCPlayerPane _instance = null;
	private static EmbeddedMediaPlayerComponent _eMPC;
	private String _mediaPath = "";
	private int _totalPlayTime = 0;
	
	/**
	 * Private constructor for VLCPlayerPane.
	 */
	private VLCPlayerPane() {
		_eMPC = new EmbeddedMediaPlayerComponent();
		_eMPC.getMediaPlayer().setVolume(75);
		
		//VLC Player Library stuff:
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/usr/lib");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
	    //Create the canvas.
	    Canvas canvas = new Canvas();
	    canvas.setBackground(Color.black);
	    canvas.setVisible(true);

	    //Set the layout of panel.
	    setLayout(new BorderLayout());

	    //Add the canvas and player component.
	    add(canvas, BorderLayout.CENTER);
	    add(_eMPC);
	    setVisible(true);
	  
	};
	
	/**
	 * Method to get single instance of VLCPlayerPane.
	 * @return
	 */
	public static VLCPlayerPane getInstance() {
		if (_instance == null) {
			_instance = new VLCPlayerPane(); 
		}
		return _instance;
	}
	
	/**
	 * Set new path to media for playing.
	 * @param absolutePath
	 */
	public void setMediaPath(String absolutePath) {
		_mediaPath = absolutePath;
	}
	
	/**
	 * Get path to media for playing.
	 */
	public String getMediaPath() {
		return _mediaPath;
	}
	
	/**
	 * Sets total play time of the currently
	 * playing file.
	 * 
	 * @param totalPlayTime
	 */
	public void setPlayTime(int totalPlayTime) {
		_totalPlayTime = totalPlayTime;
		
	}
	
	/**
	 * Return total play time of the currently
	 * playing file.
	 * 
	 * @return totalPlayTime (milliseconds)
	 */
	public int getPlayTime() {
		return _totalPlayTime;
		
	}
	
	/*
	 * The below methods are for interacting with the media player.
	 */
	
	/**
	 * Check if media player is currently playing
	 * something.
	 * @return
	 */
	public boolean isPlaying() {
		return _eMPC.getMediaPlayer().isPlaying();
	}
	
	/**
	 * Start playing media with the given media path.
	 */
	public void play() {
		//Ensure that the player isn't muted.
		_eMPC.getMediaPlayer().mute(false);
		
		//Play from the given mediaPath.
	    _eMPC.getMediaPlayer().playMedia(_mediaPath);
	}
	
	/**
	 * Toggle pause of media player.
	 */
	public void pause() {
	    _eMPC.getMediaPlayer().pause();
	}

	/**
	 * Stop the media player.
	 */
	public void stop() {
		_eMPC.getMediaPlayer().stop();
	}
	
	/**
	 * Add a new listener to the media player.
	 * 
	 * @param eL
	 */
	public void addMediaEventHandler(MediaPlayerEventListener eL) {
		_eMPC.getMediaPlayer().addMediaPlayerEventListener(eL);
	}

	/**
	 * Skip media player by the 
	 * specified amount of milliseconds
	 * 
	 * @param time (milliseconds)
	 */
	public void skip(int time) {
		_eMPC.getMediaPlayer().skip(time);
	}

	/**
	 * Mute sound of player.
	 */
	public void mute() {
		_eMPC.getMediaPlayer().mute();
	}

	/**
	 * Set volume of media player to given value.
	 * @param value
	 */
	public void setVolume(int value) {
		_eMPC.getMediaPlayer().setVolume(value);
	}

	/**
	 * Get current time of currently playing 
	 * media in player.
	 * @return
	 */
	public Long getTime() {
		return _eMPC.getMediaPlayer().getTime();
	}

	/**
	 * Get total time length of the media.
	 * @return
	 */
	public int getLength() {
		return (int) _eMPC.getMediaPlayer().getLength();
		
	}
}
