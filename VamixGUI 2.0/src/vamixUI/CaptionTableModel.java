package vamixUI;

import javax.swing.table.AbstractTableModel;

public class CaptionTableModel extends AbstractTableModel {
	
	private String[] _columns = {"Text", "Start", "End"};
	private Object[][] _data = new Object[50][4];
	public CaptionTableModel() {
		
	}
	@Override
	public int getRowCount() {
		return _data.length;
	}

	@Override
	public int getColumnCount() {
		return _columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return _data[rowIndex][columnIndex];
	}
	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		_data[rowIndex][columnIndex] = aValue;
		fireTableCellUpdated(rowIndex,columnIndex);
	}
	
	public String getColumnName(int column) { //TODO might not need
		return _columns[column];
	}
	
	private int getFirstEmptyRow() {
		int row = 0;
		while (row < getRowCount()) {
			if (_data[row][0] == null) {
				break;
			}
			row++;
		}
		return row;
	}
	
	/**
	 * Add data to first empty row
	 * @param text
	 * @param start
	 * @param end
	 */
	public void addData(String text, String start, String end) {
		int row = getFirstEmptyRow();
		setValueAt(text, row, 0);
		setValueAt(text, row, 1);
		setValueAt(text, row, 2);
	}
	
	/**
	 * Replace data at specified row
	 * @param row
	 * @param text
	 * @param start
	 * @param end
	 */
	public void addData(int row, String text, String start, String end) {
		setValueAt(text, row, 0);
		setValueAt(text, row, 1);
		setValueAt(text, row, 2);
	}
	
	public void removeData(int row) {
		
	}
}
