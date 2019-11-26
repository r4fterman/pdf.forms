/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* This file is part of the PDF Forms Designer
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
* BugReportPanel.java
* ---------------
*/
package org.pdf.forms.gui.windows;

import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.jdesktop.layout.GroupLayout;

public class BugReportPanel extends JPanel implements TableModelListener {

    private final LinkedHashMap filesAndSizes;
    private final JDialog parentDialog;

    private JTable filesToAttach;
    private JLabel sizeOfAttachment;

    public BugReportPanel(
            final LinkedHashMap filesAndSizes,
            final JDialog parentDialog) {
        this.filesAndSizes = filesAndSizes;
        this.parentDialog = parentDialog;

        initComponents();

        filesToAttach.getModel().addTableModelListener(this);

        final DefaultTableCellRenderer tableRenderer = (DefaultTableCellRenderer) filesToAttach.getTableHeader().getDefaultRenderer();
        tableRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        final TableColumn col = filesToAttach.getColumnModel().getColumn(0);
        col.setPreferredWidth(400);

        setTotalSizeOfAttachments();
    }

    private void setTotalSizeOfAttachments() {
        double totalSize = 0;

        final MyTableModel model = (MyTableModel) filesToAttach.getModel();

        final Object[][] data = model.data;

        for (final Object[] row : data) {
            final boolean isChecked = (Boolean) row[2];
            if (isChecked) {
                final double size = (Double) row[1];
                totalSize += size;
            }
        }

        sizeOfAttachment.setText("Total size of attachments: " + totalSize + " Kb");
    }

    private void initComponents() {
        final JLabel jLabel1 = new JLabel();
        final JTextField nameBox = new JTextField();
        final JLabel jLabel2 = new JLabel();
        final JTextField emailBox = new JTextField();
        final JLabel jLabel3 = new JLabel();
        final JScrollPane jScrollPane1 = new JScrollPane();
        final JTextArea reproducedBox = new JTextArea();
        final JScrollPane jScrollPane2 = new JScrollPane();
        filesToAttach = new JTable();
        final JLabel jLabel4 = new JLabel();
        sizeOfAttachment = new JLabel();
        final JButton sendButton = new JButton();
        final JButton cancelButton = new JButton();

        jLabel1.setText("Your Name:");

        jLabel2.setText("Your Email:");

        jLabel3.setText("Steps to reproduce the problem:");

        reproducedBox.setColumns(20);
        reproducedBox.setRows(5);
        jScrollPane1.setViewportView(reproducedBox);

        jScrollPane2.setBorder(null);

        filesToAttach.setModel(new MyTableModel());
        jScrollPane2.setViewportView(filesToAttach);

        jLabel4.setText("Files to attach:");

        sizeOfAttachment.setText("Total size of attachments");

        sendButton.setText("Send");
        sendButton.addActionListener(this::sendClicked);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(this::cancelClicked);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(jScrollPane2, GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                                        .add(jScrollPane1, GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                                        .add(jLabel1)
                                        .add(nameBox, GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                                        .add(jLabel2)
                                        .add(jLabel3)
                                        .add(emailBox, GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                                        .add(jLabel4)
                                        .add(sizeOfAttachment)
                                        .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(cancelButton)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(sendButton)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(nameBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel2, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(emailBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jScrollPane1, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel4)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jScrollPane2, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(sizeOfAttachment)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(sendButton)
                                        .add(cancelButton))
                                .addContainerGap())
        );
    }

    private void sendClicked(final java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void cancelClicked(final java.awt.event.ActionEvent evt) {
        parentDialog.setVisible(false);
    }

    class MyTableModel extends AbstractTableModel {
        private final String[] columnNames = {
                "File",
                "Size (Kb)",
                "Include"
        };
        private final Object[][] data;

        MyTableModel() {
            data = new Object[filesAndSizes.size()][3];

            int count = 0;
            for (final Object o : filesAndSizes.keySet()) {
                final String file = (String) o;
                final Double size = (Double) filesAndSizes.get(file);

                data[count][0] = file;
                data[count][1] = size;
                data[count][2] = Boolean.TRUE;

                count++;
            }
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public String getColumnName(final int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(
                final int row,
                final int col) {
            return data[row][col];
        }

        @Override
        public Class getColumnClass(final int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        @Override
        public boolean isCellEditable(
                final int row,
                final int col) {
            return col == 2;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        @Override
        public void setValueAt(
                final Object value,
                final int row,
                final int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }

    @Override
    public void tableChanged(final TableModelEvent e) {
        //        int row = e.getFirstRow();
        //        int column = e.getColumn();
        //        TableModel model = (TableModel)e.getSource();
        //        String columnName = model.getColumnName(column);
        //        Object data = model.getValueAt(row, column);

        setTotalSizeOfAttachments();

        //        System.out.println(row+" "+column+" "+columnName);
    }

}
