package org.pdf.forms.gui.properties.layout;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.jdesktop.layout.GroupLayout.BASELINE;
import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.LayoutStyle.RELATED;
import static org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TriStateCheckBox.NOT_SELECTED;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.gui.properties.PropertyChanger;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TriStateCheckBox;
import org.pdf.forms.model.des.SizeAndPosition;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

public class SizeAndPositionPanel extends JPanel {

    private static final String[] ANCHORS = {
            "Top Left",
            "Top Middle",
            "Top Right",
            "Middle Left",
            "Center",
            "Middle Right",
            "Button Left",
            "Bottom Middle",
            "Bottom Right"
    };

    private static final String EXPAND_TO_FIT = "Expand to fit";
    private static final int UNITS = (int) (Rule.INCH / 2.54);

    private final Logger logger = LoggerFactory.getLogger(SizeAndPositionPanel.class);
    private final IDesigner designerPanel;

    private JTextField xBox;
    private JTextField widthBox;
    private TriStateCheckBox xExpandToFitBox;
    private JTextField heightBox;
    private JTextField yBox;
    private TriStateCheckBox yExpandToFitBox;
    private JComboBox<String> anchorLocationBox;

    private JToggleButton rotate0Degree;
    private JToggleButton rotate90Degree;
    private JToggleButton rotate180Degree;
    private JToggleButton rotate270Degree;
    private ButtonGroup buttonGroup;
    private Set<IWidget> widgets;

    public SizeAndPositionPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;

