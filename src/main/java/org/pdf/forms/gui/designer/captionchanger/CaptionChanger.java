/*
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 *
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 *
 * This file is part of the PDF Forms Designer
 *
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


 *
 * ---------------
 * CaptionChanger.java
 * ---------------
 */
package org.pdf.forms.gui.designer.captionchanger;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.jpedal.utils.Strip;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.components.PdfCaption;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CaptionChanger {

    private static final String HTML_LINE_BREAK = "<br/>";
    private static final String STRING_LINE_BREAK = "\n";

    private JTextPane textArea;
    private JScrollPane scroll;

    private IWidget selectedWidget;

    private IDesigner designerPanel;
    private String alignment;

    public void displayCaptionChanger(
            final IWidget selectedWidget,
            final IDesigner designerPanel) {
        this.selectedWidget = selectedWidget;
        this.designerPanel = designerPanel;

        final PdfCaption captionComponent = selectedWidget.getCaptionComponent();
        final String captionText = captionComponent.getText();
        alignment = getAlignment(captionText);

        textArea = createTextArea(captionText);
        textArea.setFont(captionComponent.getFont());

        final Point captionLocation = selectedWidget.getAbsoluteLocationsOfCaption();
        final Rectangle captionBounds = captionComponent.getBounds();
        if (captionText.contains("<br")) {
            this.scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            setBounds(scroll, captionLocation, captionBounds);
            designerPanel.add(scroll);
        } else {
            setBounds(textArea, captionLocation, captionBounds);
            designerPanel.add(textArea);
        }

        textArea.requestFocus();
    }

    String getAlignment(final String captionText) {
        final String align = "align=";

        final int idx = captionText.indexOf(align);
        if (idx == -1) {
            return null;
        }

        final int start = idx + align.length();
        final int end = captionText.indexOf('>', start);

        // Fix for: align====left
        String text = captionText.substring(start, end);
        while (text.startsWith("=")) {
            text = text.substring(1);
        }
        return text;
    }

    public void closeCaptionChanger() {
        if (textArea == null || !textArea.isVisible()) {
            return;
        }

        setCaptionText();

        selectedWidget.setSize(selectedWidget.getWidth(), selectedWidget.getHeight());
    }

    private void setCaptionText() {
        final String captionText = textArea.getText().replaceAll(STRING_LINE_BREAK, HTML_LINE_BREAK);

        final StringBuilder builder = new StringBuilder("<html>");
        if (alignment == null) {
            builder.append(captionText);
        } else {
            builder
                    .append("<p align=").append(alignment).append(">")
                    .append(captionText)
                    .append("</p>");
        }
        final String text = builder.toString();

        final Document properties = selectedWidget.getProperties();
        final Element captionProperties =
                XMLUtils.getElementsFromNodeList(properties.getElementsByTagName("caption_properties"))
                        .get(0);

        final Element textElement = XMLUtils.getPropertyElement(captionProperties, "Text");

        textElement.getAttributeNode("value").setValue(text);

        selectedWidget.setCaptionProperties(captionProperties);

        textArea.setText("");
        textArea.setVisible(false);
        if (scroll != null) {
            scroll.setVisible(false);
        }

        designerPanel.repaint();
    }

    private void setBounds(
            final JComponent component,
            final Point location,
            final Rectangle bounds) {
        final int absoluteX = location.x;
        final int absoluteY = location.y;

        component.setBounds(absoluteX, absoluteY, bounds.width, bounds.height);
    }

    private JTextPane createTextArea(final String text) {
        final JTextPane textArea = new JTextPane();
        textArea.setBorder(BorderFactory.createLineBorder(Color.black));
        textArea.setEditorKit(new CenterText());

        textArea.setText(prepareTextForEditing(text));
        textArea.selectAll();

        return textArea;
    }

    private String prepareTextForEditing(final String text) {
        final String textWithEditingLineBreaks = text
                .replaceAll("<br>", HTML_LINE_BREAK)
                .replaceAll(HTML_LINE_BREAK, STRING_LINE_BREAK);

        return Strip.stripXML(textWithEditingLineBreaks).toString();

    }
}

