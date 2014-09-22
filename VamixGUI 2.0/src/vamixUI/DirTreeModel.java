package vamixUI;

import java.io.File;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Credits to:
 * http://repo.hackerzvoice.net/depot_madchat/ebooks/Oreilly_Nutshells/books/javaenterprise/jfc/ch03_19.htm
 * Modified some methods.
 */
public class DirTreeModel implements TreeModel {
	
	private File _root;
	private Vector<TreeModelListener> _listeners = new Vector<>();
	
	public DirTreeModel(File root) {
		_root = root;
	}
	
	@Override
	public Object getRoot() {
		return _root;
	}

	@Override
	public Object getChild(Object parent, int index) {
		String[] children = ((File)parent).list();
	    if ((children == null) || (index >= children.length)) {
	    	return null;
	    }
	    return new File(children[index]);
	}

	@Override
	public int getChildCount(Object parent) {
		String[] children = ((File)parent).list();
		if (children == null) {
			return 0;
		}
		return children.length;
	}

	// Checks if object is not a directory. Used for rendering the icons on the Jtree.
	@Override
	public boolean isLeaf(Object node) {
		File file = (File)node;
		return !file.isDirectory();
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		String[] children = ((File)parent).list();
	    if (children == null) return -1;
	    String childName = ((File)child).getName();
	    for(int i = 0; i < children.length; i++) {
	      if (childName.equals(children[i])) return i;
	    }
	    return -1;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		_listeners.add(l);
		
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		_listeners.remove(l);
		
	}

}
