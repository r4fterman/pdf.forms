package org.pdf.forms.gui.properties.object.draw;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;

public class ImageDrawPanel extends JPanel {

    private static final String[] SIZING = {
            "Stretch Image To Fit",
            "Use Image Size"};

    private Set<IWidget> widgets;

    private IDesigner designerPanel;

    private JTextField imageLocationBox;
    private JComboBox<String> sizingBox;

    public ImageDrawPanel() {
        initComponents();
    }

    private void initComponents() {
        final JLabel locationLabel = new JLabel("Location:");

        imageLocationBox = new JTextField();
        imageLocationBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateImageLocation();
            }
        });

        final JButton chooseImageButton = new JButton(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/open.gif")));
        chooseImageButton.addActionListener(this::loadImageFileChooser);

        final JLabel sizingLabel = new JLabel();
        sizingLabel.setText("Sizing:");

        sizingBox = new JComboBox<>(SIZING);
        sizingBox.addActionListener(this::updateSizing);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(locationLabel)
                                        .add(sizingLabel))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.LEADING, false)
                                        .add(layout.createSequentialGroup()
                                                .add(imageLocationBox,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        182,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(chooseImageButton,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        23,
                                                        GroupLayout.PREFERRED_SIZE))
                                        .add(sizingBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(chooseImageButton,
                                                GroupLayout.PREFERRED_SIZE,
                                                20,
                                                GroupLayout.PREFERRED_SIZE)
                                        .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                                .add(locationLabel)
                                                .add(imageLocationBox,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE)))
                                .add(14, 14, 14)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(sizingLabel)
                                        .add(sizingBox,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(42, Short.MAX_VALUE))
        );
    }

    private void updateSizing(final ActionEvent event) {
        if (widgets == null) {
            return;
        }

        final String sizing = (String) sizingBox.getSelectedItem();
        if (sizing != null) {
            widgets.forEach(widget -> widget.getWidgetModel().getProperties().getObject().getDraw().setSizing(sizing));
        }

        designerPanel.repaint();
    }

    private void loadImageFileChooser(final ActionEvent event) {
        final String path = imageLocationBox.getText();
        final JFileChooser chooser = new JFileChooser(path);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.addChoosableFileFilter(new ImageFileFilter());

        final int state = chooser.showOpenDialog(null);
        final File fileToOpen = chooser.getSelectedFile();
        if (fileToOpen != null && state == JFileChooser.APPROVE_OPTION) {
            imageLocationBox.setText(fileToOpen.getAbsolutePath());
            updateImageLocation();
        }
    }

    private void updateImageLocation() {
        if (widgets == null) {
            return;
        }

        final String location = imageLocationBox.getText();
        if (location != null && !location.equals("mixed")) {
            widgets.forEach(widget -> widget.getWidgetModel().getProperties().getObject().getDraw()
                    .setLocation(location));
        }

        designerPanel.repaint();
    }

    public void setProperties(final Set<IWidget> widgets) {
        this.widgets = widgets;

        final String locationToUse = getLocationToUse(widgets);
        imageLocationBox.setText(locationToUse);

        final String sizingToUse = getSizingToUse(widgets);
        if (sizingToUse.equals("mixed")) {
            setComboValue(sizingBox, null);
        } else {
            setComboValue(sizingBox, sizingToUse);
        }
    }

    private String getSizingToUse(final Set<IWidget> widgets) {
        final List<String> sizingValues = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getObject().getDraw().getSizing()
                        .orElse("Stretch Image To Fit"))
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(sizingValues);
    }

    private String getLocationToUse(final Set<IWidget> widgets) {
        final List<String> locationValues = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getObject().getDraw().getLocation()
                        .orElse("top left"))
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(locationValues);
    }

    private void setComboValue(
            final JComboBox<String> comboBox,
            final Object value) {
        final ActionListener[] listeners = comboBox.getActionListeners();
        Arrays.stream(listeners).forEach(comboBox::removeActionListener);

        if (value instanceof Integer) {
            comboBox.setSelectedIndex((Integer) value);
        } else {
            comboBox.setSelectedItem(value);
        }

        Arrays.stream(listeners).forEach(comboBox::addActionListener);
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private String findCommonOrMixedValue(final List<String> values) {
        final boolean listContainsOnlyEqualValues =
                Collections.frequency(values, values.get(0)) == values.size();
        if (listContainsOnlyEqualValues) {
            return values.get(0);
        }
        return "mixed";
    }

}
