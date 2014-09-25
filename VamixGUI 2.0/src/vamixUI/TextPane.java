package vamixUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import say.swing.JFontChooser;

public class TextPane extends JPanel {
	// main sections
	private JPanel _textAndChangesPanel = new JPanel();
	private JPanel _addTextPanel = new JPanel();
	private JPanel _changesPanel = new JPanel();
	private JPanel _buttonPanel = new JPanel();
	
	//sub sections
	private final JPanel _textOptionPanel = new JPanel();
	private final JPanel _imageAndConfirmPanel = new JPanel();
	private final JPanel _confirmationPanel = new JPanel();
	private final JPanel _spinnerPanel1 = new JPanel();
	private final JPanel _spinnerPanel2 = new JPanel();
	private final JPanel _fontAndColourPanel = new JPanel();
	private final JPanel _tableButtonsPanel = new JPanel();
	private final JPanel _editAndDeletePanel = new JPanel();
	private final JPanel _playPanel = new JPanel();
	private final JPanel _inputVideoPanel = new JPanel();
	
	// button panel
	private final JButton _loadButton = new JButton("Load");
	private final JButton _saveButton = new JButton("Save project");
	private final JButton _exportButton = new JButton("Export");
	
	// Add text panel components
	
	// Text section
	private final JTextArea _textInput = new JTextArea("Type a caption to add...");
	private final JScrollPane _textScroll = new JScrollPane(_textInput);
	private final JButton _fontButton = new JButton("Font");
	private final JButton _colourButton = new JButton("Colour");
	private final JLabel _durationLabel1 = new JLabel("Start (hh:mm:ss)");
	private final JSpinner _hoursSpinner1 = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner _minsSpinner1 = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
	private final JSpinner _secsSpinner1 = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
	private final JLabel _timeSep1 = new JLabel(":");
	private final JLabel _timeSep2 = new JLabel(":");
	private final JLabel _durationLabel2 = new JLabel("End (hh:mm:ss)");
	private final JSpinner _hoursSpinner2 = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner _minsSpinner2 = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
	private final JSpinner _secsSpinner2 = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
	private final JLabel _timeSep3 = new JLabel(":");
	private final JLabel _timeSep4 = new JLabel(":");
	// Image section TODO: may remove
	private final JTextField _inputVideo = new JTextField("Pick a video to add text to");
	private final JButton _chooseVideoButton = new JButton("Choose");
	private final JLabel _outputVideoLabel = new JLabel("Output video");
	private final JTextField _outputVideo = new JTextField("Type in a file name");
	// Bottom
	private final JButton _previewTextButton = new JButton("Preview text");
	private final JButton _addButton = new JButton("Add");
	
	// JTable and associated buttons
	String[] fields = {"Text", "Start", "End", "Font", "Colour"};

	Vector<String> columns = new Vector<String>();
	
	DefaultTableModel _tableModel = new DefaultTableModel(fields, 0);
	private final JTable _captionsTable = new JTable(_tableModel);
	private final JScrollPane _tableScrollPane = new JScrollPane(_captionsTable);
	private final JButton _playButton = new JButton("Play video with captions");
	private final JButton _deleteButton = new JButton("Delete caption");
	
	private String _fontPath = "/usr/share/fonts/truetype/freefont/FreeSans.ttf";
	private String _colourHexValue = "0x000000";
	