        initializePanel();
    }

    private void initializePanel() {
        setBorder(BorderFactory.createTitledBorder("Size & Position"));

        final JLabel xLabel = new JLabel("X:");
        final JLabel widthLabel = new JLabel("Width:");

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

        xExpandToFitBox = new TriStateCheckBox(EXPAND_TO_FIT, NOT_SELECTED, this::checkboxClicked);
        //xExpandToFitBox.setText(EXPAND_TO_FIT);
        xExpandToFitBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        xExpandToFitBox.setEnabled(false);
        xExpandToFitBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        final JLabel yLabel = new JLabel("Y:");
        final JLabel heightLabel = new JLabel("Height:");

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

        yExpandToFitBox = new TriStateCheckBox(EXPAND_TO_FIT, NOT_SELECTED, this::checkboxClicked);
        //yExpandToFitBox.setText(EXPAND_TO_FIT);
        yExpandToFitBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        yExpandToFitBox.setEnabled(false);
        yExpandToFitBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        final JLabel anchorLabel = new JLabel("Anchor:");
        anchorLabel.setEnabled(false);

        anchorLocationBox = new JComboBox<>(ANCHORS);
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

        final GroupLayout groupLayout = new GroupLayout(this);
        setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(LEADING)
                        .add(groupLayout.createSequentialGroup()
                                .add(groupLayout.createParallelGroup(LEADING)
                                        .add(groupLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .add(xExpandToFitBox))
                                        .add(createSequentialGroup(xLabel, xBox, widthLabel, widthBox, groupLayout)))
                                .add(25, 25, 25)
                                .add(groupLayout.createParallelGroup(LEADING)
                                        .add(createSequentialGroup(yLabel, yBox, heightLabel, heightBox, groupLayout))
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
                                .add(createParallelGroup(xLabel, xBox, yLabel, yBox, groupLayout))
                                .addPreferredGap(RELATED)
                                .add(createParallelGroup(widthLabel, widthBox, heightLabel, heightBox, groupLayout))
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
        final JToggleButton rotateButton = new JToggleButton(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/Anchor Rotation 270.png")));
        rotateButton.setEnabled(false);
        rotateButton.setName("270");
        rotateButton.addActionListener(this::updateRotation);
        return rotateButton;
    }

    private JToggleButton createRotate180DegreeButton() {
        final JToggleButton rotateButton = new JToggleButton(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/Anchor Rotation 180.png")));
        rotateButton.setEnabled(false);
        rotateButton.setName("180");
        rotateButton.addActionListener(this::updateRotation);
        return rotateButton;
    }

    private JToggleButton createRotate90DegreeButton() {
        final JToggleButton rotateButton = new JToggleButton(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/Anchor Rotation 90.png")));
        rotateButton.setEnabled(false);
        rotateButton.setName("90");
        rotateButton.addActionListener(this::updateRotation);
        return rotateButton;
    }

    private JToggleButton createRotate0DegreeButton() {
        final JToggleButton rotateButton = new JToggleButton(new ImageIcon(getClass()
                .getResource("/org/pdf/forms/res/Anchor Rotation 0.png")));
        rotateButton.setEnabled(false);
        rotateButton.setName("0");
        rotateButton.addActionListener(this::updateRotation);
        return rotateButton;
    }

    private GroupLayout.SequentialGroup createSequentialGroup(
            final JLabel labelForTextFieldA,
            final JTextField textFieldA,
            final JLabel labelForTextFieldB,
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
            final JTextField textFieldA,
            final JLabel labelForTextFieldB,
            final JTextField textFieldB,
            final GroupLayout groupLayout) {
        return groupLayout.createParallelGroup(BASELINE)
                .add(labelForTextFieldA)
                .add(labelForTextFieldB)
                .add(textFieldA, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                .add(textFieldB, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);
    }

    private void updateAnchor(final ActionEvent e) {
        final String value = Optional.ofNullable(anchorLocationBox.getSelectedItem())
                .map(Object::toString)
                .orElse("");

        widgets.forEach(widget -> widget.getWidgetModel().getProperties().getLayout().getSizeAndPosition()
                .setAnchor(value));
    }

    private void updateRotation(final ActionEvent e) {
        final String value = ((JComponent) e.getSource()).getName();
        widgets.forEach(widget -> widget.getWidgetModel().getProperties().getLayout().getSizeAndPosition()
                .setRotation(value));
    }

    private void updateSizeAndPosition() {
        if (widgets != null) {
            final Point point = getPoint();
            final Dimension dimension = getDimension();
            PropertyChanger.updateSizeAndPosition(widgets, point, dimension);
        }

        if (designerPanel != null) {
            designerPanel.repaint();
        }
    }

    private Point getPoint() {
        final Point point = new Point(-1, -1);

        if (!xBox.getText().equals("mixed")) {
            final String xText = xBox.getText().replace("cm", "");
            final double customX = asDouble(xText);
            xBox.setText(customX + " cm");

            final double x = customX * UNITS;
            point.x = (int) (Math.round(x) + IMainFrame.INSET);
        }

        if (!yBox.getText().equals("mixed")) {
            final String yText = yBox.getText().replace("cm", "");
            final double customY = asDouble(yText);
            yBox.setText(customY + " cm");

            final double y = customY * UNITS;
            point.y = (int) (Math.round(y) + IMainFrame.INSET);
        }

        return point;
    }

    private Dimension getDimension() {
        final Dimension dimension = new Dimension(-1, -1);

        if (!widthBox.getText().equals("mixed")) {
            final String widthText = widthBox.getText().replace("cm", "");
            final double customWidth = asDouble(widthText);
            widthBox.setText(customWidth + " cm");

            final double width = customWidth * UNITS;
            dimension.width = (int) Math.round(width);
        }

        if (!heightBox.getText().equals("mixed")) {
            final String heightText = heightBox.getText().replace("cm", "");
            final double customHeight = asDouble(heightText);
            heightBox.setText(customHeight + " cm");

            final double height = customHeight * UNITS;
            dimension.height = (int) Math.round(height);
        }

        return dimension;
    }

    public void setProperties(final Set<IWidget> widgets) {
        this.widgets = widgets;

        final String xCordToUse = getXCoordinateToUse(widgets);
        setXCoordinate(xCordToUse);

        final String yCordToUse = getYCoordinateToUse(widgets);
        setYCoordinate(yCordToUse);

        final String widthToUse = getWidthToUse(widgets);
        setWidth(widthToUse);

        final String heightToUse = getHeightToUse(widgets);
        setHeight(heightToUse);

        final String anchorLocationToUse = getAnchorToUse(widgets);
        setAnchorLocation(anchorLocationToUse);

        final String rotationToUse = getRotationToUse(widgets);
        setRotation(rotationToUse);
    }

    private void setXCoordinate(final String xCordToUse) {
        if (xCordToUse == null) {
            return;
        }

        if (xCordToUse.equals("mixed")) {
            xBox.setText("mixed");
        } else {
            final double x = asInteger(xCordToUse);
            final double xForTextField = round((x - IMainFrame.INSET) / UNITS);
            xBox.setText(xForTextField + " cm");
        }
    }

    private void setYCoordinate(final String yCordToUse) {
        if (yCordToUse == null) {
            return;
        }

        if (yCordToUse.equals("mixed")) {
            yBox.setText("mixed");
        } else {
            final double y = asInteger(yCordToUse);
            final double yForTextField = round((y - IMainFrame.INSET) / UNITS);
            yBox.setText(yForTextField + " cm");
        }
    }

    private void setWidth(final String widthToUse) {
        if (widthToUse == null) {
            return;
        }

        if (widthToUse.equals("mixed")) {
            widthBox.setText("mixed");
        } else {
            final double width = asInteger(widthToUse);
            final double widthForTextField = round(width / UNITS);
            widthBox.setText(widthForTextField + " cm");
        }
    }

    private void setHeight(final String heightToUse) {
        if (heightToUse == null) {
            return;
        }

        if (heightToUse.equals("mixed")) {
            heightBox.setText("mixed");
        } else {
            final double height = asInteger(heightToUse);
            final double heightForTextField = round(height / UNITS);
            heightBox.setText(heightForTextField + " cm");
        }
    }

    private void setAnchorLocation(final String anchorLocationToUse) {
        final String anchorSelection;
        if ("mixed".equals(anchorLocationToUse)) {
            anchorSelection = null;
        } else {
            anchorSelection = anchorLocationToUse;
        }

        anchorLocationBox.setSelectedItem(anchorSelection);
    }

    private void setRotation(final String rotationToUse) {
        if (rotationToUse == null) {
            return;
        }

        switch (rotationToUse) {
            case "mixed":
                buttonGroup.setSelected(new JToggleButton("").getModel(), true);
                break;
            case "0":
                rotate0Degree.setSelected(true);
                break;
            case "90":
                rotate90Degree.setSelected(true);
                break;
            case "180":
                rotate180Degree.setSelected(true);
                break;
            case "270":
                rotate270Degree.setSelected(true);
                break;
            default:
                logger.warn("Unexpected rotation to use {}", rotationToUse);
                break;
        }
    }

    private String getXCoordinateToUse(final Set<IWidget> widgets) {
        final List<String> xCoordValues = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getLayout().getSizeAndPosition().getX()
                        .orElse(""))
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(xCoordValues);
    }

    private String getYCoordinateToUse(final Set<IWidget> widgets) {
        final List<String> yCoordValues = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getLayout().getSizeAndPosition().getY()
                        .orElse(""))
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(yCoordValues);
    }

    private String getWidthToUse(final Set<IWidget> widgets) {
        final List<String> widthValues = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getLayout().getSizeAndPosition().getWidth()
                        .orElse(""))
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(widthValues);
    }

    private String getHeightToUse(final Set<IWidget> widgets) {
        final List<String> heightValues = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getLayout().getSizeAndPosition().getHeight()
                        .orElse(""))
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(heightValues);
    }

    private String getAnchorToUse(final Set<IWidget> widgets) {
        final List<String> anchorValues = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getLayout().getSizeAndPosition().getAnchor()
                        .orElse(""))
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(anchorValues);
    }

    private String getRotationToUse(final Set<IWidget> widgets) {
        final List<String> rotationValues = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getLayout().getSizeAndPosition().getRotation()
                        .orElse(""))
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(rotationValues);
    }

    private String findCommonOrMixedValue(final List<String> values) {
        final boolean listContainsOnlyEqualValues = Collections
                .frequency(values, values.get(0)) == values.size();
        if (listContainsOnlyEqualValues) {
            return values.get(0);
        }
        return "mixed";
    }

    private int asInteger(final String integerNumber) {
        return Optional.of(Ints.tryParse(integerNumber))
                .orElse(-1);
    }

    private double asDouble(final String doubleNumber) {
        return Optional.ofNullable(Doubles.tryParse(doubleNumber))
                .orElse(-1d);
    }

    private double round(final double number) {
        final double exponential = Math.pow(10, 3);

        double value = number;
        value *= exponential;
        value = Math.round(value);
        value /= exponential;

        return number;
    }

    private void checkboxClicked(final MouseEvent mouseEvent) {
        final TriStateCheckBox.State xExpandState = xExpandToFitBox.getState();
        final TriStateCheckBox.State yExpandState = yExpandToFitBox.getState();

        widgets.forEach(widget -> {
            final SizeAndPosition sizeAndPosition = widget.getWidgetModel().getProperties().getLayout()
                    .getSizeAndPosition();

            if (xExpandState != TriStateCheckBox.DONT_CARE) {
                final String value = String.valueOf(xExpandState == TriStateCheckBox.SELECTED);
                sizeAndPosition.setXExpandToFit(value);
            }

            if (yExpandState != TriStateCheckBox.DONT_CARE) {
                final String value = String.valueOf(yExpandState == TriStateCheckBox.SELECTED);
                sizeAndPosition.setYExpandToFit(value);
            }
        });
    }
}
