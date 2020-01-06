package org.pdf.forms.gui.properties.layout;

import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBox;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBoxParent;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.google.common.base.Strings;

public class LayoutPropertiesPanel extends JPanel implements TristateCheckBoxParent {

    private final Logger logger = LoggerFactory.getLogger(LayoutPropertiesPanel.class);

    private final int units = (int) (Rule.INCH / 2.54);

    private IDesigner designerPanel;
    private Map<IWidget, Element> widgetsAndProperties;

    private JComboBox<String> anchorLocationBox;
    private ButtonGroup buttonGroup1;
    private JComboBox<String> captionLocationBox;
    private JTextField heightBox;
    private JTextField widthBox;
    private JTextField xBox;
    private JCheckBox xExpandToFitBox;
    private JTextField yBox;
    private JCheckBox yExpandToFitBox;

    private JToggleButton rotate0;
    private JToggleButton rotate90;
    private JToggleButton rotate180;
    private JToggleButton rotate270;

    LayoutPropertiesPanel() {
        initComponents();
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    private void initComponents() {
        final JPanel marginPanel = buildMarginPanel();
        final JPanel captionPanel = buildCaptionPanel();
        final JPanel sizeAndPositionPanel = buildSizeAndPositionPanel();

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(GroupLayout.TRAILING)
                                        .add(GroupLayout.LEADING, marginPanel, 0, 266, Short.MAX_VALUE)
                                        .add(GroupLayout.LEADING, layout.createParallelGroup(GroupLayout.TRAILING, false)
                                                .add(GroupLayout.LEADING, captionPanel, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                                                .add(GroupLayout.LEADING, sizeAndPositionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .add(142, 142, 142))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .add(sizeAndPositionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(marginPanel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(captionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(106, Short.MAX_VALUE))
        );
    }

    private JPanel buildCaptionPanel() {
        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Caption"));

        final JLabel positionLabel = new JLabel();
        positionLabel.setText("Position:");

        captionLocationBox = new JComboBox<>();
        captionLocationBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Left", "Right", "Top", "Bottom", "None" }));
        captionLocationBox.addActionListener(this::updateCaptionPosition);

        final JLabel reserveLabel = new JLabel();
        reserveLabel.setText("Reserve:");
        reserveLabel.setEnabled(false);

        final JTextField reserveBox = new JTextField();
        reserveBox.setEnabled(false);

        final GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(positionLabel)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(captionLocationBox, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                                .add(22, 22, 22)
                                .add(reserveLabel)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(reserveBox, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.LEADING)
                        .add(groupLayout.createParallelGroup(GroupLayout.BASELINE)
                                .add(positionLabel)
                                .add(reserveLabel)
                                .add(captionLocationBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .add(reserveBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        return panel;
    }

    private JPanel buildMarginPanel() {
        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Margins"));

        final JLabel leftLabel = new JLabel();
        leftLabel.setText("Left:");
        leftLabel.setEnabled(false);

        final JLabel rigthLabel = new JLabel();
        rigthLabel.setText("Right");
        rigthLabel.setEnabled(false);

        final JTextField leftMarginBox = new JTextField();
        leftMarginBox.setEnabled(false);

        final JTextField rightMarginBox = new JTextField();
        rightMarginBox.setEnabled(false);

        final JLabel topLabel = new JLabel();
        topLabel.setText("Top:");
        topLabel.setEnabled(false);

        final JLabel bottomLabel = new JLabel();
        bottomLabel.setText("Bottom:");
        bottomLabel.setEnabled(false);

        final JTextField topMarginBox = new JTextField();
        topMarginBox.setEnabled(false);

        final JTextField bottomMarginBox = new JTextField();
        bottomMarginBox.setEnabled(false);

        final GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(groupLayout.createParallelGroup(GroupLayout.LEADING)
                                        .add(leftLabel)
                                        .add(rigthLabel))
                                .add(26, 26, 26)
                                .add(groupLayout.createParallelGroup(GroupLayout.LEADING)
                                        .add(leftMarginBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                        .add(rightMarginBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
                                .add(29, 29, 29)
                                .add(groupLayout.createParallelGroup(GroupLayout.LEADING)
                                        .add(topLabel)
                                        .add(bottomLabel))
                                .add(19, 19, 19)
                                .add(groupLayout.createParallelGroup(GroupLayout.LEADING)
                                        .add(topMarginBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                        .add(bottomMarginBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(groupLayout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(leftLabel)
                                        .add(leftMarginBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .add(topLabel)
                                        .add(topMarginBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(groupLayout.createParallelGroup(GroupLayout.LEADING)
                                        .add(groupLayout.createParallelGroup(GroupLayout.BASELINE)
                                                .add(bottomLabel)
                                                .add(bottomMarginBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .add(rigthLabel)
                                        .add(rightMarginBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        return panel;
    }

    private JPanel buildSizeAndPositionPanel() {
        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Size & Position"));

        final JLabel xLabel = new JLabel();
        xLabel.setText("X:");

        final JLabel widthLabel = new JLabel();
        widthLabel.setText("Width:");

        xBox = new JTextField();
        xBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateSizeAndPosition();
            }
        });

        widthBox = new JTextField();
        widthBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateSizeAndPosition();
            }
        });

        xExpandToFitBox = new TristateCheckBox("Expand to fit", TristateCheckBox.NOT_SELECTED, this);
        xExpandToFitBox.setText("Expand to fit");
        xExpandToFitBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xExpandToFitBox.setEnabled(false);
        xExpandToFitBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        final JLabel yLabel = new JLabel();
        yLabel.setText("Y:");

        final JLabel heightLabel = new JLabel();
        heightLabel.setText("Height:");

        heightBox = new JTextField();
        heightBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateSizeAndPosition();
            }
        });

        yBox = new JTextField();
        yBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateSizeAndPosition();
            }
        });

        yExpandToFitBox = new TristateCheckBox("Expand to fit", TristateCheckBox.NOT_SELECTED, this);
        yExpandToFitBox.setText("Expand to fit");
        yExpandToFitBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        yExpandToFitBox.setEnabled(false);
        yExpandToFitBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        final JLabel anchorLabel = new JLabel();
        anchorLabel.setText("Anchor:");
        anchorLabel.setEnabled(false);

        anchorLocationBox = new JComboBox<>();
        anchorLocationBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Top Left", "Top Middle", "Top Right", "Middle Left", "Center", "Middle Right", "Middle Right", "Bottom Middle", "Bottom Right" }));
        anchorLocationBox.setEnabled(false);
        anchorLocationBox.addActionListener(this::updateAnchor);

        buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(rotate0);

        rotate0 = new JToggleButton();
        rotate0.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Anchor Rotation 0.png")));
        rotate0.setEnabled(false);
        rotate0.setName("0");
        rotate0.addActionListener(this::updateRotation);

        buttonGroup1.add(rotate90);
        rotate90 = new JToggleButton();
        rotate90.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Anchor Rotation 90.png")));
        rotate90.setEnabled(false);
        rotate90.setName("90");
        rotate90.addActionListener(this::updateRotation);

        buttonGroup1.add(rotate180);
        rotate180 = new JToggleButton();
        rotate180.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Anchor Rotation 180.png")));
        rotate180.setEnabled(false);
        rotate180.setName("180");
        rotate180.addActionListener(this::updateRotation);

        buttonGroup1.add(rotate270);
        rotate270 = new JToggleButton();
        rotate270.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Anchor Rotation 270.png")));
        rotate270.setEnabled(false);
        rotate270.setName("270");
        rotate270.addActionListener(this::updateRotation);

        final GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(groupLayout.createParallelGroup(GroupLayout.LEADING)
                                        .add(groupLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(xExpandToFitBox))
                                        .add(groupLayout.createSequentialGroup()
                                                .add(groupLayout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(widthLabel)
                                                        .add(xLabel, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(groupLayout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(xBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                        .add(widthBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))))
                                .add(25, 25, 25)
                                .add(groupLayout.createParallelGroup(GroupLayout.LEADING)
                                        .add(groupLayout.createSequentialGroup()
                                                .add(groupLayout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(heightLabel)
                                                        .add(yLabel, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(groupLayout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(yBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                        .add(heightBox, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)))
                                        .add(groupLayout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(yExpandToFitBox))))
                        .add(groupLayout.createSequentialGroup()
                                .add(anchorLabel)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(anchorLocationBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(rotate0, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(rotate90, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(rotate180, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(rotate270, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(groupLayout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(xLabel)
                                        .add(yLabel)
                                        .add(xBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .add(yBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(groupLayout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(widthLabel)
                                        .add(heightLabel)
                                        .add(widthBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .add(heightBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(groupLayout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(xExpandToFitBox)
                                        .add(yExpandToFitBox))
                                .addPreferredGap(LayoutStyle.RELATED, 15, Short.MAX_VALUE)
                                .add(groupLayout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(anchorLabel)
                                        .add(anchorLocationBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .add(rotate0, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .add(rotate90, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .add(rotate180, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                                        .add(rotate270, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
        );
        return panel;
    }

    private void updateCaptionPosition(final ActionEvent evt) {
        final Object captionPosition = captionLocationBox.getSelectedItem();
        if (captionPosition != null) {
            widgetsAndProperties.entrySet().stream()
                    .filter(entry -> entry.getKey().isComponentSplit())
                    .forEach(entry -> {
                        final Element widgetProperties = entry.getValue();
                        final Element captionPositionElement = XMLUtils.getPropertyElement(widgetProperties, "Position").get();
                        captionPositionElement.getAttributeNode("value").setValue(captionPosition.toString());
                        entry.getKey().setLayoutProperties(widgetProperties);
                    });
            designerPanel.repaint();
        }

    }

    private void updateRotation(final ActionEvent evt) {
        final String alignment = ((JComponent) evt.getSource()).getName();

        widgetsAndProperties.forEach((key, widgetProperties) -> {
            final Element rotationElement = XMLUtils.getPropertyElement(widgetProperties, "Rotation").get();
            rotationElement.getAttributeNode("value").setValue(alignment);
        });
    }

    private void updateAnchor(final ActionEvent evt) {
        final Object anchor = anchorLocationBox.getSelectedItem();
        if (anchor != null) {
            widgetsAndProperties.forEach((key, widgetProperties) -> {
                final Element anchorElement = XMLUtils.getPropertyElement(widgetProperties, "Anchor").get();
                anchorElement.getAttributeNode("value").setValue(anchor.toString());
            });
        }

    }

    private void updateSizeAndPosition() {
        final Integer[] props = new Integer[4];

        if (!xBox.getText().equals("mixed")) {
            final String xText = xBox.getText().replaceAll("cm", "");
            double x = asDouble(xText);
            xBox.setText(x + " cm");

            x = x * units;

            props[0] = (int) (Math.round(x) + IMainFrame.INSET);
        }

        if (!yBox.getText().equals("mixed")) {
            final String yText = yBox.getText().replaceAll("cm", "");
            double y = asDouble(yText);
            yBox.setText(y + " cm");

            y = y * units;

            props[1] = (int) (Math.round(y) + IMainFrame.INSET);
        }

        if (!widthBox.getText().equals("mixed")) {
            final String widthText = widthBox.getText().replaceAll("cm", "");
            double width = asDouble(widthText);
            widthBox.setText(width + " cm");

            width = width * units;

            props[2] = (int) Math.round(width);
        }

        if (!heightBox.getText().equals("mixed")) {
            final String heightText = heightBox.getText().replaceAll("cm", "");
            double height = asDouble(heightText);
            heightBox.setText(height + " cm");

            height = height * units;

            props[3] = (int) Math.round(height);
        }

        if (widgetsAndProperties != null) {
            final Set<IWidget> widgets = widgetsAndProperties.keySet();

            PropertyChanger.updateSizeAndPosition(widgets, props);

            for (final IWidget widget : widgets) {
                widget.setLayoutProperties(widgetsAndProperties.get(widget));
            }
        }

        if (designerPanel != null) {
            designerPanel.repaint();
        }
    }

    @Override
    public void checkboxClicked(final MouseEvent event) {
        final TristateCheckBox.State xExpandState = (((TristateCheckBox) xExpandToFitBox).getState());
        final TristateCheckBox.State yExpandState = (((TristateCheckBox) yExpandToFitBox).getState());

        widgetsAndProperties.forEach((key, widgetProperties) -> {
            final List<Element> layoutProperties = XMLUtils.getElementsFromNodeList(widgetProperties.getChildNodes());

            /* add size & position properties */
            final List<Element> sizeAndPosition = XMLUtils.getElementsFromNodeList(layoutProperties.get(0).getChildNodes());

            if (xExpandState != TristateCheckBox.DONT_CARE) {
                final Element xExpand = sizeAndPosition.get(4);
                final String value;
                if (xExpandState == TristateCheckBox.SELECTED) {
                    value = "true";
                } else {
                    value = "false";
                }
                xExpand.getAttributeNode("value").setValue(value);
            }

            if (yExpandState != TristateCheckBox.DONT_CARE) {
                final Element yExpand = sizeAndPosition.get(5);
                final String value;
                if (yExpandState == TristateCheckBox.SELECTED) {
                    value = "true";
                } else {
                    value = "false";
                }
                yExpand.getAttributeNode("value").setValue(value);
            }
        });
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        String xCordToUse = null;
        String yCordToUse = null;
        String widthToUse = null;
        String heightToUse = null;
        //        TristateCheckBox.State xExpandStateToUse = null, yExpandStateToUse = null;
        String anchorLocationToUse = null;
        String rotationToUse = null;
        String captionPositionToUse = null;

        boolean isComponentSplit = false;

        for (final IWidget widget : widgetsAndProperties.keySet()) {
            if (widget.isComponentSplit()) {
                isComponentSplit = true;
                break;
            }
        }

        /* set the caption alignment box */
        captionLocationBox.setEnabled(isComponentSplit);

        for (Map.Entry<IWidget, Element> entry : widgetsAndProperties.entrySet()) {
            final IWidget widget = entry.getKey();
            final Element props = entry.getValue();

            /* add size & position properties */
            final Element sizeAndPosition = (Element) props.getElementsByTagName("sizeandposition").item(0);

            final String xCord = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "X").get();
            final String width = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Width").get();
            final String yCord = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Y").get();
            final String height = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Height").get();

            //            boolean xExpand = Boolean.valueOf(XMLUtils.getAttribute(sizeAndPosition, 4)).booleanValue();
            //            boolean yExpand = Boolean.valueOf(XMLUtils.getAttribute(sizeAndPosition, 5)).booleanValue();

            final String anchor = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Anchor").get();
            final String rotation = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Rotation").get();

            /* add caption properties */
            String captionPosition = null;
            if (widget.isComponentSplit()) {
                final Element caption = (Element) props.getElementsByTagName("caption").item(0);
                captionPosition = XMLUtils.getAttributeFromChildElement(caption, "Position").get();
            }

            if (captionPositionToUse == null) {
                captionPositionToUse = captionPosition;
            } else {
                if (widget.isComponentSplit() && !captionPositionToUse.equals(captionPosition)) {
                    captionPositionToUse = "mixed";
                }
            }

            if (xCordToUse == null) { // this must be the first time round
                xCordToUse = xCord;
                yCordToUse = yCord;
                widthToUse = width;
                heightToUse = height;

                //                xExpandStateToUse = xExpand ? TristateCheckBox.SELECTED : TristateCheckBox.NOT_SELECTED;
                //                yExpandStateToUse = yExpand ? TristateCheckBox.SELECTED : TristateCheckBox.NOT_SELECTED;

                anchorLocationToUse = anchor;
                rotationToUse = rotation;

            } else { // check for subsequent widgets

                if (!xCordToUse.equals(xCord)) {
                    xCordToUse = "mixed";
                }

                if (!yCordToUse.equals(yCord)) {
                    yCordToUse = "mixed";
                }

                if (!widthToUse.equals(width)) {
                    widthToUse = "mixed";
                }

                if (!heightToUse.equals(height)) {
                    heightToUse = "mixed";
                }

                //                if (xExpandStateToUse != TristateCheckBox.DONT_CARE) {
                //                    if (xExpandStateToUse == TristateCheckBox.SELECTED && !xExpand) {
                //                        xExpandStateToUse = TristateCheckBox.DONT_CARE;
                //                    } else if (xExpandStateToUse == TristateCheckBox.NOT_SELECTED && xExpand) {
                //                        xExpandStateToUse = TristateCheckBox.DONT_CARE;
                //                    }
                //                }

                //                if (yExpandStateToUse != TristateCheckBox.DONT_CARE) {
                //                    if (yExpandStateToUse == TristateCheckBox.SELECTED && !yExpand) {
                //                        yExpandStateToUse = TristateCheckBox.DONT_CARE;
                //                    } else if (yExpandStateToUse == TristateCheckBox.NOT_SELECTED && yExpand) {
                //                        yExpandStateToUse = TristateCheckBox.DONT_CARE;
                //                    }
                //                }

                if (!anchorLocationToUse.equals(anchor)) {
                    anchorLocationToUse = "mixed";
                }

                if (!rotationToUse.equals(rotation)) {
                    rotationToUse = "mixed";
                }

            }
        }

        if ("mixed".equals(xCordToUse)) {
            xBox.setText("mixed");
        } else {
            double x = asInteger(xCordToUse);
            x = round((x - IMainFrame.INSET) / units);
            xBox.setText(x + " cm");
        }

        if ("mixed".equals(yCordToUse)) {
            yBox.setText("mixed");
        } else {
            double y = asInteger(yCordToUse);
            y = round((y - IMainFrame.INSET) / units);
            yBox.setText(y + " cm");
        }

        if ("mixed".equals(widthToUse)) {
            widthBox.setText("mixed");
        } else {
            double width = asInteger(widthToUse);
            width = round(width / units);
            widthBox.setText(width + " cm");
        }

        if ("mixed".equals(heightToUse)) {
            heightBox.setText("mixed");
        } else {
            double height = asInteger(heightToUse);
            height = round(height / units);
            heightBox.setText(height + " cm");
        }

        //        ((TristateCheckBox) xExpandToFitBox).setState(xExpandStateToUse);
        //        ((TristateCheckBox) yExpandToFitBox).setState(yExpandStateToUse);

        final Object selectedItem1;
        if ("mixed".equals(anchorLocationToUse)) {
            selectedItem1 = null;
        } else {
            selectedItem1 = anchorLocationToUse;
        }
        anchorLocationBox.setSelectedItem(selectedItem1);

        if ("mixed".equals(rotationToUse)) {
            buttonGroup1.setSelected(new JToggleButton("").getModel(), true);
        } else if ("0".equals(rotationToUse)) {
            rotate0.setSelected(true);
        } else if ("90".equals(rotationToUse)) {
            rotate90.setSelected(true);
        } else if ("180".equals(rotationToUse)) {
            rotate180.setSelected(true);
        } else if ("270".equals(rotationToUse)) {
            rotate270.setSelected(true);
        } else {
            logger.warn("Unexpected rotation to use {}", rotationToUse);
        }

        if (isComponentSplit) {
            final Object selectedItem;
            if ("mixed".equals(captionPositionToUse)) {
                selectedItem = null;
            } else {
                selectedItem = captionPositionToUse;
            }
            captionLocationBox.setSelectedItem(selectedItem);
        } else {
            captionLocationBox.setSelectedItem(null);
        }

        //        /** add margin properties */
        //        List margins = propertiesSet.item(1).getChildNodes();
        //
        //        String left = XMLUtils.getAttribute(margins, 0);
        //        String right = XMLUtils.getAttribute(margins, 1);
        //        String top = XMLUtils.getAttribute(margins, 2);
        //        String bottom = XMLUtils.getAttribute(margins, 3);
        //
        //        leftMargingBox.setText(left);
        //        rightMarginBox.setText(right);
        //        topMarginBox.setText(top);
        //        bottomMarginBox.setText(bottom);
        //
        //        /** add caption properties */
        //        List caption = propertiesSet.item(2).getChildNodes();
        //
        //        String position = XMLUtils.getAttribute(caption, 0);
        //        String reserve = XMLUtils.getAttribute(caption, 1);
        //
        //        captionLocationBox.setSelectedItem(position);
        //        reserveBox.setText(reserve);
    }

    private double asDouble(final String doubleNumber) {
        if (Strings.isNullOrEmpty(doubleNumber)) {
            return 0d;
        }
        return Double.parseDouble(doubleNumber);
    }

    private double asInteger(final String integerNumber) {
        if (Strings.isNullOrEmpty(integerNumber)) {
            return 0d;
        }
        return Integer.parseInt(integerNumber);
    }

    private double round(final double number) {
        final double exponential = Math.pow(10, 3);

        double value = number;
        value *= exponential;
        value = Math.round(value);
        value /= exponential;

        return number;
    }

}
