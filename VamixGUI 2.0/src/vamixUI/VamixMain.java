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
	private VLCPlayer mediaplayer = VLCPlayer.getInstance();
	// This string holds the media URL path
	private static String mediapath = "/home/angel/Videos/Fullmetal Alchemist Brotherhood - 01.mp4";
	private JPanel panel;

	public VamixMain() {
	    JFrame frame = new JFrame();
    
	    frame.setContentPane(mediaplayer);
	    
	    frame.setLocation(100, 100);
	    frame.setSize(1050,600);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	    VLCPlayer.getInstance().play();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				VamixMain frame = new VamixMain();
				frame.setVisible(true);
			}
		});
	}
}