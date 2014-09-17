package vamixUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
    
public class VamixMain extends JPanel {

	// Declares our media player component
	private EmbeddedMediaPlayerComponent mediaplayer;
	// This string holds the media URL path
	private static String mediapath;
	private JPanel panel;

	public VamixMain() {
	    mediapath = "/home/karen/Desktop/video.mpg";
	    JFrame frame = new JFrame();
	    
	    mediaplayer = new EmbeddedMediaPlayerComponent();
	    
	    frame.setContentPane(mediaplayer);
	    
	    frame.setLocation(100, 100);
	    frame.setSize(1050,600);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	    
	    mediaplayer.getMediaPlayer().playMedia(mediapath);
	}
	
	public void play() {
	    mediaplayer.getMediaPlayer().playMedia(mediapath);
	}
	
	public static void main(String[] args) {
		NativeLibrary.addSearchPath(
	                RuntimeUtil.getLibVlcLibraryName(), "/usr/lib"
	            );
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new VamixMain();
			}
			
		});
		
	
	}
}


