package vamixUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/**
 * Panel used for traversing 
 * through user folders to find 
 * files.
 * 
 * @author kxie094 (?)
 *
 */
@SuppressWarnings("serial")
public class DirPane extends JPanel {
	private final JButton _changeDirButton = new JButton("Change directory");
	private final JButton _refreshButton = new JButton("Refresh");
	private final JButton _openButton = new JButton("Open");
	private File _selectedFile = null;
	
	public DirPane(final File directory) {
		// Set layout of panel.
		setLayout(new BorderLayout());

		// Make tree list with all the nodes, and make it into a JTree
		// JTree tree = new JTree(DirPaneOp.addNodes(null, directory));
		final JTree tree = new JTree();
		tree.setModel(new DirTreeModel(new File(System.getProperty("user.dir"))));
		
		// Add listener for file selection.
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// Get selected file from tree.
				TreePath node = e.getPath();
				_selectedFile = (File)node.getLastPathComponent();
				
				TreePath test = e.getNewLeadSelectionPath();
				String parent = test.getParentPath().toString();
				parent = parent.replace("[", "");
				parent = parent.replace("]", "");
				
				String output = parent + "/" + _selectedFile.toString();
				 _selectedFile = new File(output);
				 
				System.out.println("Selected: " + _selectedFile.toString());
			}
		});
		
		// Put JTree into JScrollPane.
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
					tree.setModel(new DirTreeModel(new File(dirPath)));
				}
				
			}
			
		});
		
		// refresh the current directory
		_refreshButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tree.setModel(new DirTreeModel(directory));
				
			}
			
		});
		
		// Add listener to open button to open selected file.
		_openButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_selectedFile != null) {
					//Testing:
					try {
						String type = Files.probeContentType(_selectedFile.toPath());
						System.out.println("Type of file: " + type);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					// if file is valid (video or audio) 
					//TODO: Error Checking needs to be done.
					
					VLCPlayerPane.getInstance().setMediaPath(_selectedFile.getAbsolutePath());
					VLCPlayerPane.getInstance().play();
					VamixGUI.getInstance().setPlay();
					System.out.println("WORKED");
				}	
			}
		});
		
		add(buttonPanel, BorderLayout.SOUTH);
		
	}

}