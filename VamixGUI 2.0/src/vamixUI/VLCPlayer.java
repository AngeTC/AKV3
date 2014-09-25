package vamixUI;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.io.File;

import javax.swing.JPanel;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class VLCPlayer extends JPanel {

	private static VLCPlayer _instance = null;
	private static EmbeddedMediaPlayerComponent _eMPC;
	private String _mediaPath = "";
	private String _subtitlePath = "";
	
	private VLCPlayer() {
		_eMPC = new EmbeddedMediaPlayerComponent();
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/usr/lib");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
	    /* Set the canvas */
	    Canvas canvas = new Canvas();
	    canvas.setBackground(Color.black);
	    canvas.setVisible(true);

	    /* Set the layout */
	    this.setLayout(new BorderLayout());

	    /* Add the canvas */
	    this.add(canvas, BorderLayout.CENTER);
	    this.setVisible(true);
	    this.add(_eMPC);
	};
	
	public static VLCPlayer getInstance() {
		if (_instance == null) {
			_instance = new VLCPlayer(); 
		}
		return _instance;
	}
	
	public boolean isPlaying() {
		return _eMPC.getMediaPlayer().isPlaying();
	}
	
	// added subtitle compatibility
	public void play() {
		_subtitlePath = "/home/karen/Desktop/blah.ass";
	    _eMPC.getMediaPlayer().playMedia(_mediaPath, "sub-file=" + _subtitlePath);
	}
	
	public void pause() {
	    _eMPC.getMediaPlayer().pause();
	}

	public void stop() {
		_eMPC.getMediaPlayer().stop();
	}
	
	public void addMediaEventHandler(MediaPlayerEventListener eL) {
		_eMPC.getMediaPlayer().addMediaPlayerEventListener(eL);
	}

	public void skipForward() {
		_eMPC.getMediaPlayer().skip(5000);
	}

	public void skipBackward() {
		_eMPC.getMediaPlayer().skip(-5000);
	}

	public void mute() {
		_eMPC.getMediaPlayer().mute();
	}

	public void setVolume(int value) {
		_eMPC.getMediaPlayer().setVolume(value);
	}

	public void setMediaPath(String absolutePath) {
		_mediaPath = absolutePath;
	}

	public Long getTime() {
		return _eMPC.getMediaPlayer().getTime();
	}
	
	// add subtitles
	public void setSubtitle(File file) {
		_subtitlePath = file.getAbsolutePath();
	}
		
}
