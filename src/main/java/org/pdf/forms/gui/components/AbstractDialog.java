package org.pdf.forms.gui.components;

import java.awt.*;
import java.util.Optional;

import javax.swing.*;

public abstract class AbstractDialog extends JDialog {

    private boolean canceled = false;

    public AbstractDialog(final JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public boolean isCanceled() {
        return canceled;
    }

    protected abstract String getDialogTitle();

    protected Optional<String> getDialogDescription() {
        return Optional.empty();
    }

    protected abstract Component createMainPanel();

    protected void buildDialog() {
        setTitle(getDialogTitle());

        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));

        createDescriptionPanel().ifPresent(panel -> contentPane.add(panel, BorderLayout.NORTH));
        contentPane.add(createMainPanel(), BorderLayout.CENTER);
        contentPane.add(createButtonPane(), BorderLayout.SOUTH);

    }

    protected void setDialogSize(
            final int width,
            final int height) {
        setSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));

    }

    private Optional<Component> createDescriptionPanel() {
        return getDialogDescription()
                .map(description -> {
                    final JPanel panel = new JPanel(new BorderLayout());
                    panel.add(new JLabel(description), BorderLayout.CENTER);
                    return panel;
                });
    }

    private Component createButtonPane() {
        final JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> finishDialog());

        final JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancelDialog());

        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        panel.add(cancelButton);
        panel.add(okButton);

        return panel;
    }

    public void finishDialog() {
        this.canceled = false;
        setVisible(false);
        dispose();
    }

    public void cancelDialog() {
        this.canceled = true;
        setVisible(false);
        dispose();
    }
}
