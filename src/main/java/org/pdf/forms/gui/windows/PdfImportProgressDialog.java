package org.pdf.forms.gui.windows;

import static javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.ActionEvent;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

public class PdfImportProgressDialog extends JDialog {

    private int noOfPages;
    private boolean isCancelled = false;

    private JLabel infoText;
    private JProgressBar progressBar;
    private JButton stopButton;

    public PdfImportProgressDialog(final JFrame frame) {
        super(frame);
        initComponents();
    }

    private void initComponents() {

        final JLabel jLabel1 = new JLabel();
        infoText = new JLabel();
        progressBar = new JProgressBar();
        stopButton = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/animation.gif"))); // NOI18N

        infoText.setText("Importing PDF page 1 of 10:");

        stopButton.setText("Stop");
        stopButton.addActionListener(this::stopButtonClicked);

        final GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 424, Short.MAX_VALUE)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                                                .addGap(10, 10, 10)
                                                .addComponent(stopButton))
                                        .addComponent(infoText, GroupLayout.Alignment.LEADING))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 179, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(infoText)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(stopButton))
                                .addContainerGap())
        );

        pack();
    }

    private void stopButtonClicked(final ActionEvent evt) {
        stopButton.setEnabled(false);
        isCancelled = true;
    }

    public void setStatusMaximum(final int max) {
        noOfPages = max;
        progressBar.setMaximum(max);
    }

    public void setProgress(final int progress) {
        progressBar.setValue(progress);
        infoText.setText("Importing PDF page " + (progress + 1) + " of " + noOfPages + ":");
    }

    public boolean isCancelled() {
        return isCancelled;
    }

}
