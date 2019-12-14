package org.pdf.forms.gui.properties.object.draw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jpedal.examples.simpleviewer.utils.FileFilterer;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class ImageDrawPanel extends JPanel {

    private Map<IWidget, Element> widgetsAndProperties;

    private IDesigner designerPanel;

    private JTextField imageLocationBox;
    private JComboBox<String> sizingBox;

    public ImageDrawPanel() {
        initComponents();
    }

    private void initComponents() {
        final JLabel jLabel1 = new JLabel();
        imageLocationBox = new JTextField();
        final JButton jButton1 = new JButton();
        final JLabel jLabel2 = new JLabel();
        sizingBox = new javax.swing.JComboBox<>();

        jLabel1.setText("Location:");

        imageLocationBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateImageLocation(evt);
            }
        });

        jButton1.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/open.gif")));
        jButton1.addActionListener(this::loadImageFileChooser);

        jLabel2.setText("Sizing:");

        sizingBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Stretch Image To Fit", "Use Image Size" }));
        sizingBox.addActionListener(this::updateSizing);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(jLabel1)
                                        .add(jLabel2))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(GroupLayout.LEADING, false)
                                        .add(layout.createSequentialGroup()
                                                .add(imageLocationBox, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(jButton1, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
                                        .add(sizingBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(jButton1, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                                        .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                                .add(jLabel1)
                                                .add(imageLocationBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .add(14, 14, 14)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(jLabel2)
                                        .add(sizingBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(42, Short.MAX_VALUE))
        );
    }

    private void updateSizing(final ActionEvent evt) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final String sizing = (String) sizingBox.getSelectedItem();

        for (final IWidget widget : widgets) {
            if (sizing != null) {
                final Element widgetProperties = widgetsAndProperties.get(widget);

                final Element sizingElement = XMLUtils.getPropertyElement(widgetProperties, "Sizing").get();

                sizingElement.getAttributeNode("value").setValue(sizing);
            }

            widget.setObjectProperties(widgetsAndProperties.get(widget));
        }

        designerPanel.repaint();
    }

    private void loadImageFileChooser(final ActionEvent evt) {
        final String path = imageLocationBox.getText();
        final JFileChooser chooser = new JFileChooser(path);

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        final String[] images = new String[] {
                "gif", "jpeg", "png" }; // gif jpeg png
        chooser.addChoosableFileFilter(new FileFilterer(images, "Images (gif, jpeg, png)"));

        final int state = chooser.showOpenDialog(null);

        final File fileToOpen = chooser.getSelectedFile();

        if (fileToOpen != null && state == JFileChooser.APPROVE_OPTION) {
            imageLocationBox.setText(fileToOpen.getAbsolutePath());
            updateImageLocation(null);
        }
    }

    private void updateImageLocation(final FocusEvent event) {
        final Set<IWidget> widgets = widgetsAndProperties.keySet();

        final String location = imageLocationBox.getText();

        for (final IWidget widget : widgets) {
            if (location != null && !location.equals("mixed")) {
                final Element widgetProperties = widgetsAndProperties.get(widget);

                final Element locationElement = XMLUtils.getPropertyElement(widgetProperties, "Location").get();

                locationElement.getAttributeNode("value").setValue(location);
            }

            widget.setObjectProperties(widgetsAndProperties.get(widget));
        }

        designerPanel.repaint();
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        String locationToUse = null;
        String sizingToUse = null;

        /* iterate through the widgets */
        for (final IWidget widget : widgetsAndProperties.keySet()) {
            final Element objectProperties = widgetsAndProperties.get(widget);

            /* get draw properties */
            final Element drawProperties = (Element) objectProperties.getElementsByTagName("draw").item(0);

            final String location = XMLUtils.getAttributeFromChildElement(drawProperties, "Location").orElse("top left");
            final String sizing = XMLUtils.getAttributeFromChildElement(drawProperties, "Sizing").orElse("Stretch Image To Fit");

            if (locationToUse == null) { // this must be the first time round
                locationToUse = location;
                sizingToUse = sizing;
            } else { // check for subsequent widgets
                if (!locationToUse.equals(location)) {
                    locationToUse = "mixed";
                }

                if (!sizingToUse.equals(sizing)) {
                    sizingToUse = "mixed";
                }
            }

            imageLocationBox.setText(locationToUse);

            final String value;
            if (sizingToUse.equals("mixed")) {
                value = null;
            } else {
                value = sizingToUse;
            }
            setComboValue(sizingBox, value);
        }
    }

    private void setComboValue(
            final JComboBox comboBox,
            final Object value) {
        final ActionListener listener = comboBox.getActionListeners()[0];
        comboBox.removeActionListener(listener);

        if (value instanceof Integer) {
            comboBox.setSelectedIndex((Integer) value);
        } else {
            comboBox.setSelectedItem(value);
        }

        comboBox.addActionListener(listener);
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

}
