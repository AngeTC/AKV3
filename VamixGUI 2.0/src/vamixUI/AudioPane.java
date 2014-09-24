package vamixUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

/**
 * Panel containing audio functionality.
 * 
 * @author acas212 & kxie094
 *
 */
@SuppressWarnings("serial")
public class AudioPane extends JPanel implements ActionListener {

	private final JLabel _replaceAndOverlayLabel = new JLabel("Replace / Overlay Audio:");
	private final JPanel _audioSelectPanel = new JPanel(new BorderLayout());
	private final JPanel _audioSelectSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
	private final JLabel _inputFileLabel = new JLabel("Select Audio File To Use:");
	private final JTextField _chosenAudioInput = new JTextField(18);
	private final JButton _audioFileButton = new JButton("Select Audio File");

	private final JLabel _stripLabel = new JLabel("Strip Audio:");
	private final JButton _stripButton = new JButton("Strip Audio");
	private final JButton _stripCancelButton = new JButton("Cancel");
	
	private final JCheckBox _removeAudioOnVideo = new JCheckBox("Remove Stripped Audio on Video?");
	private final JCheckBox _haveAudioOutput = new JCheckBox("Create Output of Stripped Audio?");
	private final JPanel _stripPanel = new JPanel(new BorderLayout());
	private final JPanel _stripSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
	private final JPanel _stripButtonPanel = new JPanel(new GridLayout(0,1));

	private final JPanel _bottomButtonPanel = new JPanel(new BorderLayout());
	private final JPanel _bottomSubButtonPanel = new JPanel(new GridLayout(0,2));
	private final JButton _replaceButton = new JButton("Replace Audio");
	private final JButton _overlayButton = new JButton("Overlay Audio");
	private final JButton _overlayWorkerCancelButton = new JButton("Cancel");

	private final JProgressBar _processBar = new JProgressBar();

	private ExtractWorker _eW;
	private StripWorker _sW;
	private OverlayWorker _oW;

	private File _selectedAudio;
	private File _currentWorkingVideo;
	private String _saveStateName = "vamixTempState.mp4";

	/**
	 * SwingWorker for extract functionality, used
	 * for extracting the video audio in the background.
	 * 
	 * @author acas212
	 */
	class ExtractWorker extends SwingWorker<Void, Void> {

		private File _video;
		private Process _process;

		/**
		 * Constructor for ExtractWorker.
		 */
		public ExtractWorker(File video) {
			_video = video;
		}

		/**
		 * Runs the background process for 'avconv'.
		 */
		@Override
		protected Void doInBackground() throws Exception {
			System.out.println(_video.getAbsolutePath());

			ProcessBuilder builder = new ProcessBuilder("avconv", "-y", "-i", _video.getAbsolutePath(), "-map", "0:a", "-strict", "experimental", "audioStripped.mp3");
			try {		
				//Create and run new process for avconv.
				builder.redirectErrorStream(true);

				_process = builder.start();
				InputStream stdout = _process.getInputStream();

				BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				String line = null;

				//New list for extract output.
				ArrayList<String> extractOutput = new ArrayList<String>();

				while ((line = stdoutBuffered.readLine()) != null ) {
					//If cancelled, end avconv command.
					if (this.isCancelled()) {
						_process.destroy();

						//Else, add line to the output list.
					} else {
						extractOutput.add(line);
					}
				}

				/*
				 *If any error occurred, display new error message by extracting the final
				 *line of the extract output list.
				 */
				if (_process.waitFor() != 0) {
					String errorMsg = extractOutput.get(extractOutput.size() - 1);
					JOptionPane.showMessageDialog(null, "Error: " + errorMsg);
				}

			} catch (Exception e) {}
			return null;
		}

