/**
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
* BugReportPanel.java
* ---------------
*/
package org.pdf.forms.gui.windows;

import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JDialog;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

public class BugReportPanel extends javax.swing.JPanel implements TableModelListener {
    
    private LinkedHashMap filesAndSizes;
	private JDialog parentDialog;
    
	/** Creates new form BugReportDialog 
     * @param filesAndSizes 
	 * @param parentDialog */
    public BugReportPanel(LinkedHashMap filesAndSizes, JDialog parentDialog) {
    	this.filesAndSizes = filesAndSizes;
    	this.parentDialog = parentDialog;
    	
        initComponents();
        
        filesToAttach.getModel().addTableModelListener(this);
        
        DefaultTableCellRenderer tableRenderer = (DefaultTableCellRenderer) filesToAttach.getTableHeader().getDefaultRenderer();
		tableRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        TableColumn col = filesToAttach.getColumnModel().getColumn(0);
        col.setPreferredWidth(400);
        
        setTotalSizeOfAttachments();
    }

	private void setTotalSizeOfAttachments() {
		double totalSize = 0;

		MyTableModel model = (MyTableModel) filesToAttach.getModel();

		Object[][] data = model.data;

		for (int i = 0; i < data.length; i++) {
			Object[] row = data[i];

			boolean isChecked = ((Boolean) row[2]).booleanValue();
			if (isChecked) {
				double size = ((Double) row[1]).doubleValue();
				totalSize += size;
			}
		}

		sizeOfAttachment.setText("Total size of attachments: " + totalSize + " Kb");
	}

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        nameBox = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        emailBox = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        reproducedBox = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        filesToAttach = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        sizeOfAttachment = new javax.swing.JLabel();
        sendButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

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
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendClicked(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                    .add(jLabel1)
                    .add(nameBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                    .add(jLabel2)
                    .add(jLabel3)
                    .add(emailBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                    .add(jLabel4)
                    .add(sizeOfAttachment)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(cancelButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(sendButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(nameBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(emailBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sizeOfAttachment)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(sendButton)
                    .add(cancelButton))
                .addContainerGap())
        );
    }

	private void sendClicked(java.awt.event.ActionEvent evt) {
	    // TODO add your handling code here:
	}
	
	private void cancelClicked(java.awt.event.ActionEvent evt) {
	    parentDialog.setVisible(false);
	}
    
    class MyTableModel extends AbstractTableModel {
        private String[] columnNames = {"File", "Size (Kb)", "Include"};
        private Object[][] data;// = {{"File 1", new Boolean(true)}, {"File 2", new Boolean(false)}};
        
        public MyTableModel() {
        	data = new Object[filesAndSizes.size()][3];
        	
        	int count = 0;
        	for (Iterator it = filesAndSizes.keySet().iterator(); it.hasNext();) {
				String file = (String) it.next();
				Double size = (Double) filesAndSizes.get(file);
				
				data[count][0] = file;
				data[count][1] = size;
				data[count][2] = Boolean.TRUE;
				
				count++;
			}
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }
        
        public int getRowCount() {
            return data.length;
        }
        
        public String getColumnName(int col) {
            return columnNames[col];
        }
        
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
        
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
        
        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            if (col == 2) 
                return true;
            
            return false;
        }
        
        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }
    
    public void tableChanged(TableModelEvent e) {
//        int row = e.getFirstRow();
//        int column = e.getColumn();
//        TableModel model = (TableModel)e.getSource();
//        String columnName = model.getColumnName(column);
//        Object data = model.getValueAt(row, column);
        
    	setTotalSizeOfAttachments();
    	
//        System.out.println(row+" "+column+" "+columnName);
    }

    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField emailBox;
    private javax.swing.JTable filesToAttach;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nameBox;
    private javax.swing.JTextArea reproducedBox;
    private javax.swing.JButton sendButton;
    private javax.swing.JLabel sizeOfAttachment;
}
