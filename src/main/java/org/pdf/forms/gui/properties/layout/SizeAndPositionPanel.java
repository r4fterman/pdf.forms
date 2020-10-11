package org.pdf.forms.gui.properties.layout;

import static org.jdesktop.layout.GroupLayout.BASELINE;
import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.LayoutStyle.RELATED;
import static org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBox.NOT_SELECTED;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Strings;
import org.jdesktop.layout.GroupLayout;
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

public class SizeAndPositionPanel extends JPanel implements TristateCheckBoxParent {

    private static final String[] ANCHORS = {"Top Left", "Top Middle", "Top Right", "Middle Left", "Center", "Middle Right", "Middle Right", "Bottom Middle", "Bottom Right"};
    private static final String EXPAND_TO_FIT = "Expand to fit";
    private static final int UNITS = (int) (Rule.INCH / 2.54);

    private final Logger logger = LoggerFactory.getLogger(SizeAndPositionPanel.class);
    private final IDesigner designerPanel;

    private JTextField xBox;
    private JTextField widthBox;
    private TristateCheckBox xExpandToFitBox;
    private JTextField heightBox;
    private JTextField yBox;
    private TristateCheckBox yExpandToFitBox;
    private JComboBox<String> anchorLocationBox;

    private JToggleButton rotate0Degree;
    private JToggleButton rotate90Degree;
    private JToggleButton rotate180Degree;
    private JToggleButton rotate270Degree;
    private ButtonGroup buttonGroup;
    private Map<IWidget, Element> widgetsAndProperties;

    public SizeAndPositionPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;