		/**
		 * Method which displays the final message once the 
		 * Worker has finished / been cancelled.
		 */
		@Override
		protected void done() {
			try {
				//If other worker not made: 
				if (_sW == null) {
					//End process bar normally.
					_processBar.setIndeterminate(false);
					_processBar.setString("No Tasks Being Performed");
				//Else:
				} else {
					//Make a check if other worker is finished.
					if (_sW.isDone() == true) {
						_processBar.setIndeterminate(false);
						_processBar.setString("No Tasks Being Performed");
					}
				}

				//If cancelled, display 'cancelled' message.
				if (isCancelled()) {
					JOptionPane.showMessageDialog(null, "Stripped Audio Extraction has been Cancelled.");

					//If no errors occurred, display 'complete' status.	
				} else if (_process.waitFor() == 0) {
					JOptionPane.showMessageDialog(null, "Stripped Audio Extraction Complete.");
				}
			} catch (InterruptedException e) {}
		}
	}

	/**
	 * SwingWorker for stripping functionality, used
	 * for removing the audio from the video in the background.
	 * 
	 * @author acas212
	 */
	class StripWorker extends SwingWorker<Void, Void> {

		private boolean _audioRemoved;
		private boolean _audioOut;
		private File _video;
		private Process _process;

		/**
		 * Constructor for StripWorker.
		 */
		public StripWorker(boolean audioKept, boolean audioOut, File video) {
			_audioRemoved = audioKept;
			_audioOut = audioOut;
			_video = video;
		}

		/**
		 * Runs the background process for 'avconv' for stripping audio.
		 */
		@Override
		protected Void doInBackground() throws Exception {

			ProcessBuilder builder = null;

			if (_audioRemoved) {
				if (_audioOut) {
					//Extract and remove audio:
					_eW = new ExtractWorker(_video);
					_eW.execute();
					builder = new ProcessBuilder("avconv", "-y", "-i", _video.getAbsolutePath(), "-an", _saveStateName);

				} else {
					//Remove audio:
					builder = new ProcessBuilder("avconv", "-y", "-i", _video.getAbsolutePath(), "-an", _saveStateName);
				}
			} else { //Not removed
				if (_audioOut) {
					//Just extract audio.
					_eW = new ExtractWorker(_video);
					_processBar.setIndeterminate(true);
					_processBar.setString("Processing...");
					_eW.execute();
				}
			}

			try {		
				//Create and run new process for avconv.
				builder.redirectErrorStream(true);

				_process = builder.start();
				InputStream stdout = _process.getInputStream();

				BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				String line = null;

				//New list for extract output.
				ArrayList<String> stripOutput = new ArrayList<String>();

				_processBar.setIndeterminate(true);
				_processBar.setString("Processing...");

				while ((line = stdoutBuffered.readLine()) != null ) {
					//If cancelled, end avconv command.
					if (this.isCancelled()) {
						_eW.cancel(true);
						_process.destroy();

						//Else, add line to the output list.
					} else {
						stripOutput.add(line);
					}
				}

				/*
				 *If any error occurred, display new error message by extracting the final
				 *line of the extract output list.
				 */
				if (_process.waitFor() != 0) {
					String errorMsg = stripOutput.get(stripOutput.size() - 1);
					JOptionPane.showMessageDialog(null, "Error: " + errorMsg);
				}

			} catch (Exception e) {}
			return null;
		}

		/**
		 * Method which displays the final message once the 
		 * Worker has finished / been cancelled.  Also handles 
		 * the logging of a successful extract.
		 */
		@Override
		protected void done() {
			try {
				if (_process != null) {
					
					//If other worker not made:
					if (_eW == null) {
						//End process bar normally.
						_processBar.setIndeterminate(false);
						_processBar.setString("No Tasks Being Performed");
					//Else:
					} else {
						//Make a check if other worker is finished.
						if (_eW.isDone() == true) {
							_processBar.setIndeterminate(false);
							_processBar.setString("No Tasks Being Performed");
						}
					}

					//If cancelled, display 'cancelled' message.
					if (isCancelled()) {
						JOptionPane.showMessageDialog(null, "Strip has been cancelled.");

						//If no errors occurred, display 'complete' status.	
					} else if (_process.waitFor() == 0) {
						JOptionPane.showMessageDialog(null, "Strip Complete.");
					}
				}
			} catch (InterruptedException e) {}
		}
	}

