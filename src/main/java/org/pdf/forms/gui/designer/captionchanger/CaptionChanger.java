package org.pdf.forms.gui.designer.captionchanger;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

import java.awt.*;

import javax.swing.*;

import org.apache.commons.text.StringEscapeUtils;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.components.PdfCaption;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CaptionChanger {

    private static final String HTML_LINE_BREAK = "<br/>";
    private static final String STRING_LINE_BREAK = "\n";

    private JTextPane captionTextArea;
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

        captionTextArea = createTextArea(captionText);
        captionTextArea.setFont(captionComponent.getFont());

        final Point captionLocation = selectedWidget.getAbsoluteLocationsOfCaption();
        final Rectangle captionBounds = captionComponent.getBounds();
        if (captionText.contains("<br")) {
            this.scroll = new JScrollPane(captionTextArea, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_AS_NEEDED);
            setBounds(scroll, captionLocation, captionBounds);
            designerPanel.add(scroll);
        } else {
            setBounds(captionTextArea, captionLocation, captionBounds);
            designerPanel.add(captionTextArea);
        }

        captionTextArea.requestFocus();
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
        if (captionTextArea == null || !captionTextArea.isVisible()) {
            return;
        }

        setCaptionText();

        selectedWidget.setSize(selectedWidget.getWidth(), selectedWidget.getHeight());
    }

    private void setCaptionText() {
        final String userEnteredText = captionTextArea.getText();
        final String sanitizedXml = StringEscapeUtils.escapeXml11(userEnteredText);
        final String captionText = sanitizedXml.replace(STRING_LINE_BREAK, HTML_LINE_BREAK);

        final StringBuilder builder = new StringBuilder("<html>");
        if (alignment == null) {
            builder.append(captionText);
        } else {
            builder
                    .append("<p align=").append(alignment).append(">")
                    .append(captionText)
                    .append("</p>");
        }
        builder.append("</html>");
        final String text = builder.toString();

        final Document properties = selectedWidget.getProperties();
        final Element captionProperties =
                XMLUtils.getElementsFromNodeList(properties.getElementsByTagName("caption_properties")).get(0);

        XMLUtils.getPropertyElement(captionProperties, "Text")
                .ifPresent(textElement -> textElement.getAttributeNode("value").setValue(text));

        selectedWidget.setCaptionProperties(captionProperties);

        captionTextArea.setText("");
        captionTextArea.setVisible(false);
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
                .replace("<br>", HTML_LINE_BREAK)
                .replace(HTML_LINE_BREAK, STRING_LINE_BREAK);

        // &lt; --> <
        return StringEscapeUtils.unescapeXml(textWithEditingLineBreaks);
    }
}

