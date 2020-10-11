package org.pdf.forms.document;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Page {

    private final Logger logger = LoggerFactory.getLogger(Page.class);
    private final List<ButtonGroup> radioButtonGroups = new ArrayList<>();
    private final List<ButtonGroup> checkBoxGroups = new ArrayList<>();

    private final String pageName;

    private List<IWidget> widgets = new ArrayList<>();
    private int height;
    private int width;
    private int pdfPageNumber;
    private Document pageProperties;
    private String pdfFileLocation;

    private int rotation = 0;

    public Page(
            final String pageName,
            final int width,
            final int height) {
        this.width = width;
        this.height = height;

        this.pageName = pageName;
    }

    public Page(
            final String pageName,
            final String pdfFile,
            final int pdfPage) {
        this.pdfFileLocation = pdfFile;
        this.pdfPageNumber = pdfPage;

        this.pageName = pageName;
    }

    Document getPageProperties() {
        try {
            pageProperties = XMLUtils.createNewDocument();

            final Element rootElement = XMLUtils.createAndAppendElement(pageProperties, "page", pageProperties);

            final boolean isPdfFile = pdfFileLocation != null;
            final String value;
            if (isPdfFile) {
                value = "pdfpage";
            } else {
                value = "simplepage";
            }
            XMLUtils.addBasicProperty(pageProperties, "pagetype", value, rootElement);

            XMLUtils.addBasicProperty(pageProperties, "pagename", pageName, rootElement);

            final Element pageDate = XMLUtils.createAndAppendElement(pageProperties, "pagedata", rootElement);

            if (isPdfFile) {
                XMLUtils.addBasicProperty(pageProperties, "pdffilelocation", pdfFileLocation, pageDate);
                XMLUtils.addBasicProperty(pageProperties, "pdfpagenumber", pdfPageNumber + "", pageDate);
            } else {
                XMLUtils.addBasicProperty(pageProperties, "width", width + "", pageDate);
                XMLUtils.addBasicProperty(pageProperties, "height", height + "", pageDate);
            }

            for (final IWidget widget : widgets) {
                final Document widgetProperties = widget.getProperties();

                final Element widgetRoot = widgetProperties.getDocumentElement();

                pageProperties.getDocumentElement().appendChild(pageProperties.importNode(widgetRoot, true));
            }

            /* add radio button groups to the page */
            final Element radioButtonGroupsElement = XMLUtils.createAndAppendElement(pageProperties, "radiobuttongroups", rootElement);
            addButtonGroupsToPage(radioButtonGroupsElement, IWidget.RADIO_BUTTON);

            /* add check box groups to the page */
            final Element checkBoxGroupsElement = XMLUtils.createAndAppendElement(pageProperties, "checkboxgroups", rootElement);
            addButtonGroupsToPage(checkBoxGroupsElement, IWidget.CHECK_BOX);
        } catch (final ParserConfigurationException e) {
            logger.error("Error build page properties", e);
        }

        return pageProperties;
    }

    private void addButtonGroupsToPage(
            final Element radioButtonGroupsElement,
            final int type) {
        final List<ButtonGroup> buttonGroups;
        if (type == IWidget.RADIO_BUTTON) {
            buttonGroups = radioButtonGroups;
        } else {
            buttonGroups = checkBoxGroups;
        }

        for (final ButtonGroup buttonGroup : buttonGroups) {
            XMLUtils.addBasicProperty(pageProperties, "buttongroupname", buttonGroup.getName(), radioButtonGroupsElement);
        }
    }

    public int getPdfPageNumber() {
        return pdfPageNumber;
    }

    public String getPdfFileLocation() {
        return pdfFileLocation;
    }

    public String getPageName() {
        return pageName;
    }

    public List<IWidget> getWidgets() {
        return widgets;
    }

    public List<ButtonGroup> getRadioButtonGroups() {
        return radioButtonGroups;
    }

    public List<ButtonGroup> getCheckBoxGroups() {
        return checkBoxGroups;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(final int rotation) {
        this.rotation = rotation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Dimension getSize() {
        return new Dimension(width, height);
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public void setSize(final Dimension size) {
        this.width = size.width;
        this.height = size.height;
    }

    public void setWidgets(final List<IWidget> widgets) {
        this.widgets = widgets;
    }

    public ButtonGroup getCheckBoxGroup(final String groupName) {
        for (final Object group : checkBoxGroups) {
            final ButtonGroup buttonGroup = (ButtonGroup) group;
            final String bgName = buttonGroup.getName();
            if (bgName.equals(groupName)) {
                return buttonGroup;
            }
        }

        return null;
    }
}
