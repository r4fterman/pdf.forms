package org.pdf.forms.gui.windows;

import java.awt.event.ActionEvent;
import java.util.Map;

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
import org.jdesktop.layout.LayoutStyle;

public class BugReportPanel extends JPanel implements TableModelListener {

    private final Map<String, Double> filesAndSizes;
    private final JDialog parentDialog;

    private JTable filesToAttach;
    private JLabel sizeOfAttachmentLabel;

    public BugReportPanel(
            final Map<String, Double> filesAndSizes,
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

        sizeOfAttachmentLabel.setText("Total size of attachments: " + totalSize + " Kb");
    }

    private void initComponents() {
        final JLabel nameLabel = new JLabel();
        nameLabel.setText("Your Name:");

        final JLabel emailLabel = new JLabel();
        emailLabel.setText("Your Email:");

        final JLabel stepsLabel = new JLabel();
        stepsLabel.setText("Steps to reproduce the problem:");

        final JTextArea reproducedBox = new JTextArea();
        reproducedBox.setColumns(20);
        reproducedBox.setRows(5);

        final JScrollPane reproduceScrollPane = new JScrollPane();
        reproduceScrollPane.setViewportView(reproducedBox);

        final JScrollPane attachmentScrollPane = new JScrollPane();
        attachmentScrollPane.setBorder(null);

        filesToAttach = new JTable();
        filesToAttach.setModel(new MyTableModel());
        attachmentScrollPane.setViewportView(filesToAttach);

        final JLabel attachLabel = new JLabel();
        attachLabel.setText("Files to attach:");

        sizeOfAttachmentLabel = new JLabel();
        sizeOfAttachmentLabel.setText("Total size of attachments");

        final JButton sendButton = new JButton();
        sendButton.setText("Send");
        sendButton.addActionListener(this::sendClicked);

        final JButton cancelButton = new JButton();
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(this::cancelClicked);

        final JTextField nameBox = new JTextField();
        final JTextField emailBox = new JTextField();

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(attachmentScrollPane, GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                                        .add(reproduceScrollPane, GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                                        .add(nameLabel)
                                        .add(nameBox, GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                                        .add(emailLabel)
                                        .add(stepsLabel)
                                        .add(emailBox, GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                                        .add(attachLabel)
                                        .add(sizeOfAttachmentLabel)
                                        .add(GroupLayout.TRAILING, layout.createSequentialGroup()
                                                .add(cancelButton)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(sendButton)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(nameLabel)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(nameBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(emailLabel, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(emailBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(stepsLabel)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(reproduceScrollPane, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(attachLabel)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(attachmentScrollPane, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(sizeOfAttachmentLabel)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(sendButton)
                                        .add(cancelButton))
                                .addContainerGap())
        );
    }

    private void sendClicked(final ActionEvent event) {
        // TODO: implement bug report sending, e.g. via email
    }

    private void cancelClicked(final ActionEvent event) {
        parentDialog.setVisible(false);
    }

    class MyTableModel extends AbstractTableModel {
        private final Map<Integer, Class<?>> columns = Map.of(
                0, String.class,
                1, Double.class,
                2, Boolean.class
        );
        private final Map<Integer, String> columnNames = Map.of(
                0, "File",
                1, "Size (Kb)",
                2, "Include"
        );
        private final Object[][] data;

        MyTableModel() {
            data = new Object[filesAndSizes.size()][3];

            int count = 0;
            for (final Map.Entry<String, Double> entry : filesAndSizes.entrySet()) {
                data[count][0] = entry.getKey();
                data[count][1] = entry.getValue();
                data[count][2] = Boolean.TRUE;

                count++;
            }
        }

        @Override
        public int getColumnCount() {
            return columnNames.size();
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public String getColumnName(final int columnIndex) {
            return columnNames.getOrDefault(columnIndex, "Undefined Column");
        }

        @Override
        public Object getValueAt(
                final int row,
                final int col) {
            return data[row][col];
        }

        @Override
        public Class<?> getColumnClass(final int columnIndex) {
            return columns.getOrDefault(columnIndex, String.class);
        }

        @Override
        public boolean isCellEditable(
                final int row,
                final int col) {
            return col == 2;
        }

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
    public void tableChanged(final TableModelEvent event) {
        setTotalSizeOfAttachments();
    }

}