	/**
	 * SwingWorker for overlaying and
	 * replacing audio in the background.
	 * 
	 * @author acas212
	 */
	class OverlayWorker extends SwingWorker<Void, Void> {

		private boolean _replaceSelected;
		private File _audio;
		private File _video;
		private Process _process;

		/**
		 * Constructor for OverlayWorker.
		 */
		public OverlayWorker(boolean replaceSelected, File audio, File video) {
			_replaceSelected = replaceSelected;
			_audio = audio;
			_video = video;
		}

		/**
		 * Runs the background process for 'avconv' for replacing / overlaying audio.
		 */
		@Override
		protected Void doInBackground() throws Exception {
			ProcessBuilder builder;

			builder = new ProcessBuilder("avconv", "-y", "-i", _video.getAbsolutePath(), "-i", _audio.getAbsolutePath(), "-filter_complex", "amix=inputs=2", "-strict", "experimental", _saveStateName);

			if (_replaceSelected) {
				builder = new ProcessBuilder("avconv", "-y", "-i", _video.getAbsolutePath(), "-i", _audio.getAbsolutePath(), "-c:v", "copy", "-c:a", "copy", "-map", "0:0", "-map", "1:a", _saveStateName);
			}

			try {		
				//Create and run new process for avconv.
				builder.redirectErrorStream(true);
				System.out.println("beforeProcess");

				_process = builder.start();

				System.out.println("afterProcess");
				InputStream stdout = _process.getInputStream();

				BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				String line = null;

				//New list for avconv output.
				ArrayList<String> avconvOutput = new ArrayList<String>();

				_processBar.setIndeterminate(true);
				_processBar.setString("Processing...");

				while ((line = stdoutBuffered.readLine()) != null ) {
					//If cancelled, end avconv command.
					if (this.isCancelled()) {
						_process.destroy();

						//Else, add line to the output list.
					} else {
						avconvOutput.add(line);
					}
				}

				/*
				 *If any error occurred, display new error message by extracting the final
				 *line of the output list.
				 */
				if (_process.waitFor() != 0) {
					String errorMsg = avconvOutput.get(avconvOutput.size() - 1);
					JOptionPane.showMessageDialog(null, "Error: " + errorMsg);
				}

			} catch (Exception e) {}
			return null;
		}

		/**
		 * Method which displays the final message once the 
		 * Worker has finished / been cancelled.
		 */
		@Override
		protected void done() {
			try {
				_processBar.setIndeterminate(false);
				_processBar.setString("No Tasks Being Performed");

				//If cancelled, display 'cancelled' message.
				if (isCancelled()) {
					if (_replaceSelected) {
						JOptionPane.showMessageDialog(null, "Replace has been cancelled.");
					} else {
						JOptionPane.showMessageDialog(null, "Overlay has been cancelled.");
					}

					//If no errors occurred, display 'complete' status.	
				} else if (_process.waitFor() == 0) {
					if (_replaceSelected) {
						JOptionPane.showMessageDialog(null, "Replace Complete.");
					} else {
						JOptionPane.showMessageDialog(null, "Overlay Complete.");
					}
				}
			} catch (InterruptedException e) {}
		}
	}

