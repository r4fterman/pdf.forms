/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* 	This file is part of the PDF Forms Designer
*
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


*
* ---------------
* ListFieldPanel.java
* ---------------
*/
package org.pdf.forms.gui.properties.object.field;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBox;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBoxParent;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class ListFieldPanel extends javax.swing.JPanel implements TristateCheckBoxParent {

    private Map<IWidget, Element> widgetsAndProperties;
    private IDesigner designerPanel;

    private final String[] HEADINGS = { "Text" };

    private final MyTableModel tableModel;
    private final int type;

    public ListFieldPanel(final int type) {
        this.type = type;
        tableModel = new MyTableModel();

        initComponents();

        allowCustomTextEntryBox.setEnabled(type == IWidget.COMBO_BOX);
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {
        final javax.swing.JLabel jLabel1;
        final javax.swing.JLabel jLabel3;
        final javax.swing.JScrollPane jScrollPane3;

        jLabel1 = new javax.swing.JLabel();
        appearanceBox = new javax.swing.JComboBox<>();
        listLabel = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        itemsTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        presenceBox = new javax.swing.JComboBox<>();
        allowCustomTextEntryBox = new TristateCheckBox("Allow Custom Text Entry", TristateCheckBox.NOT_SELECTED, this);

        jLabel1.setText("Appearance:");
        jLabel1.setEnabled(false);

        appearanceBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Underline", "Solid", "Sunken Box", "Custom..." }));
        appearanceBox.setSelectedIndex(3);
        appearanceBox.setEnabled(false);

        listLabel.setText("List Items:");

        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/pdf/forms/res/plus.gif")));
        addButton.addActionListener(evt -> addRow(evt));

        removeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/pdf/forms/res/Cross.gif")));
        removeButton.addActionListener(evt -> removeRow(evt));

        upButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/pdf/forms/res/Up.gif")));
        upButton.addActionListener(evt -> moveUp(evt));

        downButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/pdf/forms/res/Down.gif")));
        downButton.addActionListener(evt -> moveDown(evt));

        itemsTable.setModel(tableModel);
        jScrollPane3.setViewportView(itemsTable);

        jLabel3.setText("Presence:");

        presenceBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Visible" }));

        allowCustomTextEntryBox.setText("Allow Custom Text Entry");
        allowCustomTextEntryBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        allowCustomTextEntryBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane3, 0, 0, Short.MAX_VALUE)
                                                        .add(layout.createSequentialGroup()
                                                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, listLabel)
                                                                        .add(org.jdesktop.layout.GroupLayout.LEADING, allowCustomTextEntryBox))
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(removeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(upButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                .add(6, 6, 6)
                                                                .add(downButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                                .addContainerGap())
                                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                .add(layout.createSequentialGroup()
                                                        .add(jLabel1)
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                        .add(appearanceBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 198, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                                        .add(jLabel3)
                                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                        .add(presenceBox, 0, 135, Short.MAX_VALUE)
                                                        .add(84, 84, 84)))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel1)
                                        .add(appearanceBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(listLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(downButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(removeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(upButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(allowCustomTextEntryBox)
                                .add(23, 23, 23)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(jLabel3)
                                        .add(presenceBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void moveDown(final java.awt.event.ActionEvent evt) {
        final int selectedRow = itemsTable.getSelectedRow();

        if (selectedRow != -1 && selectedRow != tableModel.getRowCount() - 1) {
            tableModel.moveRow(selectedRow, 1);
        }

        updateItems();
    }

    private void moveUp(final java.awt.event.ActionEvent evt) {
        final int selectedRow = itemsTable.getSelectedRow();

        if (selectedRow > 0) {
            tableModel.moveRow(selectedRow, -1);
        }

        updateItems();
    }

    private void removeRow(final java.awt.event.ActionEvent evt) {
        final int selectedRow = itemsTable.getSelectedRow();

        if (selectedRow != -1) {
            tableModel.deleteRow(selectedRow);
        }

        updateItems();
    }

    private void addRow(final java.awt.event.ActionEvent evt) {
        int selectedRow = itemsTable.getSelectedRow();

        if (selectedRow == -1) {
            selectedRow = tableModel.getRowCount();
        }

        tableModel.insertRow(selectedRow);

        updateItems();
    }

    private void updateItems() {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        if (widgets.size() == 1) {
            final IWidget widget = widgets.iterator().next();

            final Element widgetProperties = widgetsAndProperties.get(widget);

            final Element itemsElement = (Element) widgetProperties.getElementsByTagName("items").item(0);

            final List<Element> items = XMLUtils.getElementsFromNodeList(itemsElement.getChildNodes());

            /* remove all elements from list before re-populating */
            for (final Element element : items) {
                itemsElement.removeChild(element);
            }

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                final String value = (String) tableModel.getValueAt(i, 0);

                if (value != null && !value.equals("")) {
                    XMLUtils.addBasicProperty(widget.getProperties(), "item", value, itemsElement);
                }
            }

            widget.setObjectProperties(widgetProperties);
        }

        designerPanel.repaint();
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        final TristateCheckBox.State allowCustomTextEntryToUse = null;

        if (widgetsAndProperties.size() == 1) {
            setItemsEnabled(true);
        } else {
            setItemsEnabled(false);
        }

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element objectProperties = widgetsAndProperties.get(widget);

            /* add field properties */
            if (widgetsAndProperties.size() == 1) { // only 1 widget is currently selected
                final Element itemElement = (Element) objectProperties.getElementsByTagName("items").item(0);

                final List<Element> itemsList = XMLUtils.getElementsFromNodeList(itemElement.getChildNodes());

                for (int i = 0; i < itemsList.size(); i++) {
                    final Element item = itemsList.get(i);

                    final String value = XMLUtils.getAttributeFromElement(item, "item");

                    tableModel.insertRow(i);
                    tableModel.setValueAt(value, i, 0);
                }
            }

            //            if (type == IWidget.COMBO_BOX) {
            //                boolean allowCustomTextEntry = Boolean.valueOf(XMLUtils.getAttribute(fieldProperties, 2)).booleanValue();
            //
            //                if (allowCustomTextEntryToUse == null) {
            //                    allowCustomTextEntryToUse = allowCustomTextEntry ? TristateCheckBox.SELECTED : TristateCheckBox.NOT_SELECTED;
            //                } else {
            //                    if (allowCustomTextEntryToUse != TristateCheckBox.DONT_CARE) {
            //                        if (allowCustomTextEntryToUse == TristateCheckBox.SELECTED && !allowCustomTextEntry) {
            //                            allowCustomTextEntryToUse = TristateCheckBox.DONT_CARE;
            //                        } else if (allowCustomTextEntryToUse == TristateCheckBox.NOT_SELECTED && allowCustomTextEntry) {
            //                            allowCustomTextEntryToUse = TristateCheckBox.DONT_CARE;
            //                        }
            //                    }
            //                }
            //
            //                ((TristateCheckBox) allowCustomTextEntryBox).setState(allowCustomTextEntryToUse);
            //            }
        }

        tableModel.addTableModelListener(e -> updateItems());
    }

    private void setItemsEnabled(final boolean enabled) {
        itemsTable.setEnabled(enabled);
        listLabel.setEnabled(enabled);
        addButton.setEnabled(enabled);
        removeButton.setEnabled(enabled);
        upButton.setEnabled(enabled);
        downButton.setEnabled(enabled);
    }

    @Override
    public void checkboxClicked(final MouseEvent e) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final TristateCheckBox.State allowCustomTextEntryState = (((TristateCheckBox) allowCustomTextEntryBox).getState());

        for (final IWidget widget : widgets) {
            final List<Element> objectProperties = XMLUtils.getElementsFromNodeList(
                    widgetsAndProperties.get(widget).getChildNodes());

            /* add field properties */
            final List<Element> fieldProperties = XMLUtils.getElementsFromNodeList(
                    objectProperties.get(0).getChildNodes());

            if (allowCustomTextEntryState != TristateCheckBox.DONT_CARE) {
                final Element allowMultipleLines = fieldProperties.get(2);
                allowMultipleLines.getAttributeNode("value").setValue(allowCustomTextEntryState == TristateCheckBox.SELECTED ? "true" : "false");
            }
        }
    }

    private javax.swing.JButton addButton;
    private javax.swing.JCheckBox allowCustomTextEntryBox;
    private javax.swing.JComboBox<String> appearanceBox;
    private javax.swing.JButton downButton;
    private javax.swing.JTable itemsTable;
    private javax.swing.JLabel listLabel;
    private javax.swing.JComboBox<String> presenceBox;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton upButton;

    class MyTableModel extends AbstractTableModel {

        private final List<Map<String, Object>> values = new ArrayList<>();

        private int rows;

        public void insertRow(final int selectedRow) {
            final Map<String, Object> map = new HashMap<>();
            if (selectedRow != -1) {
                for (final String heading : HEADINGS) {
                    map.put(heading, null);
                }

                values.add(selectedRow /*+ 1*/, map);
            }

            rows++;
            fireTableDataChanged();
        }

        public void moveRow(
                final int index,
                final int move) {
            final Map<String, Object> item = values.remove(index);

            values.add(index + move, item);

            fireTableDataChanged();
        }

        public void deleteRow(final int selectedRow) {
            values.remove(selectedRow);

            rows--;
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
            return rows;
        }

        @Override
        public Object getValueAt(
                final int rowIndex,
                final int columnIndex) {
            if (rowIndex < values.size()) {
                final Map<String, Object> map = values.get(rowIndex);

                return map.get(HEADINGS[columnIndex]);
            }

            return null;
        }

        @Override
        public void setValueAt(
                final Object value,
                final int rowIndex,
                final int columnIndex) {
            if (rowIndex < values.size()) {
                final Map<String, Object> map = values.get(rowIndex);
                map.remove(HEADINGS[columnIndex]);
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
}