	public TextPane() {
		setLayout(new BorderLayout());
		// button and video, NORTH
		JPanel videoAndButtons = new JPanel();
		videoAndButtons.setLayout(new GridLayout(2,0));
		_buttonPanel.setLayout(new GridLayout(1,0));
		_buttonPanel.add(_loadButton);
		_buttonPanel.add(_saveButton);
		_buttonPanel.add(_exportButton);
		_inputVideoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		_inputVideoPanel.add(_inputVideo);
		_inputVideoPanel.add(_chooseVideoButton);
		_inputVideo.setEditable(false);
		videoAndButtons.add(_buttonPanel);
		videoAndButtons.add(_inputVideoPanel);
		add(videoAndButtons, BorderLayout.NORTH);
		
		// add text panel and changes panel, CENTRE
		_textAndChangesPanel.setLayout(new GridLayout(2,0));
		//*****ADD TEXT PANEL******
		_addTextPanel.setLayout(new BorderLayout());
		// Text options
		_textOptionPanel.setLayout(new BorderLayout());
		_textOptionPanel.add(_textScroll, BorderLayout.CENTER);
		_fontAndColourPanel.setLayout(new GridLayout(0,1));
		JPanel fontAndSize = new JPanel();
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = e.getAllFonts();
		Vector<String> allFonts = new Vector<String>();
		for (Font f : fonts) {
			allFonts.add(f.getName());
		}
		_fontAndColourPanel.add(_fontButton);
		_fontAndColourPanel.add(_colourButton);
		_textOptionPanel.add(_fontAndColourPanel, BorderLayout.EAST);
		// spinner panels
		
		_spinnerPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
		_spinnerPanel1.add(_durationLabel1);
		_spinnerPanel1.add(_hoursSpinner1);
		_spinnerPanel1.add(_timeSep1);
		_spinnerPanel1.add(_minsSpinner1);
		_spinnerPanel1.add(_timeSep2);
		_spinnerPanel1.add(_secsSpinner1);
		
		_spinnerPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		_spinnerPanel2.add(_durationLabel2);
		_spinnerPanel2.add(_hoursSpinner2);
		_spinnerPanel2.add(_timeSep3);
		_spinnerPanel2.add(_minsSpinner2);
		_spinnerPanel2.add(_timeSep4);
		_spinnerPanel2.add(_secsSpinner2);
		
		JPanel spinnersPanel = new JPanel();
		spinnersPanel.setLayout(new GridLayout(2,0));
		spinnersPanel.add(_spinnerPanel1);
		spinnersPanel.add(_spinnerPanel2);
		
		_textOptionPanel.add(spinnersPanel, BorderLayout.SOUTH);
		
		TitledBorder addTextBorder = BorderFactory.createTitledBorder("Add text");
		_addTextPanel.setBorder(addTextBorder);
		
		_addTextPanel.add(_textOptionPanel, BorderLayout.CENTER);
		
		// image and confirm buttons
		_imageAndConfirmPanel.setLayout(new GridLayout(0,1));
		_confirmationPanel.setLayout(new GridLayout(1,0));
		_confirmationPanel.add(_previewTextButton);
		_confirmationPanel.add(_addButton);
		
		_imageAndConfirmPanel.add(_confirmationPanel);
		
		_addTextPanel.add(_imageAndConfirmPanel, BorderLayout.SOUTH);
		_textAndChangesPanel.add(_addTextPanel);
		
		//****CHANGES PANEL******
		TitledBorder changeBorder = new TitledBorder("Added captions");
		_changesPanel.setBorder(changeBorder);
		_changesPanel.setLayout(new BorderLayout());
		_changesPanel.add(_tableScrollPane, BorderLayout.CENTER);
		_captionsTable.getTableHeader().setReorderingAllowed(false);
		// add table buttons
		_tableButtonsPanel.setLayout(new GridLayout(1,0));

		_editAndDeletePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		_editAndDeletePanel.add(_deleteButton);
		_playPanel.setLayout(new GridLayout(1,1));
		_playPanel.add(_playButton);
		_tableButtonsPanel.add(_editAndDeletePanel);
		_tableButtonsPanel.add(_playPanel);
	
		_changesPanel.add(_tableButtonsPanel, BorderLayout.SOUTH);
		// add changes panel
		_textAndChangesPanel.add(_changesPanel);
		
		add(_textAndChangesPanel, BorderLayout.CENTER);
		_captionsTable.getColumnModel().getColumn(0).setPreferredWidth(130);
		_captionsTable.getColumnModel().getColumn(4).setPreferredWidth(60);
		_captionsTable.getColumnModel().getColumn(3).setPreferredWidth(60);
		
		//-------END OF LAYING OUT OF COMPONENTS-----------
		
		//---------START LISTENERS/FUNCTIONALITY-----------
		
		_addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addCaptionToTable();
				
			}
			
		});
		
		_deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_captionsTable.getSelectedRow() != -1) {
					_tableModel.removeRow(_captionsTable.getSelectedRow());
				}
				
			}
			
		});
		
		_fontButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFontChooser fc = new JFontChooser();
				int returnValue = fc.showDialog(null);
				if (returnValue == JFontChooser.OK_OPTION) {
					Font selected = fc.getSelectedFont();
					System.out.println(selected.getName() + ":" + selected.getStyle());
					
					FontHandler fh = new FontHandler();
					String path = FontHandler.getPathForFont(selected.getName(), 0);
					
					// set path if font found, else use default
					if (path != "") {
						_fontPath = path;
					}
					System.out.println(_fontPath);
				}
				
			}
			
		});
		
		_colourButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color newColour = JColorChooser.showDialog(
	                     _fontAndColourPanel,
	                     "Choose text color",
	                     Color.BLACK);
				// convert colour to hex to be used in avconv
				if (newColour != null) {
					String hex = Integer.toHexString(newColour.getRGB());
					_colourHexValue = "0x" + hex.substring(2);
					System.out.println(_colourHexValue);
				}
				
			}
			
		});
		
		
		_playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_tableModel.getRowCount() < 1) {
					JOptionPane.showMessageDialog(null, "No captions added yet.");
					return;
				}
				for (int i = 0; i < _tableModel.getRowCount(); i++) {
					
					VideoTextHandler handler = new VideoTextHandler("g.mpg","sdfsdfsdf","1","5", _fontPath , _colourHexValue,"g.mp4", true);
				}
				
			}
			
		});
		
		_exportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String outputFile = JOptionPane.showInputDialog(null, "Enter output file name:", "Dialog for Input",
				        JOptionPane.WARNING_MESSAGE);
				if (outputFile != null) {
					String wd = System.getProperty("user.dir");
					String path = wd + outputFile + ".mp4";
					File outFile = new File(outputFile);
					if (outFile.exists()) {
						int selectedOption = JOptionPane.showConfirmDialog(null, 
                                "File exists. Overwrite?", 
                                "Choose", 
                                JOptionPane.YES_NO_OPTION); 
						if (selectedOption == JOptionPane.YES_OPTION) {
							// do command
							System.out.println("yes");
						} else {
							System.out.println("no");
						}
					}
				}
				
				
				
			}
			
		});
		
		_chooseVideoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.addChoosableFileFilter(new FileNameExtensionFilter("Videos", "mpg", "mp4", "mkv", "wmv", "rm", "swf"));
				int returnVal = fc.showOpenDialog(_inputVideoPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					_inputVideo.setText(fc.getSelectedFile().toString());
				}
			}
			
		});
		
	}

	/**
	 * Returns string representation of duration time
	 * @return
	 */
	private String getTimeAsString(int spinnerNumber) {
		String hours = "";
		String mins = "";
		String secs = ""; 
		if (spinnerNumber == 1) {
			hours = _hoursSpinner1.getValue().toString();
			mins = _minsSpinner1.getValue().toString();
			secs = _secsSpinner1.getValue().toString();
		} else if (spinnerNumber == 2) {
			hours = _hoursSpinner2.getValue().toString();
			mins = _minsSpinner2.getValue().toString();
			secs = _secsSpinner2.getValue().toString();
		}
		String time = hours + ":" + mins + ":" + secs;
		
		return time;
	}
	
	private String getTimeInSeconds(int spinnerNumber) {
		int hours = 0;
		int mins = 0;
		int secs = 0;
		if (spinnerNumber == 1) {
			hours = Integer.parseInt( _hoursSpinner1.getValue().toString()) * 360;
			mins = Integer.parseInt( _minsSpinner1.getValue().toString()) * 60;
			secs = Integer.parseInt( _secsSpinner1.getValue().toString());
		} else if (spinnerNumber == 2) {
			hours = Integer.parseInt( _hoursSpinner2.getValue().toString()) * 360;
			mins = Integer.parseInt( _minsSpinner2.getValue().toString()) * 60;
			secs = Integer.parseInt( _secsSpinner2.getValue().toString());
		}
		
		String time = Integer.toString(hours + mins + secs);
		return time;
	}
	
	private void addCaptionToTable() {
		if (_textInput.getText().length() <= 0) {
			JOptionPane.showMessageDialog(null, "Please enter some text.");
		} else if (getTimeAsString(1).equals("0:0:0") && getTimeAsString(2).equals("0:0:0")) { // TODO check start and end times
			JOptionPane.showMessageDialog(null, "Please enter a duration greater than 0.");
		} else if (Integer.parseInt(getTimeInSeconds(2)) <= Integer.parseInt(getTimeInSeconds(1))) {
			JOptionPane.showMessageDialog(null, "End time must be greater than start time.");
		}
		
		String[] captionData = {_textInput.getText(), getTimeAsString(1), getTimeAsString(2), "Font", "Yellow"};
		_tableModel.addRow(captionData);
		
		
	}
	
}
