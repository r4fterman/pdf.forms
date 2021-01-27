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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.*;

import org.jdesktop.layout.GroupLayout;
import org.pdf.forms.gui.properties.customcomponents.tridstatecheckbox.TriStateCheckBox;
import org.pdf.forms.widgets.IWidget;

public class TextFieldFieldPanel extends JPanel {

    private static final String[] PRESENCES = {"Visible"};
    private static final String[] APPEARANCES = {
            "None",
            "Underline",
            "Solid",
            "Sunken Box",
            "Custom..."};

    private Set<IWidget> widgets;

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

    public void setProperties(final Set<IWidget> widgets) {
        this.widgets = widgets;

        final TriStateCheckBox.State allowMultipleLinesToUse = getAllowedMultipleLinesToUse(widgets);
        ((TriStateCheckBox) allowMultipleLinesBox).setState(allowMultipleLinesToUse);

        final TriStateCheckBox.State limitLengthToUse = getLimitLengthToUse(widgets);
        ((TriStateCheckBox) limitLengthBox).setState(limitLengthToUse);

        final String maxCharsToUse = getMaxCharsToUse(widgets);
        maxCharsBox.setText(maxCharsToUse);

    }

    private void updateMaxChars(final String value) {
        if (widgets != null
                && value != null
                && !value.equals("mixed")) {
            widgets.forEach(widget -> widget.getWidgetModel().getProperties().getObject().getField()
                    .setMaxChars(value));
        }
    }

    private TriStateCheckBox.State getAllowedMultipleLinesToUse(final Set<IWidget> widgets) {
        final List<Boolean> allowMultipleLinesValues = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getObject().getField().allowMultipleLines())
                .collect(toUnmodifiableList());

        return getTriStateValue(allowMultipleLinesValues);
    }

    private TriStateCheckBox.State getLimitLengthToUse(final Set<IWidget> widgets) {
        final List<Boolean> limitLengthValues = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getObject().getField().getLimitLength())
                .collect(toUnmodifiableList());

        return getTriStateValue(limitLengthValues);
    }

    private String getMaxCharsToUse(final Set<IWidget> widgets) {
        final List<String> maxCharValues = widgets.stream()
                .map(widget -> widget.getWidgetModel().getProperties().getObject().getField().getMaxChars()
                        .orElse("10"))
                .collect(toUnmodifiableList());

        return findCommonOrMixedValue(maxCharValues);
    }

    private void saveLimitLength(final MouseEvent event) {
        final TriStateCheckBox.State state = (((TriStateCheckBox) limitLengthBox).getState());
        final Optional<Boolean> triStateValue = getTriStateValue(state);
        if (triStateValue.isEmpty()) {
            return;
        }

        final boolean limit = triStateValue.get();
        widgets.forEach(widget -> widget.getWidgetModel().getProperties().getObject().getField().setLimitLength(limit));
    }

    private void saveAllowedMultipleLines(final MouseEvent event) {
        final TriStateCheckBox.State state = (((TriStateCheckBox) allowMultipleLinesBox).getState());
        final Optional<Boolean> triStateValue = getTriStateValue(state);
        if (triStateValue.isEmpty()) {
            return;
        }

        final boolean allow = triStateValue.get();
        widgets.forEach(widget -> widget.getWidgetModel().getProperties().getObject().getField()
                .allowMultipleLines(allow));
    }

    private Optional<Boolean> getTriStateValue(final TriStateCheckBox.State state) {
        if (state == TriStateCheckBox.DONT_CARE) {
            return Optional.empty();
        }

        final boolean selected = state == TriStateCheckBox.SELECTED;
        return Optional.of(selected);
    }

    private TriStateCheckBox.State getTriStateValue(final List<Boolean> values) {
        final boolean listContainsOnlyEqualValues = Collections
                .frequency(values, values.get(0)) == values.size();

        if (listContainsOnlyEqualValues) {
            if (Boolean.TRUE.equals(values.get(0))) {
                return TriStateCheckBox.SELECTED;
            }
            return TriStateCheckBox.NOT_SELECTED;
        }
        return TriStateCheckBox.DONT_CARE;
    }

    private String findCommonOrMixedValue(final List<String> values) {
        final boolean listContainsOnlyEqualValues = Collections
                .frequency(values, values.get(0)) == values.size();
        if (listContainsOnlyEqualValues) {
            return values.get(0);
        }
        return "mixed";
    }

}
