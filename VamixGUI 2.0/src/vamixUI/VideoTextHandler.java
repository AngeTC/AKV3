package vamixUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class VideoTextHandler {
	private static AvconvTask _task;

	public static String makeCommand(String videoPath, String text, String start, String end, String fontPath, String colour, String outputFile) {
		String command = "avconv -i " + videoPath + " -vf " + "drawtext=\"fontfile='" + fontPath 
				+ "':fontcolor=" + colour + ":text='" + text + "':draw='gt(t," + start + ")*lt(t," + end + ")'\" " + outputFile;
		System.out.println(command);
		return command;
	}
	
	public VideoTextHandler(String cmd) {
		_task = new AvconvTask(cmd);
		_task.execute();
	}

	class AvconvTask extends SwingWorker<Void, String> {

		private String _cmd = "";
		private Process _process = null;

		public void setCmd(String cmd) {
			_cmd = cmd;
		}
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
				//Create and run new process.
				builder.redirectErrorStream(true);

				_process = builder.start();
				InputStream stdout = _process.getInputStream();

				BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				String line = null;

				//New list for output.
				ArrayList<String> avconvOutput = new ArrayList<String>();

				while ((line = stdoutBuffered.readLine()) != null ) {
					// if process is cancelled, destroy process
					if (isCancelled()) {
						_process.destroy();
						
					} else {
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

		protected void done() {
			try {
				//If cancelled, display 'cancelled' message.
				if (isCancelled()) {
					JOptionPane.showMessageDialog(null, "Aborted.");

					//If no errors occurred, display 'complete' status.
				} else if (_process.waitFor() == 0) {
					
					//If no errors occurred, display 'complete' status.
					TextPane.finishProgressBar();
					System.out.println("finished");
				}
			} catch (InterruptedException e) {}
		}
	}

	public static void cancelTask() {
		_task.cancel(true);
		
	}

/*	class BigTask extends SwingWorker<Void, String> {

		private TableModel tableModel;
		private String videoPath;


		public BigTask(TableModel _tableModel, String _videoPath) {
			tableModel = _tableModel;
			videoPath = _videoPath;
		}

		*//**
		 * Runs the background process for 'avconv'.
		 *//*
		@Override
		protected Void doInBackground() throws Exception {

			int outputCount = 0;
			String input = videoPath;
			String defOutput = System.getProperty("user.dir") + "/" + "out";
			String output = "";
			
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				output = defOutput + outputCount + ".mp4";
				String cmd = VideoTextHandler.makeCommand(input, tableModel.getValueAt(i, 0).toString(), TextPane.getTimeInSeconds(tableModel.getValueAt(i, 1).toString()), TextPane.getTimeInSeconds(tableModel.getValueAt(i, 2).toString()),
						tableModel.getValueAt(i, 3).toString(),tableModel.getValueAt(i, 4).toString(), output);
				_task = new AvconvTask(cmd);
				_task.execute();
				while(!_task.isDone()) {
					if (isCancelled()) {
						_task.cancel(true);
						return null;
					}
				}

				input = output;
				outputCount++;

			}

			return null;
		}

		protected void done() {
				//If cancelled, display 'cancelled' message.
				if (isCancelled()) {
					JOptionPane.showMessageDialog(null, "Aborted.");

					//If no errors occurred, display 'complete' status.
					TextPane.finishProgressBar();
					
					System.out.println("finished");
				}

			}
		}*/
}
