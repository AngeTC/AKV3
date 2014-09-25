package vamixUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

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
	private final JTextField _inputVideo = new JTextField("Pick a video to add text to...");
	private final JButton _chooseImageButton = new JButton("Choose");
	// Bottom
	private final JButton _previewTextButton = new JButton("Preview text");
	private final JButton _addButton = new JButton("Add");
	
	// JTable and associated buttons
	CaptionTableModel _tableModel = new CaptionTableModel();
	private final JTable _captionsTable = new JTable(_tableModel);
	private final JScrollPane _tableScrollPane = new JScrollPane(_captionsTable);
	private final JButton _playButton = new JButton("Play video with changes");
	private final JButton _deleteButton = new JButton("Delete");
	private final JButton _editButton = new JButton("Edit");
	
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
		_inputVideoPanel.add(_chooseImageButton);
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
		
		//-------END OF LAYING OUT OF COMPONENTS-----------
		
		//---------START LISTENERS/FUNCTIONALITY-----------
		_captionsTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        int row = table.rowAtPoint(p);
		        if (me.getClickCount() == 2) {
		            System.out.println(row + ":" + table.getValueAt(row, 0));
		        }
		    }
		});
		
		_addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addCaptionToTable();
				
			}
			
		});
		
	}

	/**
	 * Returns string representation of duration time
	 * @return
	 */
	private String getDurationAsString() {
		String hours = _hoursSpinner1.getValue().toString();
		String mins = _minsSpinner1.getValue().toString();
		String secs = _secsSpinner1.getValue().toString();
		String time = hours + ":" + mins + ":" + secs;
		
		return time;
	}
	
	private String getDurationInSeconds() {
		int hours = Integer.parseInt( _hoursSpinner1.getValue().toString()) * 360;
		int mins = Integer.parseInt( _hoursSpinner1.getValue().toString()) * 60;
		int secs = Integer.parseInt( _hoursSpinner1.getValue().toString());
		
		String time = Integer.toString(hours + mins + secs);
		return time;
	}
	
	private void addCaptionToTable() {
		if (_textInput.getText().length() <= 0) {
			JOptionPane.showMessageDialog(null, "Please enter some text.");
		} else if (getDurationAsString().equals("0:0:0")) { // TODO check start and end times
			JOptionPane.showMessageDialog(null, "Please enter a duration greater than 0.");
		}
		
	}
	
	private void deleteCaptionFromTable() {
		// TODO
		
	}
	
}
