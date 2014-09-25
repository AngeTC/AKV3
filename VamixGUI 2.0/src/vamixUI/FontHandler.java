package vamixUI;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

// find usr/share/fonts \( -name "*Free*" -a -name "*BoldItalic*" \) -o \( -name "*Free*" -a -name "*BoldOblique*" \) -print

public class FontHandler {
	public static String getPathForFont(String fontName, int styleCode) {
		BashCommand cmd = new BashCommand();
		String command = "find /usr/share/fonts ";
		
		command = command.concat("-name " + "\"*" + fontName + "*\"" + " -print");
		String[] output = cmd.runBash(command);
		
		// return if font is not found
		if (output == null || output.length < 1) {
			return "";
		}
		
		String path = "";
		// select correct path for styled font
		if (styleCode == 1) {
			for (String s : output) {
				if (s.contains("Bold") && !(s.contains("Italic") || s.contains("Oblique"))) {
					path = s;
				} 
			}
		} else if (styleCode == 2) {
			for (String s : output) {
				if (s.contains("Italic") || s.contains("Oblique") && !(s.contains("Bold"))) {
					path = s;
				} 
			}
		} else if (styleCode == 3) {
			for (String s : output) {
				if (s.contains("BoldItalic") || s.contains("BoldOblique")) {
					path = s;
				} 
			}
		} else {
			path = output[0];
		}
		
		return path;
	}
	
}
