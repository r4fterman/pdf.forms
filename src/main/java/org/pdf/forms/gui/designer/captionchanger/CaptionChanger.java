/*
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 *
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 *
 * 	This file is part of the PDF Forms Designer
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
import javax.swing.JTextPane;

import org.jpedal.utils.Strip;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.components.PdfCaption;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CaptionChanger {

    private static final String HTML_LINE_BREAK = "<br>";
    private static final String STRING_LINE_BREAK = "\n";

    private JTextPane textArea;

    private IWidget selectedWidget;

    private IDesigner designerPanel;
    private String alignment;

    public void displayCaptionChanger(
            final IWidget selectedWidget,
            final IDesigner designerPanel) {

        this.selectedWidget = selectedWidget;
        this.designerPanel = designerPanel;

        final PdfCaption captionComponent = selectedWidget.getCaptionComponent();

        final Rectangle captionBounds = captionComponent.getBounds();

        textArea = new JTextPane();

        // setting editor kit to CenterText puts the text in the vertical center of the text field
        textArea.setEditorKit(new CenterText());

        String captionText = captionComponent.getText();

        alignment = getAlignment(captionText);

        // insert line breaks
        captionText = captionText.replaceAll(HTML_LINE_BREAK, STRING_LINE_BREAK);
        captionText = Strip.stripXML(captionText).toString();

        textArea.setText(captionText);
        textArea.selectAll();

        textArea.setBorder(BorderFactory.createLineBorder(Color.black));

        final Point captionLocation = selectedWidget.getAbsoluteLocationsOfCaption();

        final int absoluteX = captionLocation.x;
        final int absoluteY = captionLocation.y;

        textArea.setBounds(absoluteX, absoluteY, captionBounds.width, captionBounds.height);

        designerPanel.add(textArea);

        textArea.requestFocus();
    }

    private String getAlignment(final String captionText) {

        int start = captionText.indexOf("align");
        if (start != -1) {
            start += 6;
            int end = start;

            while (captionText.charAt(end) != '>') {
                end++;
            }

            return captionText.substring(start, end);
        }

        return null;
    }

    public void closeCaptionChanger() {
        if (textArea == null || !textArea.isVisible()) {
            return;
        }

        setCaptionText();

        selectedWidget.setSize(selectedWidget.getWidth(), selectedWidget.getHeight());
    }

    private void setCaptionText() {
        String captionText = textArea.getText();

        final String alignment = this.alignment == null ? "" : "<p align=" + this.alignment + ">";
        captionText = "<html>" + alignment + captionText.replaceAll(STRING_LINE_BREAK, HTML_LINE_BREAK);

        final Document properties = selectedWidget.getProperties();
        final Element captionProperties =
                XMLUtils.getElementsFromNodeList(properties.getElementsByTagName("caption_properties"))
                        .get(0);

        final Element textElement = XMLUtils.getPropertyElement(captionProperties, "Text");

        textElement.getAttributeNode("value").setValue(captionText);

        selectedWidget.setCaptionProperties(captionProperties);

        textArea.setText("");
        textArea.setVisible(false);

        designerPanel.repaint();
    }
}

