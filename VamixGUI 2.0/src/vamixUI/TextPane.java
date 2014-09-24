package vamixUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

public class TextPane extends JPanel {
	// main sections
	private JPanel _textAndChangesPanel = new JPanel();
	private JPanel _addTextPanel = new JPanel();
	private JPanel _changesPanel = new JPanel();
	private JPanel _buttonPanel = new JPanel();
	
	//sub sections
	private JPanel _textOptionPanel = new JPanel();
	private JPanel _imageOptionPanel = new JPanel();
	private JPanel _confirmationPanel = new JPanel();
	private JPanel _spinnerPanel = new JPanel();
	private JPanel _fontAndColourPanel = new JPanel();
	private JPanel _scenePanel = new JPanel();
	
	// button panel
	private final JButton _loadButton = new JButton("Load");
	private final JButton _saveButton = new JButton("Save project");
	private final JButton _exportButton = new JButton("Export");
	
	// Add text panel components
	private final JLabel _sceneLabel = new JLabel("Scene");
	private final String[] _sceneStrings = { "Opening Scene", "Closing Scene" };
	private final JComboBox _sceneComboBox = new JComboBox(_sceneStrings);
	// Text section
	private final JLabel _text = new JLabel("Text");
	private final JTextArea _textInput = new JTextArea("Type a caption to add...");
	private final JScrollPane _textScroll = new JScrollPane(_textInput);
	private final JButton _fontButton = new JButton("Font");
	private final JButton _colourButton = new JButton("Colour");
	private final JLabel _durationLabel = new JLabel("Duration:");
	private final JSpinner _hoursSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner _minsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
	private final JSpinner _secsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
	private final JLabel _timeSep1 = new JLabel(":");
	private final JLabel _timeSep2 = new JLabel(":");
	// Image section TODO: may remove
	private final JTextField _inputImage = new JTextField("Image file");
	private final JButton _chooseImageButton = new JButton("Choose...");
	// Bottom
	private final JButton _previewTextButton = new JButton("Preview");
	private final JButton _okButton = new JButton("OK");
	
	// JTable and play button
	private final JTable _addedTextTable = new JTable();
	private final JButton _playButton = new JButton("Play video with above changes");
	
	public TextPane() {
		setLayout(new BorderLayout());
		
		// button panel, NORTH
		_buttonPanel.add(_loadButton);
		_buttonPanel.add(_saveButton);
		_buttonPanel.add(_exportButton);
		add(_buttonPanel, BorderLayout.NORTH);
		
		// add text panel and changes panel, CENTRE
		_textAndChangesPanel.setLayout(new GridLayout(2,0));
		//*****ADD TEXT PANEL******
		_addTextPanel.setLayout(new BorderLayout());
		// Add scene option
		_scenePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		_scenePanel.add(_sceneLabel);
		_scenePanel.add(_sceneComboBox);
		_addTextPanel.add(_scenePanel, BorderLayout.NORTH);
		// Text options
		_textOptionPanel.setLayout(new BorderLayout());
		_textOptionPanel.add(_textInput, BorderLayout.CENTER);
		_textOptionPanel.add(_durationLabel, BorderLayout.SOUTH);
		// spinner panel
		_spinnerPanel.add(_hoursSpinner);
		_spinnerPanel.add(_timeSep1);
		_spinnerPanel.add(_minsSpinner);
		_spinnerPanel.add(_timeSep2);
		_spinnerPanel.add(_secsSpinner);
		_textOptionPanel.add(_spinnerPanel, BorderLayout.SOUTH);
		
		// add border to text option panel
		TitledBorder textOptionBorder = BorderFactory.createTitledBorder("Text options");
		_textOptionPanel.setBorder(textOptionBorder);
		
		_addTextPanel.add(_textOptionPanel, BorderLayout.CENTER);
		
		
		_textAndChangesPanel.add(_addTextPanel);
		_textAndChangesPanel.add(_changesPanel);
		
		add(_textAndChangesPanel, BorderLayout.CENTER);
		
		// text box and font options
		
	}
	
	
}
