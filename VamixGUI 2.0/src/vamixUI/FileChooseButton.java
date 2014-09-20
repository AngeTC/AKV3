package vamixUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;

/**
 * Button which opens a file chooser.
 * 
 * @author acas212
 */
@SuppressWarnings("serial")
public class FileChooseButton extends JButton { 
	
	/**
	 * Constructor of File Choose Button.
	 */
	public FileChooseButton(String name) {
		//Set text on button.
		setText(name);
		
		//Add new listener to button to create a new file chooser.
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				JFileChooser fileChooser = new JFileChooser();

				int returnValue = fileChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					
					//Play file selected from chooser.
					File selectedFile = fileChooser.getSelectedFile();
					VLCPlayerPane.getInstance().setMediaPath(selectedFile.getAbsolutePath());
					VLCPlayerPane.getInstance().play();
					
					//Set play button on VamixGUI as pressed.
					VamixGUI.getInstance().setPlay();
				}
			}
		});
	}
}

