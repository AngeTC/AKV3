package vamixUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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
	private final JButton _loadButton = new JButton("Load data");
	private final JButton _saveButton = new JButton("Save data");
	
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
	
	private final JTextField _inputVideo = new JTextField("Pick a video to add text to");
	private final JButton _chooseVideoButton = new JButton("Choose");
	private final JLabel _outputVideoLabel = new JLabel("Output video");
	private final JTextField _outputVideo = new JTextField("Type in a file name");
	// Bottom
	private final JButton _previewTextButton = new JButton("Preview Text");
	private final JButton _addButton = new JButton("Add");
	
	private final static JProgressBar progressBar = new JProgressBar();

	
	// JTable and associated buttons
	String[] fields = {"Text", "Start", "End", "Font", "Colour"};

	Vector<String> columns = new Vector<String>();
	
	DefaultTableModel _tableModel = new DefaultTableModel(fields, 0) {
		@Override
		public boolean isCellEditable(int row, int column) {
	        return false;
	    }
	};
	private final JTable _captionsTable = new JTable(_tableModel);
	private final JScrollPane _tableScrollPane = new JScrollPane(_captionsTable);
	private final JButton _exportingButton = new JButton("Export");
	private final JButton _deleteButton = new JButton("Delete Caption");
	
	private String _fontPath = "/usr/share/fonts/truetype/freefont/FreeSans.ttf";
	private String _colourHexValue = "0x000000";
	
	boolean canDraw;
	
	public TextPane() {
		setLayout(new BorderLayout());
		// button and video, NORTH
		JPanel videoAndButtons = new JPanel();
		videoAndButtons.setLayout(new GridLayout(2,0));
		_buttonPanel.setLayout(new GridLayout(1,0));
		_buttonPanel.add(_loadButton);
		_buttonPanel.add(_saveButton);
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
		_playPanel.add(_exportingButton);
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
		
		
		_exportingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (_tableModel.getRowCount() < 1) {
					JOptionPane.showMessageDialog(null, "No captions added yet.");
					return;
				} else if (_inputVideo.getText().equals("Pick a video to add text to")) {
					JOptionPane.showMessageDialog(null, "Please choose a valid video file");
					return;
				} else if (_captionsTable.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Please select caption from text log.");
					return;
				} 
				
				String outputFile = JOptionPane.showInputDialog(null, "Enter output file name:", "Dialog for Input",
				        JOptionPane.WARNING_MESSAGE);
				if (outputFile != null) {
					String wd = System.getProperty("user.dir");
					String path = wd + "/" + outputFile + ".mp4";
					
					String[] outputNameExists = new BashCommand().runBash("if [ ! -f " + path + " ]; then echo 0; else echo 1; fi");
					
					if (outputNameExists[0].equals("1")) {
						int selectedOption = JOptionPane.showConfirmDialog(null, 
                                "File exists. Overwrite?", 
                                "Choose", 
                                JOptionPane.YES_NO_OPTION); 
						if (selectedOption == JOptionPane.YES_OPTION) {
							new BashCommand().runBash("rm " + path);

							canDraw = true;
							//System.out.println("yes");
						} else {
							canDraw = false;
							//System.out.println("no");
						}
					} else {
						canDraw = true;
					}
				}
				
				if (canDraw && _captionsTable.getSelectedRow() != -1) {
					int row = _captionsTable.getSelectedRow();
					String stringCommand = VideoTextHandler.makeCommand(_inputVideo.getText(), _tableModel.getValueAt(row, 0).toString(), getTimeInSeconds(_tableModel.getValueAt(row, 1).toString()), getTimeInSeconds(_tableModel.getValueAt(row, 2).toString()),
							_fontPath, _colourHexValue, outputFile + ".mp4");
						new VideoTextHandler(stringCommand);
						
						//Create Jdialog with progress bar.
						final JComponent[] components = new JComponent[] {
								progressBar
						};
						progressBar.setIndeterminate(true);
						progressBar.setString("Processing");
						progressBar.setStringPainted(true);
						JOptionPane.showMessageDialog(null, components, "Progress", JOptionPane.PLAIN_MESSAGE);
				}
					
					
				
			}
			
		});
		
		/*_exportButton.addActionListener(new ActionListener() {

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
			
		});*/
		
		_chooseVideoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// check if chosen file is a video
					String type;
					try {
						type = Files.probeContentType(fc.getSelectedFile().toPath());
						if (type.contains("video")) {
							_inputVideo.setText(fc.getSelectedFile().toString());
						} else {
							JOptionPane.showMessageDialog(null, "Invalid file chosen.");
							return;
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}
			}
			
		});
		
		_loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String type;
					try {
						type = Files.probeContentType(fc.getSelectedFile().toPath());
						if (type.contains("text")) {
							System.out.println("GOOD");
							TextSaveHandler.loadTable(_captionsTable, fc.getSelectedFile().getName());
						} else {
							JOptionPane.showMessageDialog(null, "Invalid file chosen.");
							return;
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
			
		});
		
		_saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String outputFile = JOptionPane.showInputDialog(null, "Enter output file name:", "Dialog for Input",
				        JOptionPane.WARNING_MESSAGE);
				if (outputFile != null) {
					String wd = System.getProperty("user.dir");
					String path = wd + "/" + outputFile + ".txt";
					
					String[] outputSaveExists = new BashCommand().runBash("if [ ! -f " + path + " ]; then echo 0; else echo 1; fi");
					
					if (outputSaveExists[0].equals("1")) {
						int selectedOption = JOptionPane.showConfirmDialog(null, 
                                "File exists. Overwrite?", 
                                "Choose", 
                                JOptionPane.YES_NO_OPTION); 
						if (selectedOption == JOptionPane.YES_OPTION) {
							TextSaveHandler.saveTable(_captionsTable, outputFile);
						} 
					} else {
						TextSaveHandler.saveTable(_captionsTable, outputFile);
					}
				}
				
			}
			
		});
		
		_previewTextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_textInput.getText().length() <= 0) {
					JOptionPane.showMessageDialog(null, "Please enter some text.");
					return;
				} else if (getTimeAsString(1).equals("0:0:0") && getTimeAsString(2).equals("0:0:0")) { // TODO check start and end times
					JOptionPane.showMessageDialog(null, "Please enter a duration greater than 0.");
					return;
				} else if (Integer.parseInt(getTimeInSeconds(2)) <= Integer.parseInt(getTimeInSeconds(1))) {
					JOptionPane.showMessageDialog(null, "End time must be greater than start time.");
					return;
				}
				
				String stringCommand = VideoTextHandler.makeCommand(_inputVideo.getText(), _textInput.getText(), getTimeInSeconds(1), getTimeInSeconds(2),
						_fontPath, _colourHexValue, "preview.mp4");
				new VideoTextHandler(stringCommand);
				new BashCommand().runBash("avconv -y -i preview.mp4 -r 3 -ss "+ getTimeAsString(1) + " -t "+ getTimeAsString(2) +" -s 500x500 -f image2 prev.png 2> /dev/null");
				
				Image image;
				try {
					String path = System.getProperty("user.dir") + "/" + "prev.png";
					image = ImageIO.read(new File(path));
					ImageIcon icon = new ImageIcon(image);
					JLabel iconLabel = new JLabel();
					iconLabel.setIcon(icon);
					
					JDialog dialog = new JDialog();
					dialog.getContentPane().add(iconLabel, BorderLayout.CENTER);
					dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
					dialog.setSize(new Dimension(500,500));
					dialog.setVisible(true);


					
				} catch (IOException e1) {}
				
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
	
	public static String getTimeInSeconds(String time) {
		String[] times = time.split(":");
		
		int hours = Integer.parseInt(times[0]);
		int mins = Integer.parseInt(times[1]);
		int secs = Integer.parseInt(times[2]);
		
		hours = hours * 360;
		mins = mins * 60;
		
		String outTime = Integer.toString(hours + mins + secs);
		return outTime;
	}
	
	private void addCaptionToTable() {
		if (_textInput.getText().length() <= 0) {
			JOptionPane.showMessageDialog(null, "Please enter some text.");
			return;
		} else if (getTimeAsString(1).equals("0:0:0") && getTimeAsString(2).equals("0:0:0")) { // TODO check start and end times
			JOptionPane.showMessageDialog(null, "Please enter a duration greater than 0.");
			return;
		} else if (Integer.parseInt(getTimeInSeconds(2)) <= Integer.parseInt(getTimeInSeconds(1))) {
			JOptionPane.showMessageDialog(null, "End time must be greater than start time.");
			return;
		}
		
		String[] captionData = {_textInput.getText(), getTimeAsString(1), getTimeAsString(2), _fontPath, _colourHexValue};
		_tableModel.addRow(captionData);
		
		
	}



	public static void finishProgressBar() {
		progressBar.setIndeterminate(false);
		progressBar.setString("Finished");
		
	}
	
}
