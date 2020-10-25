package org.pdf.forms.gui.properties.object.field;

import static java.util.stream.Collectors.toUnmodifiableMap;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBox;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBoxParent;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class ListFieldPanel extends JPanel implements TristateCheckBoxParent {

    private static final String[] HEADINGS = {
            "Text"
    };
    private static final String[] APPEARANCES = {
            "None",
            "Underline",
            "Solid",
            "Sunken Box",
            "Custom..."
    };

    private final MyTableModel tableModel;

    private Map<IWidget, Element> widgetsAndProperties;
    private IDesigner designerPanel;

    private JButton addButton;
    private JCheckBox allowCustomTextEntryBox;
    private JButton downButton;
    private JTable itemsTable;
    private JLabel listLabel;
    private JButton removeButton;
    private JButton upButton;

    public ListFieldPanel(final int type) {
        tableModel = new MyTableModel();

        initComponents();

        allowCustomTextEntryBox.setEnabled(type == IWidget.COMBO_BOX);
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {
        final JLabel appearanceLabel = new JLabel();
        appearanceLabel.setText("Appearance:");
        appearanceLabel.setEnabled(false);

        final JComboBox<String> appearanceBox = new JComboBox<>();
        appearanceBox.setModel(new DefaultComboBoxModel<>(APPEARANCES));
        appearanceBox.setSelectedIndex(3);
        appearanceBox.setEnabled(false);

        listLabel = new JLabel();
        listLabel.setText("List Items:");

        addButton = new JButton();
        addButton.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/plus.gif")));
        addButton.addActionListener(this::addRow);

        removeButton = new JButton();
        removeButton.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Cross.gif")));
        removeButton.addActionListener(this::removeRow);

        upButton = new JButton();
        upButton.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Up.gif")));
        upButton.addActionListener(this::moveUp);

        downButton = new JButton();
        downButton.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Down.gif")));
        downButton.addActionListener(this::moveDown);

        itemsTable = new JTable();
        itemsTable.setModel(tableModel);

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(itemsTable);

        final JLabel presenceLabel = new JLabel();
        presenceLabel.setText("Presence:");

        final JComboBox<String> presenceBox = new JComboBox<>();
        presenceBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Visible" }));

        allowCustomTextEntryBox = new TristateCheckBox("Allow Custom Text Entry", TristateCheckBox.NOT_SELECTED, this);
        allowCustomTextEntryBox.setText("Allow Custom Text Entry");
        allowCustomTextEntryBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        allowCustomTextEntryBox.setMargin(new Insets(0, 0, 0, 0));

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(layout.createParallelGroup(GroupLayout.TRAILING, false)
                                                        .add(GroupLayout.LEADING, scrollPane, 0, 0, Short.MAX_VALUE)
                                                        .add(layout.createSequentialGroup()
                                                                .add(layout.createParallelGroup(GroupLayout.TRAILING)
                                                                        .add(GroupLayout.LEADING, listLabel)
                                                                        .add(GroupLayout.LEADING, allowCustomTextEntryBox))
                                                                .addPreferredGap(LayoutStyle.RELATED)
                                                                .add(addButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.RELATED)
                                                                .add(removeButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.RELATED)
                                                                .add(upButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                                                .add(6, 6, 6)
                                                                .add(downButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
                                                .addContainerGap())
                                        .add(layout.createParallelGroup(GroupLayout.LEADING)
                                                .add(layout.createSequentialGroup()
                                                        .add(appearanceLabel)
                                                        .addPreferredGap(LayoutStyle.RELATED)
                                                        .add(appearanceBox, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE))
                                                .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                                                        .add(presenceLabel)
                                                        .addPreferredGap(LayoutStyle.RELATED)
                                                        .add(presenceBox, 0, 135, Short.MAX_VALUE)
                                                        .add(84, 84, 84)))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(appearanceLabel)
                                        .add(appearanceBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(listLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                        .add(downButton, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                        .add(addButton, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                        .add(removeButton, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                        .add(upButton, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(scrollPane, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(allowCustomTextEntryBox)
                                .add(23, 23, 23)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(presenceLabel)
                                        .add(presenceBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void moveDown(final ActionEvent event) {
        final int selectedRow = itemsTable.getSelectedRow();
        if (selectedRow != -1 && selectedRow != tableModel.getRowCount() - 1) {
            tableModel.moveRow(selectedRow, 1);
        }

        updateItems();
    }

    private void moveUp(final ActionEvent event) {
        final int selectedRow = itemsTable.getSelectedRow();
        if (selectedRow > 0) {
            tableModel.moveRow(selectedRow, -1);
        }

        updateItems();
    }

    private void removeRow(final ActionEvent event) {
        final int selectedRow = itemsTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.deleteRow(selectedRow);
        }

        updateItems();
    }

    private void addRow(final ActionEvent event) {
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
        setItemsEnabled(widgetsAndProperties.size() == 1);

        for (Element objectProperties : widgetsAndProperties.values()) {
            if (widgetsAndProperties.size() == 1) {
                // only 1 widget is currently selected
                final Element itemElement = (Element) objectProperties.getElementsByTagName("items").item(0);

                final List<Element> itemsList = XMLUtils.getElementsFromNodeList(itemElement.getChildNodes());

                for (int i = 0; i < itemsList.size(); i++) {
                    final Element item = itemsList.get(i);

                    final String value = XMLUtils.getAttributeFromElement(item, "item").orElse("");

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
    public void checkboxClicked(final MouseEvent event) {
        final TristateCheckBox.State allowCustomTextEntryState = (((TristateCheckBox) allowCustomTextEntryBox).getState());

        final Set<IWidget> widgets = widgetsAndProperties.keySet();
        for (final IWidget widget : widgets) {
            final List<Element> objectProperties = XMLUtils.getElementsFromNodeList(widgetsAndProperties.get(widget).getChildNodes());
            final List<Element> fieldProperties = XMLUtils.getElementsFromNodeList(objectProperties.get(0).getChildNodes());
            if (allowCustomTextEntryState != TristateCheckBox.DONT_CARE) {
                final boolean value = allowCustomTextEntryState == TristateCheckBox.SELECTED;
                fieldProperties.get(2).getAttributeNode("value").setValue(String.valueOf(value));
            }
        }
    }

    static class MyTableModel extends AbstractTableModel {

        private final List<Map<String, Object>> values = new ArrayList<>();

        private int rows;

        void insertRow(final int selectedRow) {
            if (selectedRow != -1) {
                final Map<String, Object> map = Arrays.stream(HEADINGS).collect(toUnmodifiableMap(
                        heading -> heading,
                        heading -> new Object()
                ));
                values.add(selectedRow /*+ 1*/, map);
            }
            rows++;

            fireTableDataChanged();
        }

        void moveRow(
                final int index,
                final int move) {
            final Map<String, Object> item = values.remove(index);
            values.add(index + move, item);

            fireTableDataChanged();
        }

        void deleteRow(final int selectedRow) {
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
