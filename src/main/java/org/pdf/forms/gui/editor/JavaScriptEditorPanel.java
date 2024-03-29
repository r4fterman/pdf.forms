package org.pdf.forms.gui.editor;

import static javax.swing.SwingConstants.VERTICAL;
import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.LayoutStyle.RELATED;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import org.jdesktop.layout.GroupLayout;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.model.des.JavaScriptContent;
import org.pdf.forms.widgets.IWidget;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

public class JavaScriptEditorPanel extends JPanel implements Dockable {

    private final JComboBox<String> eventBox;
    private final JComboBox<String> languageBox;
    private final JComboBox<String> runAtBox;
    private final JTextArea scriptBox;

    private Map<String, String> eventsAndValues;
    private Set<IWidget> widgets;

    public JavaScriptEditorPanel() {
        scriptBox = createScriptBox();
        eventBox = new JComboBox<>();
        eventBox.addActionListener(this::eventChanged);
        languageBox = new JComboBox<>(new String[]{"JavaScript"});
        runAtBox = new JComboBox<>(new String[]{"Client"});

        initPanel();
    }

    private void initPanel() {
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(scriptBox);

        final JLabel eventBoxLabel = new JLabel("Show:");
        final JLabel scriptLanguageLabel = new JLabel("Language:");
        final JLabel runAtLabel = new JLabel("Run At:");
        final JSeparator separator = new JSeparator(VERTICAL);
        final JButton saveButton = createSaveButton();

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(LEADING)
                        .add(createHorizontalGroup(eventBoxLabel, scriptLanguageLabel, runAtLabel, separator, saveButton, layout))
                        .add(GroupLayout.TRAILING, scrollPane, DEFAULT_SIZE, 607, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(createVerticalGroup(eventBoxLabel, scriptLanguageLabel, runAtLabel, separator, saveButton, layout))
                                .addPreferredGap(RELATED)
                                .add(scrollPane, DEFAULT_SIZE, 144, Short.MAX_VALUE))
        );
    }

    private GroupLayout.ParallelGroup createVerticalGroup(
            final JLabel eventBoxLabel,
            final JLabel scriptLanguageLabel,
            final JLabel runAtLabel,
            final JSeparator separator,
            final JButton saveButton,
            final GroupLayout layout) {
        return layout.createParallelGroup(LEADING, false)
                .add(separator)
                .add(eventBoxLabel, DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .add(eventBox, DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .add(languageBox, DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .add(scriptLanguageLabel, DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .add(runAtBox, DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .add(runAtLabel, PREFERRED_SIZE, 20, PREFERRED_SIZE)
                .add(separator, DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .add(saveButton, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE);
    }

    private GroupLayout.SequentialGroup createHorizontalGroup(
            final JLabel eventBoxLabel,
            final JLabel scriptLanguageLabel,
            final JLabel runAtLabel,
            final JSeparator separator,
            final JButton saveButton,
            final GroupLayout layout) {
        return layout.createSequentialGroup()
                .add(eventBoxLabel)
                .addPreferredGap(RELATED)
                .add(eventBox, PREFERRED_SIZE, 155, PREFERRED_SIZE)
                .addPreferredGap(RELATED)
                .add(separator, PREFERRED_SIZE, 2, PREFERRED_SIZE)
                .addPreferredGap(RELATED)
                .add(saveButton)
                .addPreferredGap(RELATED, 70, Short.MAX_VALUE)
                .add(scriptLanguageLabel)
                .addPreferredGap(RELATED)
                .add(languageBox, PREFERRED_SIZE, 93, PREFERRED_SIZE)
                .addPreferredGap(RELATED)
                .add(separator, PREFERRED_SIZE, 2, PREFERRED_SIZE)
                .addPreferredGap(RELATED)
                .add(runAtLabel)
                .addPreferredGap(RELATED)
                .add(runAtBox, PREFERRED_SIZE, 76, PREFERRED_SIZE);
    }

    private JTextArea createScriptBox() {
        final JTextArea textArea = new JTextArea();
        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        return textArea;
    }

    private JButton createSaveButton() {
        final JButton saveButton = new JButton();
        saveButton.setText("Save");
        saveButton.addActionListener(this::saveClicked);
        return saveButton;
    }

    private void saveClicked(final ActionEvent event) {
        if (widgets.size() == 1 && widgets.iterator().next() instanceof FormsDocument) {
            final FormsDocument formsDocument = (FormsDocument) widgets.iterator().next();
            formsDocument.getDesDocument().getJavaScript().ifPresent(this::saveJavaScript);
        } else {
            saveJavaScriptInWidgets();
        }
    }

    private void eventChanged(final ActionEvent event) {
        final String selectedItem = (String) eventBox.getSelectedItem();
        scriptBox.setText(eventsAndValues.getOrDefault(selectedItem, ""));
    }

    public void setScript(final Set<IWidget> widgets) {
        this.widgets = widgets;

        eventBox.removeAllItems();
        eventsAndValues = new LinkedHashMap<>();

        if (widgets.size() == 1) {
            setSingleWidgetScript(widgets.iterator().next());
        } else {
            getDockKey().setName("Script Editor - Multiple Components");
            handleWidgets(widgets);
            eventsAndValues.keySet().forEach(eventBox::addItem);
        }
    }

    private void setSingleWidgetScript(final IWidget widget) {
        if (widget == null || widget instanceof Page) {
            return;
        }

        if (widget instanceof FormsDocument) {
            getDockKey().setName("Script Editor - Document");
            final FormsDocument document = (FormsDocument) widget;
            document.getDesDocument().getJavaScript().ifPresent(this::extractJavaScript);
        } else {
            getDockKey().setName("Script Editor - " + widget.getWidgetName() + " Component");
            handleWidgets(widgets);
        }
        eventsAndValues.keySet().forEach(eventBox::addItem);
    }

    private void saveJavaScriptInWidgets() {
        widgets.stream()
                .map(IWidget::getJavaScript)
                .forEach(this::saveJavaScript);
    }

    private void saveJavaScript(final JavaScriptContent javaScriptContent) {
        final String eventName = (String) eventBox.getSelectedItem();
        final String eventValue = scriptBox.getText().trim();

        javaScriptContent.setEventValue(eventName, eventValue);
    }

    private void handleWidgets(final Set<IWidget> widgets) {
        for (final IWidget widget: widgets) {
            final Optional<JavaScriptContent> javaScriptContent = Optional.ofNullable(widget.getJavaScript());

            final boolean jsExtracted = javaScriptContent
                    .map(this::extractJavaScript)
                    .orElse(false);
            if (jsExtracted) {
                break;
            }
        }
    }

    private boolean extractJavaScript(final JavaScriptContent javaScriptContent) {
        if (javaScriptContent.getInitialize().isEmpty()) {
            setState(false);
            return true;
        }

        setState(true);

        final Map<String, String> events = javaScriptContent.getEvents();
        final Set<Map.Entry<String, String>> entries = events.entrySet();
        for (final Map.Entry<String, String> entry: entries) {
            final String existingValue = eventsAndValues.getOrDefault(entry.getKey(), entry.getValue());
            if (!existingValue.equals(entry.getValue())) {
                eventsAndValues.put(entry.getKey(), "mixed scripts in this event for the selected components");
            } else {
                eventsAndValues.put(entry.getKey(), entry.getValue());
            }
        }
        return false;
    }

    public void setState(final boolean state) {
        eventBox.removeAllItems();
        scriptBox.setText("");

        eventBox.setEnabled(state);
        languageBox.setEnabled(state);
        runAtBox.setEnabled(state);
        scriptBox.setEnabled(state);

        getDockKey().setName("Script Editor");
    }

    @Override
    public DockKey getDockKey() {
        return new DockKey("scripteditor", "Script Editor", "JavaScript Editor");
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
