package de.hsh.dbs2.imdb.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.hsh.dbs2.imdb.logic.dto.MovieDTO;

/**
 * Tabellenmodell fuer eine Tabelle mit allen Filmen
 */
public class TabModelMovie extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private final List<MovieDTO> data;
	private final List<String> columns = new ArrayList<>();

	public TabModelMovie(List<MovieDTO> data) {
		this.data = data;
		columns.add("Id");
		columns.add("Title");
		columns.add("Type");
		columns.add("Year");
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (column == 0)
			return data.get(row).getId();
		else if (column == 1)
			return data.get(row).getTitle();
		else if (column == 2)
			return data.get(row).getType();
		else if (column == 3)
			return data.get(row).getYear();
		return null;
	}

	@Override
	public String getColumnName(int column) {
		return columns.get(column);
	}

	public MovieDTO getRow(int row) {
		return data.get(row);
	}

}
