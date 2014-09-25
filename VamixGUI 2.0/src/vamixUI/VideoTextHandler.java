package vamixUI;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class VideoTextHandler extends JDialog {
	private JProgressBar _progress = new JProgressBar();
	private JButton _cancel = new JButton("Cancel");
	private AvconvTask _task;

	private String makeCommand(String videoPath, String text, String start, String end, String fontPath, String colour, String outputFile, boolean isPreview) {
		String command = "avconv -i " + videoPath + " -vf " + "drawtext=\"fontfile='" + fontPath 
				+ "':fontcolor=" + colour + ":text='" + text + "':draw='gt(t," + start + ")*lt(t," + end + ")'\" " + outputFile;
		System.out.println(command);
		return command;
	}
	
	public VideoTextHandler(String videoPath, String text, String start, String end, String fontPath, String colour, String outputFile, boolean isPreview) {
		String cmd = makeCommand(videoPath, text, start, end, fontPath, colour, outputFile, isPreview);
		_task = new AvconvTask(cmd);
		_task.execute();
		_progress.setString("Processing...");
		final JComponent[] components = new JComponent[] {
				_progress,
				_cancel
		};
		JOptionPane.showMessageDialog(null, components, "Progress", JOptionPane.PLAIN_MESSAGE);
	}
	
	class AvconvTask extends SwingWorker<Void, String> {

		private String _cmd = "";
		private Process _process = null;
		
		
		public AvconvTask(String cmd) {
			_cmd = cmd;
		}

		/**
		 * Runs the background process for 'avconv'.
		 */
		@Override
		protected Void doInBackground() throws Exception {
			
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", _cmd);
			
			try {		
				//Create and run new process for wget.
				builder.redirectErrorStream(true);

				_process = builder.start();
				InputStream stdout = _process.getInputStream();

				BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				String line = null;

				//New list for download output.
				ArrayList<String> avconvOutput = new ArrayList<String>();

				while ((line = stdoutBuffered.readLine()) != null ) {
					// if process is cancelled, finish
					if (isCancelled()) {
						_process.destroy();
						done();
					
					//Else, run publish() and add line to the output list.
					} else {
						publish(line);
						avconvOutput.add(line);
					}
				}

				/*
				 *If any error occurrs, display new error message by extracting the final
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
		 * Show animation while process isn't finished.
		 */
		@Override
		protected void process(List<String> chunks) {

			_progress.setIndeterminate(true);
				
		} 

		protected void done() {
			try {
				//If cancelled, display 'cancelled' message.
				if (isCancelled()) {
					JOptionPane.showMessageDialog(null, "Aborted.");

				//If no errors occurred, display 'complete' status.
				} else if (_process.waitFor() == 0) {
					_progress.setValue(100);
					_progress.setString("Finished!");
				}
			} catch (InterruptedException e) {
				
			}
		}
	}
}
