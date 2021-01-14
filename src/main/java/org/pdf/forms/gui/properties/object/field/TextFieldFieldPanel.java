package org.pdf.forms.gui.properties.object.field;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.jdesktop.layout.GroupLayout.BASELINE;
import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.GroupLayout.TRAILING;
import static org.jdesktop.layout.LayoutStyle.RELATED;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TriStateCheckBox;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class TextFieldFieldPanel extends JPanel {

    private static final String[] PRESENCES = {"Visible"};
    private static final String[] APPEARANCES = {
            "None",
            "Underline",
            "Solid",
            "Sunken Box",
            "Custom..."};

    private Map<IWidget, Element> widgetsAndProperties;

    private JCheckBox allowMultipleLinesBox;
    private JCheckBox limitLengthBox;
    private JTextField maxCharsBox;

    public TextFieldFieldPanel() {
        initComponents();
    }

    private void initComponents() {
        allowMultipleLinesBox = new TriStateCheckBox(
                "Allow Multiple Line",
                TriStateCheckBox.NOT_SELECTED,
                this::saveAllowedMultipleLines);
        allowMultipleLinesBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        allowMultipleLinesBox.setMargin(new Insets(0, 0, 0, 0));

        limitLengthBox = new TriStateCheckBox("Limit Length", TriStateCheckBox.NOT_SELECTED, this::saveLimitLength);
        limitLengthBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        limitLengthBox.setMargin(new Insets(0, 0, 0, 0));

        final JLabel maxCharsLabel = new JLabel("Max Chars:");

        maxCharsBox = new JTextField();
        maxCharsBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent evt) {
                updateMaxChars(maxCharsBox.getText());
            }
        });

        final JLabel presenceLabel = new JLabel("Presence");

        final JComboBox<String> presenceBox = new JComboBox<>(PRESENCES);
        final JComboBox<String> appearanceBox = new JComboBox<>(APPEARANCES);
        appearanceBox.setSelectedIndex(3);
        appearanceBox.setEnabled(false);

        final JLabel appearanceLabel = new JLabel("Appearance:");
        appearanceLabel.setEnabled(false);

        final JSeparator separator = new JSeparator();

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        final GroupLayout.SequentialGroup appearanceGroup = layout.createSequentialGroup()
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

        final GroupLayout.SequentialGroup presenceGroup = layout.createSequentialGroup()
                .add(presenceLabel)
                .addPreferredGap(RELATED)
                .add(presenceBox, 0, 202, Short.MAX_VALUE);

        final GroupLayout.ParallelGroup parallelGroup = layout.createParallelGroup(TRAILING)
                .add(separator, DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .add(LEADING, appearanceGroup)
                .add(LEADING, sequentialGroup2)
                .add(LEADING, presenceGroup);

        final GroupLayout.SequentialGroup sequentialGroup = layout.createSequentialGroup()
                .addContainerGap()
                .add(parallelGroup)
                .add(16, 16, 16);

        layout.setHorizontalGroup(layout.createParallelGroup(LEADING).add(sequentialGroup));

        final GroupLayout.ParallelGroup appearanceParallelGroup = layout.createParallelGroup(BASELINE)
                .add(appearanceLabel)
                .add(appearanceBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);

        final GroupLayout.ParallelGroup parallelGroup2 = layout.createParallelGroup(BASELINE)
                .add(limitLengthBox)
                .add(maxCharsLabel)
                .add(maxCharsBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);

        final GroupLayout.ParallelGroup presenceParallelGroup = layout.createParallelGroup(BASELINE)
                .add(presenceLabel)
                .add(presenceBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);

        final GroupLayout.SequentialGroup sequentialGroup5 = layout.createSequentialGroup()
                .addContainerGap()
                .add(appearanceParallelGroup)
                .addPreferredGap(RELATED)
                .add(allowMultipleLinesBox)
                .addPreferredGap(RELATED)
                .add(parallelGroup2)
                .addPreferredGap(RELATED)
                .add(separator, PREFERRED_SIZE, 10, PREFERRED_SIZE)
                .addPreferredGap(RELATED)
                .add(presenceParallelGroup)
                .addContainerGap(DEFAULT_SIZE, Short.MAX_VALUE);

        layout.setVerticalGroup(layout.createParallelGroup(LEADING).add(sequentialGroup5));
    }

    public void setProperties(final Map<IWidget, Element> widgetsAndProperties) {
        this.widgetsAndProperties = widgetsAndProperties;

        final TriStateCheckBox.State allowMultipleLinesToUse = getAllowedMultipleLinesToUse(widgetsAndProperties
                .values());
        ((TriStateCheckBox) allowMultipleLinesBox).setState(allowMultipleLinesToUse);

        final TriStateCheckBox.State limitLengthToUse = getLimitLengthToUse(widgetsAndProperties.values());
        ((TriStateCheckBox) limitLengthBox).setState(limitLengthToUse);

        final String maxCharsToUse = getMaxCharsToUse(widgetsAndProperties.values());
        maxCharsBox.setText(maxCharsToUse);

    }

    private void updateMaxChars(final String value) {
        if (value != null && !value.equals("mixed")) {
            for (final Element widgetProperties: widgetsAndProperties.values()) {
                XMLUtils.getPropertyElement(widgetProperties, "Max Chars")
                        .ifPresent(element -> element.getAttributeNode("value").setValue(value));
            }
        }
    }

    private TriStateCheckBox.State getAllowedMultipleLinesToUse(final Collection<Element> widgetElements) {
        final List<Boolean> allowMultipleLinesValues = widgetElements.stream()
                .map(element -> {
                    final Element fieldProperties = (Element) element.getElementsByTagName("field")
                            .item(0);

                    return XMLUtils.getAttributeValueFromChildElement(fieldProperties,
                            "Allow Multiple Lines")
                            .map(Boolean::valueOf)
                            .orElse(false);
                })
                .collect(toUnmodifiableList());

        return getTriStateValue(allowMultipleLinesValues);
    }

    private TriStateCheckBox.State getLimitLengthToUse(final Collection<Element> widgetElements) {
        final List<Boolean> limitLengthValues = widgetElements.stream()
                .map(element -> {
                    final Element fieldProperties = (Element) element.getElementsByTagName("field")
                            .item(0);

                    return XMLUtils.getAttributeValueFromChildElement(fieldProperties, "Limit Length")
                            .map(Boolean::valueOf)
                            .orElse(false);
                })
                .collect(toUnmodifiableList());

        return getTriStateValue(limitLengthValues);
    }

    private TriStateCheckBox.State getTriStateValue(final List<Boolean> valueList) {
        final boolean listContainsOnlyEqualValues = Collections
                .frequency(valueList, valueList.get(0)) == valueList.size();

        if (listContainsOnlyEqualValues) {
            if (Boolean.TRUE.equals(valueList.get(0))) {
                return TriStateCheckBox.SELECTED;
            }
            return TriStateCheckBox.NOT_SELECTED;
        }
        return TriStateCheckBox.DONT_CARE;
    }

    private String getMaxCharsToUse(final Collection<Element> widgetElements) {
        final List<String> maxCharValues = widgetElements.stream()
                .map(element -> {
                    final Element fieldProperties = (Element) element.getElementsByTagName("field").item(0);
                    return XMLUtils.getAttributeValueFromChildElement(fieldProperties, "Max Chars")
                            .orElse("10");
                })
                .collect(toUnmodifiableList());

        final boolean listContainsOnlyEqualValues = Collections
                .frequency(maxCharValues, maxCharValues.get(0)) == maxCharValues.size();
        if (listContainsOnlyEqualValues) {
            return maxCharValues.get(0);
        }
        return "mixed";
    }

    private void saveLimitLength(final MouseEvent event) {
        final TriStateCheckBox.State state = (((TriStateCheckBox) limitLengthBox).getState());

        for (final Element propertiesElement: widgetsAndProperties.values()) {
            final List<Element> objectProperties = XMLUtils.getElementsFromNodeList(propertiesElement.getChildNodes());

            final List<Element> fieldProperties = XMLUtils
                    .getElementsFromNodeList(objectProperties.get(0).getChildNodes());
            final Element element = fieldProperties.get(2);
            saveTriStateValue(state, element);
        }
    }

    private void saveAllowedMultipleLines(final MouseEvent event) {
        final TriStateCheckBox.State state = (((TriStateCheckBox) allowMultipleLinesBox).getState());

        for (final Element propertiesElement: widgetsAndProperties.values()) {
            final List<Element> objectProperties = XMLUtils.getElementsFromNodeList(propertiesElement.getChildNodes());

            final List<Element> fieldProperties = XMLUtils
                    .getElementsFromNodeList(objectProperties.get(0).getChildNodes());
            final Element element = fieldProperties.get(1);
            saveTriStateValue(state, element);
        }
    }

    private void saveTriStateValue(
            final TriStateCheckBox.State state,
            final Element element) {
        if (state == TriStateCheckBox.DONT_CARE) {
            return;
        }

        final Attr valueNode = element.getAttributeNode("value");
        if (state == TriStateCheckBox.SELECTED) {
             valueNode.setValue("true");
        } else {
            valueNode.setValue("false");
        }
    }

}
