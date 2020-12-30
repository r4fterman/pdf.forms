package org.pdf.forms.gui.properties.object.page;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.Rule;
import org.pdf.forms.widgets.IWidget;

import com.itextpdf.text.PageSize;

public class PagePanel extends JPanel {

    private static final String[] PAPER_TYPES = {
            "A4",
            "A5",
            "Letter",
            "Custom"
    };

    private static final Dimension A4 = new Dimension((int) PageSize.A4.getWidth(), (int) PageSize.A4.getHeight());
    private static final Dimension A5 = new Dimension((int) PageSize.A5.getWidth(), (int) PageSize.A5.getHeight());
    private static final Dimension LETTER = new Dimension((int) PageSize.LETTER.getWidth(),
            (int) PageSize.LETTER.getHeight());

    private static final Map<String, Dimension> PAPER_TYPES_TO_DIMENSION = Map.of(
            "A4", A4,
            "A5", A5,
            "Letter", LETTER
    );
    private static final Map<Dimension, String> DIMENSION_TO_PAPER_TYPES = Map.of(
            A4, "A4",
            A5, "A5",
            LETTER, "Letter"
    );

    private final IDesigner designerPanel;

    private Page page;

    private JTextField heightBox;
    private JRadioButton landscapeButton;
    private JComboBox<String> paperTypeBox;
    private JRadioButton portraitButton;
    private JTextField widthBox;

    public PagePanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;

        initComponents();

        portraitButton.setEnabled(false);
        landscapeButton.setEnabled(false);
    }

    private void initComponents() {
        final JLabel paperTypeLabel = new JLabel("Paper Type:");

        paperTypeBox = new JComboBox<>();
        paperTypeBox.setModel(new DefaultComboBoxModel<>(PAPER_TYPES));
        paperTypeBox.addActionListener(this::updatePaperType);

        final JLabel heightLabel = new JLabel("Height:");

        heightBox = new JTextField();
        heightBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateSize();
            }
        });

        final JLabel widthLabel = new JLabel("Width:");

        widthBox = new JTextField();
        widthBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateSize();
            }
        });

        final JLabel orientationLabel = new JLabel("Orientation:");

        portraitButton = new JRadioButton("Portrait");
        portraitButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        portraitButton.setMargin(new Insets(0, 0, 0, 0));
        portraitButton.addActionListener(this::orientationClicked);

        landscapeButton = new JRadioButton("Landscape");
        landscapeButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        landscapeButton.setMargin(new Insets(0, 0, 0, 0));
        landscapeButton.addActionListener(this::orientationClicked);

        final ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(portraitButton);
        buttonGroup.add(landscapeButton);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(paperTypeLabel)
                                                .addPreferredGap(LayoutStyle.RELATED)
                                                .add(paperTypeBox,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        184,
                                                        GroupLayout.PREFERRED_SIZE))
                                        .add(layout.createSequentialGroup()
                                                .add(10, 10, 10)
                                                .add(layout.createParallelGroup(GroupLayout.LEADING)
                                                        .add(layout.createSequentialGroup()
                                                                .add(heightLabel)
                                                                .addPreferredGap(LayoutStyle.RELATED)
                                                                .add(heightBox,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        62,
                                                                        GroupLayout.PREFERRED_SIZE)
                                                                .add(18, 18, 18)
                                                                .add(widthLabel)
                                                                .addPreferredGap(LayoutStyle.RELATED)
                                                                .add(widthBox,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        62,
                                                                        GroupLayout.PREFERRED_SIZE))
                                                        .add(layout.createSequentialGroup()
                                                                .add(orientationLabel)
                                                                .addPreferredGap(LayoutStyle.UNRELATED)
                                                                .add(portraitButton)
                                                                .add(18, 18, 18)
                                                                .add(landscapeButton)))))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(paperTypeLabel)
                                        .add(paperTypeBox,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.UNRELATED)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(heightLabel)
                                        .add(heightBox,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .add(widthLabel)
                                        .add(widthBox,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .add(11, 11, 11)
                                .add(layout.createParallelGroup(GroupLayout.BASELINE)
                                        .add(orientationLabel)
                                        .add(portraitButton)
                                        .add(landscapeButton))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void updateSize() {
        handleCustomPaper();

        designerPanel.repaint();
    }

    private void updatePaperType(final ActionEvent event) {
        final String size = String.valueOf(paperTypeBox.getSelectedItem());
        final Dimension dimension = PAPER_TYPES_TO_DIMENSION.get(size);
        if (dimension == null) {
            // custom
            paperTypeBox.setSelectedItem("Custom");
            heightBox.setEnabled(true);
            widthBox.setEnabled(true);

            handleCustomPaper();
        } else {
            paperTypeBox.setSelectedItem(size);
            setSize(dimension, false);
            page.setSize(dimension);
        }

        designerPanel.repaint();
    }

    public void setProperties(final Set<IWidget> widgets) {
        this.page = (Page) widgets.iterator().next();

        final Dimension size = page.getSize();
        final String paperType = DIMENSION_TO_PAPER_TYPES.get(size);
        if (paperType == null) {
            setItemQuietly(paperTypeBox, "Custom");
            setSize(size, true);
        } else {
            setItemQuietly(paperTypeBox, paperType);
            setSize(size, false);
        }
    }

    private void handleCustomPaper() {
        final double customHeight = getCustomHeight();
        heightBox.setText(customHeight + " cm");

        final double customWidth = getCustomWidth();
        widthBox.setText(customWidth + " cm");

        final double pageHeight = (customHeight / 2.54) * Rule.DPI;
        final double pageWidth = (customWidth / 2.54) * Rule.DPI;

        page.setSize(new Dimension((int) Math.round(pageWidth), (int) Math.round(pageHeight)));
    }

    private double getCustomWidth() {
        try {
            final String widthText = widthBox.getText().replace("cm", "");
            return Double.parseDouble(widthText);
        } catch (final NumberFormatException e) {
            // do nothing
        }
        return page.getWidth();
    }

    private double getCustomHeight() {
        try {
            final String heightText = heightBox.getText().replace("cm", "");
            return Double.parseDouble(heightText);
        } catch (final NumberFormatException e) {
            // do nothing
        }
        return page.getHeight();
    }

    private void orientationClicked(final ActionEvent event) {
        // do nothing
    }

    private void setItemQuietly(
            final JComboBox<String> comboBox,
            final Object item) {
        final ActionListener[] listeners = comboBox.getActionListeners();
        Arrays.stream(listeners).forEach(comboBox::removeActionListener);

        comboBox.setSelectedItem(item);

        Arrays.stream(listeners).forEach(comboBox::addActionListener);
    }

    private void setSize(
            final Dimension size,
            final boolean enabled) {
        heightBox.setEnabled(enabled);
        widthBox.setEnabled(enabled);

        final double height = round((size.height / Rule.DPI) * 2.54);
        heightBox.setText(height + " cm");

        final double width = round((size.width / Rule.DPI) * 2.54);
        widthBox.setText(width + " cm");
    }

    private double round(final double number) {
        final double exponential = Math.pow(10, 1);

        double value = number;
        value *= exponential;
        value = Math.round(value);
        value /= exponential;

        return value;
    }

}