	public AudioPane() {

		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 10));
		
		_stripPanel.setPreferredSize(new Dimension(385, 90));
		_stripSubPanel.setPreferredSize(new Dimension(260, 70));
		_stripButtonPanel.setPreferredSize(new Dimension(80, 70));
		
		add(_stripPanel);
		_stripPanel.setBorder(BorderFactory.createTitledBorder("Strip Audio:"));
		
		_stripPanel.add(_stripButtonPanel, BorderLayout.CENTER);
		_stripPanel.add(_stripSubPanel, BorderLayout.WEST);
		_stripButtonPanel.add(_stripButton);
		_stripButtonPanel.add(_stripCancelButton);
		_stripSubPanel.add(_removeAudioOnVideo);
		_stripSubPanel.add(_haveAudioOutput);

		add(_replaceAndOverlayLabel);
		add(_audioSelectPanel);
		_audioSelectPanel.add(_inputFileLabel, BorderLayout.NORTH);
		_audioSelectPanel.add(_audioSelectSubPanel, BorderLayout.CENTER);
		_audioSelectSubPanel.add(_chosenAudioInput);
		_audioSelectSubPanel.add(_audioFileButton);

		_bottomButtonPanel.setPreferredSize(new Dimension(380, 60));
		_bottomSubButtonPanel.setPreferredSize(new Dimension(380, 30));
		add(_bottomButtonPanel);
		_bottomButtonPanel.add(_bottomSubButtonPanel, BorderLayout.NORTH);
		_bottomButtonPanel.add(_overlayWorkerCancelButton, BorderLayout.CENTER);
		_bottomSubButtonPanel.add(_replaceButton);
		_bottomSubButtonPanel.add(_overlayButton);

		_processBar.setPreferredSize(new Dimension(380, 20));
		_processBar.setString("No Tasks Being Performed");
		_processBar.setStringPainted(true);
		add(_processBar);

		_audioFileButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();

				int returnValue = fileChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {

					File selectedFile = fileChooser.getSelectedFile();

					String type;
					try {
						type = Files.probeContentType(selectedFile.toPath());
						if(type.contains("audio")) {
							_selectedAudio = selectedFile;
							_chosenAudioInput.setText(selectedFile.getName());
						} else {
							JOptionPane.showMessageDialog(null, "File selected is not a audio " +
									"file. Please select another file.");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				} 
			}
		});

		_stripButton.addActionListener(this);
		_stripCancelButton.addActionListener(this);
		_replaceButton.addActionListener(this);
		_overlayButton.addActionListener(this);
		_overlayWorkerCancelButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == _stripCancelButton) {
			_sW.cancel(true);
			if (_eW != null) {
				_eW.cancel(true);
			}

		} else if (ae.getSource() == _overlayWorkerCancelButton) {
			_oW.cancel(true);

		} else if (VLCPlayerPane.getInstance().getMediaPath().equals("")) {
			JOptionPane.showMessageDialog(null, "Error: No video to edit. Please select a file by " +
					"playing a new media file using the Open button or using the 'Files' " +
					"tab to the right.");
			
		} else if (ae.getSource() == _stripButton) {
			if (!_removeAudioOnVideo.isSelected() && !_haveAudioOutput.isSelected()) {
				JOptionPane.showMessageDialog(null, "Error: No option selected for audio " +
						"stripping. Please select a combination from the two provided " +
						"checkboxes.");
			}

			_currentWorkingVideo = new File(VLCPlayerPane.getInstance().getMediaPath());
			_sW = new StripWorker(_removeAudioOnVideo.isSelected(), _haveAudioOutput.isSelected(), _currentWorkingVideo);
			_sW.execute();

		} else {
			if (_selectedAudio == null) {
				JOptionPane.showMessageDialog(null, "Error: No audio selected to replace / " +
						"overlay with. Please select an audio file using the button above.");
				
			}
			if (ae.getSource() == _replaceButton) {
				_currentWorkingVideo = new File(VLCPlayerPane.getInstance().getMediaPath());
				_oW = new OverlayWorker(true, _selectedAudio, _currentWorkingVideo);
				_oW.execute();
				
			} else if (ae.getSource() == _overlayButton) {
				_currentWorkingVideo = new File(VLCPlayerPane.getInstance().getMediaPath());
				_oW = new OverlayWorker(false, _selectedAudio, _currentWorkingVideo);
				_oW.execute();
				
			}
		} 
	}
}
