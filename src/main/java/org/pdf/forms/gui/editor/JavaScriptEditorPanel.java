package org.pdf.forms.gui.editor;

import static javax.swing.SwingConstants.VERTICAL;
import static org.jdesktop.layout.GroupLayout.DEFAULT_SIZE;
import static org.jdesktop.layout.GroupLayout.LEADING;
import static org.jdesktop.layout.GroupLayout.PREFERRED_SIZE;
import static org.jdesktop.layout.LayoutStyle.RELATED;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import org.jdesktop.layout.GroupLayout;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class JavaScriptEditorPanel extends JPanel implements Dockable {

    private Map<String, String> eventsAndValues;
    private Set<IWidget> widgets;

    private JComboBox<String> eventBox;
    private JComboBox<String> languageBox;
    private JComboBox<String> runAtBox;
    private JTextArea scriptBox;

    public JavaScriptEditorPanel() {
        initComponents();
    }

    private void initComponents() {
        scriptBox = createScriptBox();

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(scriptBox);

        final JLabel eventBoxLabel = new JLabel("Show:");
        eventBox = new JComboBox<>();
        eventBox.addActionListener(this::eventChanged);

        final JLabel scriptLanguageLabel = new JLabel("Language:");
        languageBox = new JComboBox<>(new String[]{"JavaScript"});

        final JLabel runAtLabel = new JLabel("Run At:");
        runAtBox = new JComboBox<>(new String[]{"Client"});

        final JSeparator separator = new JSeparator(VERTICAL);

        final JButton saveButton = createSaveButton();

        final GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(LEADING)
                        .add(layout.createSequentialGroup()
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
                                .add(runAtBox, PREFERRED_SIZE, 76, PREFERRED_SIZE))
                        .add(GroupLayout.TRAILING, scrollPane, DEFAULT_SIZE, 607, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(LEADING, false)
                                        .add(separator)
                                        .add(eventBoxLabel, DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .add(eventBox, DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .add(languageBox, DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .add(scriptLanguageLabel, DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .add(runAtBox, DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .add(runAtLabel, PREFERRED_SIZE, 20, PREFERRED_SIZE)
                                        .add(separator, DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .add(saveButton, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(RELATED)
                                .add(scrollPane, DEFAULT_SIZE, 144, Short.MAX_VALUE))
        );
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

    private void saveClicked(final ActionEvent evt) {
        if (widgets.size() == 1 && widgets.iterator().next() instanceof FormsDocument) {
            final FormsDocument document = (FormsDocument) widgets.iterator().next();
            saveJavaScript(document.getDocument());
        } else {
            saveJavaScriptInWidgets();
        }
    }

    private void eventChanged(final ActionEvent evt) {
        final String selectedItem = (String) eventBox.getSelectedItem();
        scriptBox.setText(eventsAndValues.get(selectedItem));
    }

    public void setScript(final Set<IWidget> widgets) {
        this.widgets = widgets;

        eventBox.removeAllItems();
        eventsAndValues = new LinkedHashMap<>();

        if (widgets.size() == 1) {
            final IWidget widget = widgets.iterator().next();
            if (widget == null || widget instanceof Page) {
                return;
            }

            if (widget instanceof FormsDocument) {
                getDockKey().setName("Script Editor - Document");
                final FormsDocument document = (FormsDocument) widget;
                extractJavaScript(document.getDocument());
            } else {
                getDockKey().setName("Script Editor - " + widget.getWidgetName() + " Component");
                handleWidgets(widgets);
            }
        } else {
            getDockKey().setName("Script Editor - Multiple Components");
            handleWidgets(widgets);
        }

        eventsAndValues.keySet().forEach(event -> eventBox.addItem(event));
    }

    private void saveJavaScriptInWidgets() {
        widgets.stream()
                .map(IWidget::getProperties)
                .forEach(this::saveJavaScript);
    }

    private void saveJavaScript(final Document document) {
        final Element javaScriptPropertiesElement = XMLUtils.getElementsFromNodeList(document.getElementsByTagName("javascript")).get(0);
        final Element currentElement = XMLUtils.getElementsFromNodeList(javaScriptPropertiesElement.getElementsByTagName((String) eventBox.getSelectedItem())).get(0);

        final Text currentTextNode = (Text) currentElement.getChildNodes().item(0);
        if (currentTextNode == null) {
            final Text textNode = document.createTextNode(scriptBox.getText());
            currentElement.appendChild(textNode);
        } else {
            currentTextNode.setNodeValue(scriptBox.getText());
        }
    }

    private void handleWidgets(final Set<IWidget> widgets) {
        for (final IWidget widget : widgets) {
            final Document properties = widget.getProperties();
            if (extractJavaScript(properties)) {
                break;
            }
        }
    }

    private boolean extractJavaScript(final Document properties) {
        final List<Element> elementsFromNodeList = XMLUtils.getElementsFromNodeList(properties.getElementsByTagName("javascript"));
        if (elementsFromNodeList.isEmpty()) {
            setState(false);
            return true;
        }

        setState(true);

        final Element javaScriptPropertiesElement = elementsFromNodeList.get(0);
        final List<Element> javaScriptProperties = XMLUtils.getElementsFromNodeList(javaScriptPropertiesElement.getChildNodes());
        for (final Element element : javaScriptProperties) {
            final NodeList childNodes = element.getChildNodes();
            final String nodeValue = getNodeValue(childNodes);

            final String event = element.getNodeName();
            final String currentValue = eventsAndValues.get(event);
            if (currentValue == null) {
                eventsAndValues.put(event, nodeValue);
            } else if (!currentValue.equals(nodeValue)) {
                eventsAndValues.put(event, "mixed scripts in this event for the selected components");
            }
        }

        return false;
    }

    private String getNodeValue(final NodeList childNodes) {
        if (childNodes.getLength() == 0) {
            return "";
        }

        final Text textNode = (Text) childNodes.item(0);
        return textNode.getNodeValue();
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
