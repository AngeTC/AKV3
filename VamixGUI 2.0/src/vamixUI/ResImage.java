package vamixUI;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * Class which encapsulates an image to be 
 * returned as an Image Class.
 * @author acas212
 *
 */
public class ResImage {
	
	private Image _img;

	/**
	 * Constructor for ResImage.
	 * @param imageName
	 */
	public ResImage(String imageName) {
		try {
			URL imageURL = getClass().getResource("/" + imageName);
			_img = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Return image obtained.
	 * @return
	 */
	public Image getResImage() {
		return _img;
	}
	
}
