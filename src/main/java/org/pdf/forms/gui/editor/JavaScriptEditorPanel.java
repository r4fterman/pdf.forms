/*
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 * <p>
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 * <p>
 * This file is part of the PDF Forms Designer
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * <p>
 * <p>
 * <p>
 * ---------------
 * JavaScriptEditorPanel.java
 * ---------------
 */
package org.pdf.forms.gui.editor;

import java.awt.Component;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

public class JavaScriptEditorPanel extends javax.swing.JPanel implements Dockable {

    private final DockKey key = new DockKey("Script Editor");

    private LinkedHashMap<String, String> eventsAndValues;

    private Set<IWidget> widgets;

    private javax.swing.JComboBox<String> eventBox;
    private javax.swing.JComboBox<String> languageBox;
    private javax.swing.JComboBox<String> runAtBox;
    private javax.swing.JTextArea scriptBox;

    /**
     * Creates new form JavaScriptEditorPanel.
     */
    public JavaScriptEditorPanel() {
        key.setFloatEnabled(true);
        initComponents();
    }

    private void initComponents() {
        final javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        scriptBox = new javax.swing.JTextArea();
        final javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        eventBox = new javax.swing.JComboBox<>();
        final javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        languageBox = new javax.swing.JComboBox<>();
        final javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        runAtBox = new javax.swing.JComboBox<>();
        final javax.swing.JSeparator jSeparator3 = new javax.swing.JSeparator();
        final javax.swing.JButton saveButton = new javax.swing.JButton();
        final javax.swing.JSeparator jSeparator1 = new javax.swing.JSeparator();

        scriptBox.setColumns(20);
        scriptBox.setRows(5);
        scriptBox.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jScrollPane1.setViewportView(scriptBox);

        jLabel1.setText("Show:");

        eventBox.addActionListener(this::eventChanged);

        jLabel2.setText("Language:");

        languageBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
                "JavaScript"
        }));

        jLabel3.setText("Run At:");

        runAtBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
                "Client"
        }));

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        saveButton.setText("Save");
        saveButton.addActionListener(this::saveClicked);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(eventBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 155, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jSeparator3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(saveButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 70, Short.MAX_VALUE)
                                .add(jLabel2)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(languageBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(runAtBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                        .add(jSeparator3)
                                        .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .add(eventBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .add(languageBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .add(runAtBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .add(saveButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
        );
    }

    private void saveClicked(final java.awt.event.ActionEvent evt) {
        if (widgets.size() == 1 && widgets.iterator().next() instanceof FormsDocument) {
            final FormsDocument document = (FormsDocument) widgets.iterator().next();
            saveJavaScript(document.getDocument());
        } else {
            saveJavaScriptInWidgets();
        }
    }

    private void eventChanged(final java.awt.event.ActionEvent evt) {
        scriptBox.setText(eventsAndValues.get(eventBox.getSelectedItem()));
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
                //TODO: widget is typed
                key.setName("Script Editor - Document");
                final FormsDocument document = (FormsDocument) widget;
                extractJavaScript(document.getDocument());
            } else {
                key.setName("Script Editor - " + widget.getWidgetName() + " Component");
                handleWidgets(widgets);
            }
        } else {
            key.setName("Script Editor - Multiple Components");
            handleWidgets(widgets);
        }

        for (final String event : eventsAndValues.keySet()) {
            eventBox.addItem(event);
        }
    }

    private void saveJavaScriptInWidgets() {
        for (final IWidget widget : widgets) {
            final Document document = widget.getProperties();

            saveJavaScript(document);
        }
    }

    private void saveJavaScript(final Document document) {
        final Element javaScriptPropertiesElement = XMLUtils.getElementsFromNodeList(
                document.getElementsByTagName("javascript")).get(0);

        final Element currentElement = XMLUtils.getElementsFromNodeList(
                javaScriptPropertiesElement.getElementsByTagName((String) eventBox.getSelectedItem())).get(0);

        Text currentTextNode = (Text) currentElement.getChildNodes().item(0);

        if (currentTextNode == null) {
            currentTextNode = document.createTextNode(scriptBox.getText());
            currentElement.appendChild(currentTextNode);
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
        } else {
            setState(true);
        }

        final Element javaScriptPropertiesElement = elementsFromNodeList.get(0);
        final List<Element> javaScriptProperties = XMLUtils.getElementsFromNodeList(javaScriptPropertiesElement.getChildNodes());

        for (final Element element : javaScriptProperties) {
            final String event = element.getNodeName();

            final NodeList childNodes = element.getChildNodes();

            final String nodeValue;
            if (childNodes.getLength() == 0) {
                nodeValue = "";
            } else {
                final Text textNode = (Text) childNodes.item(0);
                nodeValue = textNode.getNodeValue();
            }

            final String currentValue = eventsAndValues.get(event);
            if (currentValue == null) {
                eventsAndValues.put(event, nodeValue);
            } else if (!currentValue.equals(nodeValue)) {
                eventsAndValues.put(event, "mixed scripts in this event for the selected components");
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

        key.setName("Script Editor");
    }

    @Override
    public DockKey getDockKey() {
        return key;
    }

    @Override
    public Component getComponent() {
        return this;
    }
}
