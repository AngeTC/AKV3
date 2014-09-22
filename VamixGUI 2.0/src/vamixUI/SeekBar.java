package vamixUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JProgressBar;

/**
 * Seek bar used to skip through media.
 * 
 * @author acas212
 *
 *Credits to: http://stackoverflow.com/questions/18146914/get-value-on-clicking-jprogressbar
 *(Detecting where on progress bar it is clicked.)
 */
@SuppressWarnings("serial")
public class SeekBar extends JProgressBar {

	public SeekBar(int mediaLength) {
		
		super(0, mediaLength);
		
		addMouseListener(new MouseAdapter() {            
		    public void mouseClicked(MouseEvent e) {

		       //Retrieves the mouse position relative to the bar's origin.
		       int mouseX = e.getX();

		       //Computes how far along the mouse is relative to the component width then multiply it by the progress bar's maximum value.
		       int seekTime = (int)Math.round(((double)mouseX / (double)getWidth()) * getMaximum());
		       int currentTime = VLCPlayerPane.getInstance().getTime().intValue();
		       
		       //Set bar value to the specified seek time.
		       setValue(seekTime);
		       
		       //Skip to the specified seek time.
		       VLCPlayerPane.getInstance().skip(seekTime - currentTime);
		  }                                     
		});
	};
	
	public void setNewTotalLength(int newMax) {
		setMaximum(newMax);
	}
}
