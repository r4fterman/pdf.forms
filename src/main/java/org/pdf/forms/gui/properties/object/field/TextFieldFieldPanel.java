package org.pdf.forms.gui.properties.object.field;

import static org.jdesktop.layout.GroupLayout.BASELINE;
import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.GroupLayout.TRAILING;
import static org.jdesktop.layout.LayoutStyle.RELATED;

import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBox;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TristateCheckBoxParent;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class TextFieldFieldPanel extends JPanel implements TristateCheckBoxParent {

    private Map<IWidget, Element> widgetsAndProperties;

    private JCheckBox allowMultipleLinesBox;
    private JCheckBox limitLengthBox;
    private JTextField maxCharsBox;

    public TextFieldFieldPanel() {
        initComponents();
    }

    private void initComponents() {
        allowMultipleLinesBox = new TristateCheckBox("Allow Multiple Line", TristateCheckBox.NOT_SELECTED, this);
        allowMultipleLinesBox.setText("Allow Multiple Lines");
        allowMultipleLinesBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        allowMultipleLinesBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        limitLengthBox = new TristateCheckBox("Limit Length", TristateCheckBox.NOT_SELECTED, this);
        limitLengthBox.setText("Limit Length");
        limitLengthBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        limitLengthBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        final JLabel maxCharsLabel = new JLabel();
        maxCharsLabel.setText("Max Chars:");

        maxCharsBox = new JTextField();
        maxCharsBox.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateMaxChars();
            }
        });

        final JLabel presenceLabel = new JLabel();
        presenceLabel.setText("Presence");

        final JComboBox<String> presenceBox = new JComboBox<>();
        presenceBox.setModel(new DefaultComboBoxModel<>(new String[]{
                "Visible"}));

        final JComboBox<String> appearanceBox = new JComboBox<>();
        appearanceBox.setModel(new DefaultComboBoxModel<>(new String[]{
                "None", "Underline", "Solid", "Sunken Box", "Custom..."}));
        appearanceBox.setSelectedIndex(3);
        appearanceBox.setEnabled(false);

        final JLabel appearanceLabel = new JLabel();
        appearanceLabel.setText("Appearance:");
        appearanceLabel.setEnabled(false);

        final JSeparator separator = new JSeparator();

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        final GroupLayout.SequentialGroup sequentialGroup1 = layout.createSequentialGroup()
                .add(appearanceLabel)
                .addPreferredGap(RELATED)
                .add(appearanceBox, 0, 190, Short.MAX_VALUE);

        final GroupLayout.SequentialGroup sequentialGroup4 = layout.createSequentialGroup()
                .add(limitLengthBox)
                .addPreferredGap(RELATED, 42, Short.MAX_VALUE)
                .add(maxCharsLabel)
                .addPreferredGap(RELATED)
                .add(maxCharsBox, PREFERRED_SIZE, 42, PREFERRED_SIZE);
        final GroupLayout.ParallelGroup parallelGroup4 = layout.createParallelGroup(LEADING, false)
                .add(sequentialGroup4)
                .add(allowMultipleLinesBox);
        final GroupLayout.SequentialGroup sequentialGroup2 = layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(parallelGroup4);
        final GroupLayout.SequentialGroup sequentialGroup3 = layout.createSequentialGroup()
                .add(presenceLabel)
                .addPreferredGap(RELATED)
                .add(presenceBox, 0, 202, Short.MAX_VALUE);
        final GroupLayout.ParallelGroup parallelGroup = layout.createParallelGroup(TRAILING)
                .add(separator, DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .add(LEADING, sequentialGroup1)
                .add(LEADING, sequentialGroup2)
                .add(LEADING, sequentialGroup3);
        final GroupLayout.SequentialGroup sequentialGroup = layout.createSequentialGroup()
                .addContainerGap()
                .add(parallelGroup)
                .add(16, 16, 16);

        layout.setHorizontalGroup(layout.createParallelGroup(LEADING).add(sequentialGroup));

        final GroupLayout.ParallelGroup parallelGroup1 = layout.createParallelGroup(BASELINE)
                .add(appearanceLabel)
                .add(appearanceBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);
        final GroupLayout.ParallelGroup parallelGroup2 = layout.createParallelGroup(BASELINE)
                .add(limitLengthBox)
                .add(maxCharsLabel)
                .add(maxCharsBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);
        final GroupLayout.ParallelGroup parallelGroup3 = layout.createParallelGroup(BASELINE)
                .add(presenceLabel)
                .add(presenceBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);
        final GroupLayout.SequentialGroup sequentialGroup5 = layout.createSequentialGroup()
                .addContainerGap()
                .add(parallelGroup1)
                .addPreferredGap(RELATED)
                .add(allowMultipleLinesBox)
                .addPreferredGap(RELATED)
                .add(parallelGroup2)
                .addPreferredGap(RELATED)
                .add(separator, PREFERRED_SIZE, 10, PREFERRED_SIZE)
                .addPreferredGap(RELATED)
                .add(parallelGroup3)
                .addContainerGap(DEFAULT_SIZE, Short.MAX_VALUE);
        layout.setVerticalGroup(layout.createParallelGroup(LEADING).add(sequentialGroup5));
    }

    private void updateMaxChars() {
        final String maxCharsValue = maxCharsBox.getText();
        if (maxCharsValue != null && !maxCharsValue.equals("mixed")) {
            for (final Element widgetProperties: widgetsAndProperties.values()) {
                final Optional<Element> maxChars = XMLUtils.getPropertyElement(widgetProperties, "Max Chars");
                if (maxChars.isPresent()) {
                    final Element maxCharsElement = maxChars.get();
                    maxCharsElement.getAttributeNode("value").setValue(maxCharsValue);
                }
            }
        }
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        TristateCheckBox.State allowMultipleLinesToUse = null;
        TristateCheckBox.State limitLengthToUse = null;

        String maxCharsToUse = null;
        for (final Element objectPropertiesElement: widgetsAndProperties.values()) {
            final Element fieldProperties = (Element) objectPropertiesElement.getElementsByTagName("field").item(0);

            final boolean allowMultipleLines = XMLUtils.getAttributeFromChildElement(fieldProperties,
                    "Allow Multiple Lines")
                    .map(Boolean::valueOf)
                    .orElse(false);
            final boolean limitLength = XMLUtils.getAttributeFromChildElement(fieldProperties, "Limit Length")
                    .map(Boolean::valueOf)
                    .orElse(false);
            final String maxChars = XMLUtils.getAttributeFromChildElement(fieldProperties, "Max Chars")
                    .orElse("10");

            if (maxCharsToUse == null) {
                if (allowMultipleLines) {
                    allowMultipleLinesToUse = TristateCheckBox.SELECTED;
                } else {
                    allowMultipleLinesToUse = TristateCheckBox.NOT_SELECTED;
                }
                if (limitLength) {
                    limitLengthToUse = TristateCheckBox.SELECTED;
                } else {
                    limitLengthToUse = TristateCheckBox.NOT_SELECTED;
                }

                maxCharsToUse = maxChars;
            } else {
                if (allowMultipleLinesToUse != TristateCheckBox.DONT_CARE) {
                    if (allowMultipleLinesToUse == TristateCheckBox.SELECTED && !allowMultipleLines) {
                        allowMultipleLinesToUse = TristateCheckBox.DONT_CARE;
                    } else if (allowMultipleLinesToUse == TristateCheckBox.NOT_SELECTED && allowMultipleLines) {
                        allowMultipleLinesToUse = TristateCheckBox.DONT_CARE;
                    }
                }

                if (limitLengthToUse != TristateCheckBox.DONT_CARE) {
                    if (limitLengthToUse == TristateCheckBox.SELECTED && !limitLength) {
                        limitLengthToUse = TristateCheckBox.DONT_CARE;
                    } else if (limitLengthToUse == TristateCheckBox.NOT_SELECTED && limitLength) {
                        limitLengthToUse = TristateCheckBox.DONT_CARE;
                    }
                }

                if (!maxCharsToUse.equals(maxChars)) {
                    maxCharsToUse = "mixed";
                }
            }

            ((TristateCheckBox) allowMultipleLinesBox).setState(allowMultipleLinesToUse);
            ((TristateCheckBox) limitLengthBox).setState(limitLengthToUse);

            final String mixed;
            if (maxCharsToUse.equals("mixed")) {
                mixed = "mixed";
            } else {
                mixed = maxCharsToUse;
            }
            maxCharsBox.setText(mixed);
        }
    }

    @Override
    public void checkboxClicked(final MouseEvent event) {
        final TristateCheckBox.State allowMultipleLinesState = (((TristateCheckBox) allowMultipleLinesBox).getState());
        final TristateCheckBox.State limitLengthState = (((TristateCheckBox) limitLengthBox).getState());

        for (final Element propertiesElement: widgetsAndProperties.values()) {
            final List<Element> objectProperties = XMLUtils.getElementsFromNodeList(propertiesElement.getChildNodes());

            final List<Element> fieldProperties = XMLUtils.getElementsFromNodeList(objectProperties.get(0)
                    .getChildNodes());
            if (allowMultipleLinesState != TristateCheckBox.DONT_CARE) {
                final Element allowMultipleLines = fieldProperties.get(1);
                final String value;
                if (allowMultipleLinesState == TristateCheckBox.SELECTED) {
                    value = "true";
                } else {
                    value = "false";
                }
                allowMultipleLines.getAttributeNode("value").setValue(value);
            }

            if (limitLengthState != TristateCheckBox.DONT_CARE) {
                final Element limitLength = fieldProperties.get(2);
                final String value;
                if (limitLengthState == TristateCheckBox.SELECTED) {
                    value = "true";
                } else {
                    value = "false";
                }
                limitLength.getAttributeNode("value").setValue(value);
            }
        }
    }

}
