package org.pdf.forms.gui.properties.object.field;

import static java.util.stream.Collectors.toUnmodifiableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class ItemsTableModel extends AbstractTableModel {

    private static final String[] HEADINGS = {
            "Text"
    };

    private final List<Map<String, Object>> values = new ArrayList<>();

    void insertRow(final int rowIndex) {
        if (rowIndex < 0) {
            return;
        }

        final Map<String, Object> map = Arrays.stream(HEADINGS).collect(toUnmodifiableMap(
                heading -> heading,
                heading -> new Object()
        ));
        values.add(rowIndex, map);
        fireTableDataChanged();
    }

    void moveRow(
            final int rowIndexToMoveFrom,
            final int rowIndexToMoveTo) {
        final Map<String, Object> item = values.remove(rowIndexToMoveFrom);
        values.add(rowIndexToMoveFrom + rowIndexToMoveTo, item);

        fireTableDataChanged();
    }

    void deleteRow(final int selectedRow) {
        values.remove(selectedRow);

        fireTableDataChanged();
    }

    @Override
    public String getColumnName(final int column) {
        return HEADINGS[column];
    }

    @Override
    public int getColumnCount() {
        return HEADINGS.length;
    }

    @Override
    public int getRowCount() {
        return values.size();
    }

    @Override
    public Object getValueAt(
            final int rowIndex,
            final int columnIndex) {
        if (rowIndex >= 0 && rowIndex < values.size()) {
            final Map<String, Object> map = values.get(rowIndex);
            return map.get(HEADINGS[columnIndex]);
        }

        return new Object();
    }

    @Override
    public void setValueAt(
            final Object value,
            final int rowIndex,
            final int columnIndex) {
        if (rowIndex >= 0 && rowIndex < values.size()) {
            final Map<String, Object> map = values.get(rowIndex);
            map.put(HEADINGS[columnIndex], value);
        } else {
            final Map<String, Object> map = new HashMap<>();
            map.put(HEADINGS[columnIndex], value);
            values.add(rowIndex, map);
        }

        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(
            final int row,
            final int col) {
        return true;
    }
}