        initializePanel();
    }

    private void initializePanel() {
        setBorder(BorderFactory.createTitledBorder("Size & Position"));

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

        xExpandToFitBox = new TristateCheckBox(EXPAND_TO_FIT, NOT_SELECTED, this);
        //xExpandToFitBox.setText(EXPAND_TO_FIT);
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

        yExpandToFitBox = new TristateCheckBox(EXPAND_TO_FIT, NOT_SELECTED, this);
        //yExpandToFitBox.setText(EXPAND_TO_FIT);
        yExpandToFitBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        yExpandToFitBox.setEnabled(false);
        yExpandToFitBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        final JLabel anchorLabel = new JLabel();
        anchorLabel.setText("Anchor:");
        anchorLabel.setEnabled(false);

        anchorLocationBox = new JComboBox<>();
        anchorLocationBox.setModel(new DefaultComboBoxModel<>(ANCHORS));
        anchorLocationBox.setEnabled(false);
        anchorLocationBox.addActionListener(this::updateAnchor);

        rotate0Degree = createRotate0DegreeButton();
        rotate90Degree = createRotate90DegreeButton();
        rotate180Degree = createRotate180DegreeButton();
        rotate270Degree = createRotate270DegreeButton();

        buttonGroup = new ButtonGroup();
        buttonGroup.add(rotate0Degree);
        buttonGroup.add(rotate90Degree);
        buttonGroup.add(rotate180Degree);
        buttonGroup.add(rotate270Degree);

        final org.jdesktop.layout.GroupLayout groupLayout = new org.jdesktop.layout.GroupLayout(this);
        setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(groupLayout.createParallelGroup(LEADING)
                                        .add(groupLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(xExpandToFitBox))
                                        .add(createSequentialGroup(xLabel, widthLabel, xBox, widthBox, groupLayout)))
                                .add(25, 25, 25)
                                .add(groupLayout.createParallelGroup(LEADING)
                                        .add(createSequentialGroup(yLabel, heightLabel, yBox, heightBox, groupLayout))
                                        .add(groupLayout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(yExpandToFitBox))))
                        .add(groupLayout.createSequentialGroup()
                                .add(anchorLabel)
                                .addPreferredGap(RELATED)
                                .add(anchorLocationBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addPreferredGap(RELATED)
                                .add(rotate0Degree, PREFERRED_SIZE, 23, PREFERRED_SIZE)
                                .addPreferredGap(RELATED)
                                .add(rotate90Degree, PREFERRED_SIZE, 23, PREFERRED_SIZE)
                                .addPreferredGap(RELATED)
                                .add(rotate180Degree, PREFERRED_SIZE, 23, PREFERRED_SIZE)
                                .addPreferredGap(RELATED)
                                .add(rotate270Degree, PREFERRED_SIZE, 23, PREFERRED_SIZE))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(createParallelGroup(xLabel, yLabel, xBox, yBox, groupLayout))
                                .addPreferredGap(RELATED)
                                .add(createParallelGroup(widthLabel, heightLabel, widthBox, heightBox, groupLayout))
                                .addPreferredGap(RELATED)
                                .add(groupLayout.createParallelGroup(BASELINE)
                                        .add(xExpandToFitBox)
                                        .add(yExpandToFitBox))
                                .addPreferredGap(RELATED, 15, Short.MAX_VALUE)
                                .add(groupLayout.createParallelGroup(BASELINE)
                                        .add(anchorLabel)
                                        .add(anchorLocationBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .add(rotate0Degree, PREFERRED_SIZE, 23, PREFERRED_SIZE)
                                        .add(rotate90Degree, PREFERRED_SIZE, 23, PREFERRED_SIZE)
                                        .add(rotate180Degree, PREFERRED_SIZE, 23, PREFERRED_SIZE)
                                        .add(rotate270Degree, PREFERRED_SIZE, 23, PREFERRED_SIZE)))
        );
    }

    private JToggleButton createRotate270DegreeButton() {
        final JToggleButton rotateButton = new JToggleButton();
        rotateButton.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Anchor Rotation 270.png")));
        rotateButton.setEnabled(false);
        rotateButton.setName("270");
        rotateButton.addActionListener(this::updateRotation);
        return rotateButton;
    }

    private JToggleButton createRotate180DegreeButton() {
        final JToggleButton rotateButton = new JToggleButton();
        rotateButton.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Anchor Rotation 180.png")));
        rotateButton.setEnabled(false);
        rotateButton.setName("180");
        rotateButton.addActionListener(this::updateRotation);
        return rotateButton;
    }

    private JToggleButton createRotate90DegreeButton() {
        final JToggleButton rotateButton = new JToggleButton();
        rotateButton.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Anchor Rotation 90.png")));
        rotateButton.setEnabled(false);
        rotateButton.setName("90");
        rotateButton.addActionListener(this::updateRotation);
        return rotateButton;
    }

    private JToggleButton createRotate0DegreeButton() {
        final JToggleButton rotateButton = new JToggleButton();
        rotateButton.setIcon(new ImageIcon(getClass().getResource("/org/pdf/forms/res/Anchor Rotation 0.png")));
        rotateButton.setEnabled(false);
        rotateButton.setName("0");
        rotateButton.addActionListener(this::updateRotation);
        return rotateButton;
    }

    private GroupLayout.SequentialGroup createSequentialGroup(
            final JLabel labelForTextFieldA,
            final JLabel labelForTextFieldB,
            final JTextField textFieldA,
            final JTextField textFieldB,
            final GroupLayout groupLayout) {
        return groupLayout.createSequentialGroup()
                .add(groupLayout.createParallelGroup(LEADING)
                        .add(labelForTextFieldB)
                        .add(labelForTextFieldA, PREFERRED_SIZE, 50, PREFERRED_SIZE))
                .addPreferredGap(RELATED)
                .add(groupLayout.createParallelGroup(LEADING)
                        .add(textFieldA, PREFERRED_SIZE, 55, PREFERRED_SIZE)
                        .add(textFieldB, PREFERRED_SIZE, 55, PREFERRED_SIZE));
    }

    private GroupLayout.ParallelGroup createParallelGroup(
            final JLabel labelForTextFieldA,
            final JLabel labelForTextFieldB,
            final JTextField textFieldA,
            final JTextField textFieldB,
            final GroupLayout groupLayout) {
        return groupLayout.createParallelGroup(BASELINE)
                .add(labelForTextFieldA)
                .add(labelForTextFieldB)
                .add(textFieldA, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                .add(textFieldB, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);
    }

    private void updateAnchor(final ActionEvent e) {
        final Object anchor = anchorLocationBox.getSelectedItem();
        if (anchor != null) {
            widgetsAndProperties.forEach((key, widgetProperties) -> {
                final Element anchorElement = XMLUtils.getPropertyElement(widgetProperties, "Anchor").get();
                anchorElement.getAttributeNode("value").setValue(anchor.toString());
            });
        }
    }

    private void updateRotation(final ActionEvent e) {
        final String alignment = ((JComponent) e.getSource()).getName();
        widgetsAndProperties.forEach((key, widgetProperties) -> {
            final Element rotationElement = XMLUtils.getPropertyElement(widgetProperties, "Rotation").get();
            rotationElement.getAttributeNode("value").setValue(alignment);
        });
    }

    private void updateSizeAndPosition() {
        final Integer[] props = new Integer[4];

        if (!xBox.getText().equals("mixed")) {
            final String xText = xBox.getText().replace("cm", "");
            double x = asDouble(xText);
            xBox.setText(x + " cm");

            x = x * UNITS;

            props[0] = (int) (Math.round(x) + IMainFrame.INSET);
        }

        if (!yBox.getText().equals("mixed")) {
            final String yText = yBox.getText().replace("cm", "");
            double y = asDouble(yText);
            yBox.setText(y + " cm");

            y = y * UNITS;

            props[1] = (int) (Math.round(y) + IMainFrame.INSET);
        }

        if (!widthBox.getText().equals("mixed")) {
            final String widthText = widthBox.getText().replace("cm", "");
            double width = asDouble(widthText);
            widthBox.setText(width + " cm");

            width = width * UNITS;

            props[2] = (int) Math.round(width);
        }

        if (!heightBox.getText().equals("mixed")) {
            final String heightText = heightBox.getText().replace("cm", "");
            double height = asDouble(heightText);
            heightBox.setText(height + " cm");

            height = height * UNITS;

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

    public JTextField getXBox() {
        return xBox;
    }

    public JTextField getWidthBox() {
        return widthBox;
    }

    public JTextField getHeightBox() {
        return heightBox;
    }

    public JTextField getYBox() {
        return yBox;
    }

    public JComboBox<String> getAnchorLocationBox() {
        return anchorLocationBox;
    }

    public JToggleButton getRotate0Degree() {
        return rotate0Degree;
    }

    public JToggleButton getRotate90Degree() {
        return rotate90Degree;
    }

    public JToggleButton getRotate180Degree() {
        return rotate180Degree;
    }

    public JToggleButton getRotate270Degree() {
        return rotate270Degree;
    }

    public TristateCheckBox getXExpandToFitBox() {
        return xExpandToFitBox;
    }

    public TristateCheckBox getYExpandToFitBox() {
        return yExpandToFitBox;
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        String xCordToUse = null;
        String yCordToUse = null;
        String widthToUse = null;
        String heightToUse = null;
        String anchorLocationToUse = null;
        String rotationToUse = null;

        for (final Map.Entry<IWidget, Element> entry : widgetsAndProperties.entrySet()) {
            final IWidget widget = entry.getKey();
            final Element props = entry.getValue();

            /* add size & position properties */
            final Element sizeAndPosition = (Element) props.getElementsByTagName("sizeandposition").item(0);

            final String xCord = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "X").get();
            final String width = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Width").get();
            final String yCord = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Y").get();
            final String height = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Height").get();
            final String anchor = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Anchor").get();
            final String rotation = XMLUtils.getAttributeFromChildElement(sizeAndPosition, "Rotation").get();

            if (xCordToUse == null) {
                // this must be the first time round
                xCordToUse = xCord;
                yCordToUse = yCord;
                widthToUse = width;
                heightToUse = height;
                anchorLocationToUse = anchor;
                rotationToUse = rotation;
            } else {
                // check for subsequent widgets
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

                if (!anchorLocationToUse.equals(anchor)) {
                    anchorLocationToUse = "mixed";
                }

                if (!rotationToUse.equals(rotation)) {
                    rotationToUse = "mixed";
                }
            }
        }
        setProperties(xCordToUse, yCordToUse, widthToUse, heightToUse, anchorLocationToUse, rotationToUse);
    }

    private void setProperties(
            final String xCordToUse,
            final String yCordToUse,
            final String widthToUse,
            final String heightToUse,
            final String anchorLocationToUse,
            final String rotationToUse) {
        if ("mixed".equals(xCordToUse)) {
            xBox.setText("mixed");
        } else {
            double x = asInteger(xCordToUse);
            x = round((x - IMainFrame.INSET) / UNITS);
            xBox.setText(x + " cm");
        }

        if ("mixed".equals(yCordToUse)) {
            yBox.setText("mixed");
        } else {
            double y = asInteger(yCordToUse);
            y = round((y - IMainFrame.INSET) / UNITS);
            yBox.setText(y + " cm");
        }

        if ("mixed".equals(widthToUse)) {
            widthBox.setText("mixed");
        } else {
            double width = asInteger(widthToUse);
            width = round(width / UNITS);
            widthBox.setText(width + " cm");
        }

        if ("mixed".equals(heightToUse)) {
            heightBox.setText("mixed");
        } else {
            double height = asInteger(heightToUse);
            height = round(height / UNITS);
            heightBox.setText(height + " cm");
        }

        final Object anchorSelection;
        if ("mixed".equals(anchorLocationToUse)) {
            anchorSelection = null;
        } else {
            anchorSelection = anchorLocationToUse;
        }
        anchorLocationBox.setSelectedItem(anchorSelection);

        if ("mixed".equals(rotationToUse)) {
            buttonGroup.setSelected(new JToggleButton("").getModel(), true);
        } else if ("0".equals(rotationToUse)) {
            rotate0Degree.setSelected(true);
        } else if ("90".equals(rotationToUse)) {
            rotate90Degree.setSelected(true);
        } else if ("180".equals(rotationToUse)) {
            rotate180Degree.setSelected(true);
        } else if ("270".equals(rotationToUse)) {
            rotate270Degree.setSelected(true);
        } else {
            logger.warn("Unexpected rotation to use {}", rotationToUse);
        }
    }

    private double asInteger(final String integerNumber) {
        if (Strings.isNullOrEmpty(integerNumber)) {
            return 0d;
        }
        return Integer.parseInt(integerNumber);
    }

    private double asDouble(final String doubleNumber) {
        if (Strings.isNullOrEmpty(doubleNumber)) {
            return 0d;
        }
        return Double.parseDouble(doubleNumber);
    }

    private double round(final double number) {
        final double exponential = Math.pow(10, 3);

        double value = number;
        value *= exponential;
        value = Math.round(value);
        value /= exponential;

        return number;
    }

    @Override
    public void checkboxClicked(final MouseEvent e) {
        final TristateCheckBox.State xExpandState = getXExpandToFitBox().getState();
        final TristateCheckBox.State yExpandState = getYExpandToFitBox().getState();

        widgetsAndProperties.forEach((key, widgetProperties) -> {
            final List<Element> layoutProperties = XMLUtils.getElementsFromNodeList(widgetProperties.getChildNodes());

            /* add size & position properties */
            final List<Element> sizeAndPosition = XMLUtils.getElementsFromNodeList(layoutProperties.get(0).getChildNodes());

            if (xExpandState != TristateCheckBox.DONT_CARE) {
                final Element xExpand = sizeAndPosition.get(4);
                final String value = String.valueOf(xExpandState == TristateCheckBox.SELECTED);
                xExpand.getAttributeNode("value").setValue(value);
            }

            if (yExpandState != TristateCheckBox.DONT_CARE) {
                final Element yExpand = sizeAndPosition.get(5);
                final String value = String.valueOf(yExpandState == TristateCheckBox.SELECTED);
                yExpand.getAttributeNode("value").setValue(value);
            }
        });
    }
}
