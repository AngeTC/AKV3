package vamixUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class DirPane extends JPanel {
	private final JButton _changeDirButton = new JButton("Change directory");
	private final JButton _refreshButton = new JButton("Refresh");
	private final JButton _openButton = new JButton("Open");
	private File _selectedFile = null;
	private File _directory = null;
	
	public DirPane(File directory) {
		
		// set directory
		_directory = directory;
		
		// set layout
		setLayout(new BorderLayout());

		// Make tree list with all the nodes, and make it into a JTree
		final JTree tree = new JTree();
		tree.setModel(new DirTreeModel(new File(System.getProperty("user.dir"))));
		
		// Listener for file selection
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// get selected file
				TreePath node = e.getPath();
				File selected = (File)node.getLastPathComponent();
		
				TreePath path = e.getNewLeadSelectionPath();
				
				// return if path is directory
				if (path == null) {
					return;
				}
				
				String parent = path.getParentPath().toString();
				parent = parent.replace("[", "");
				parent = parent.replace("]", "");
				
				String output = parent + "/" + selected.toString();
				_selectedFile = new File(output);
				
			}
		});
		
		// Put JTree into JScrollPane
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.getViewport().add(tree);
		add(scrollpane, BorderLayout.CENTER);
		
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout());
		buttonPanel.add(_changeDirButton);
		buttonPanel.add(_refreshButton);
		buttonPanel.add(_openButton);
		
		// let user change directory and refresh the Jtree
		_changeDirButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int returnValue = fc.showOpenDialog(buttonPanel);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					System.out.println("You chose: " + fc.getSelectedFile().getName());
					String dirPath = fc.getSelectedFile().getAbsolutePath();
					_directory = new File(dirPath);
					tree.setModel(new DirTreeModel(_directory));
				}
				
			}
			
		});
		
		// refresh the current directory
		_refreshButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tree.setModel(new DirTreeModel(_directory));
				
			}
			
		});
		
		// open selected file
		_openButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_selectedFile != null) {
					// if file is valid (video or audio)
					VLCPlayer.getInstance().setMediaPath(_selectedFile.getAbsolutePath());
					VLCPlayer.getInstance().play();
					VamixGUI.getInstance().setPlay();
				}	
			}
		});
		
		add(buttonPanel, BorderLayout.SOUTH);
		
	}

}