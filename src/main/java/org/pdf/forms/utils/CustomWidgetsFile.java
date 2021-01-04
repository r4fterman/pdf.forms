package org.pdf.forms.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * holds values stored in XML file on disk.
 */
public final class CustomWidgetsFile extends PropertiesFile {

    private static CustomWidgetsFile instance;

    private final Logger logger = LoggerFactory.getLogger(CustomWidgetsFile.class);

    private CustomWidgetsFile(final File customWidgetFile) {
        super(customWidgetFile);
    }

    public static CustomWidgetsFile getInstance(final File configDir) {
        if (instance == null) {
            final File configFile = new File(configDir, ".custom_components.xml");

            instance = new CustomWidgetsFile(configFile);
        }

        return instance;
    }

    @Override
    public boolean checkAllElementsPresent() throws DOMException, ParserConfigurationException {
        //assume true and set to false if wrong
        boolean hasAllElements = true;

        final NodeList allElements = getDoc().getElementsByTagName("*");
        final List<String> elementsInTree = new ArrayList<>(allElements.getLength());

        for (int i = 0; i < allElements.getLength(); i++) {
            elementsInTree.add(allElements.item(i).getNodeName());
        }

        if (!elementsInTree.contains("custom_components")) {
            final Document doc = XMLUtils.createNewDocument();
            setDoc(doc);

            final Element customComponentElement = getDoc().createElement("custom_components");
            getDoc().appendChild(customComponentElement);

            hasAllElements = false;
        }

        return hasAllElements;
    }

    public boolean isNameTaken(final String name) {
        return XMLUtils.getElementsFromNodeList(getDoc().getElementsByTagName("custom_component")).stream()
                .map(element -> XMLUtils.getAttributeValueFromChildElement(element, "name"))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .anyMatch(text -> text.equals(name));
    }

    public void addCustomWidget(
            final String name,
            final Set<IWidget> selectedWidgets) {

        final Element customComponentElement = XMLUtils.createAndAppendElement(getDoc(), "custom_component", getDoc().getDocumentElement());

        XMLUtils.addBasicProperty(getDoc(), "name", name, customComponentElement);

        for (final IWidget widget : selectedWidgets) {
            final Document widgetProperties = widget.getProperties();

            final Element widgetRoot = widgetProperties.getDocumentElement();

            customComponentElement.appendChild(getDoc().importNode(widgetRoot, true));
        }

        try {
            writeDoc();
        } catch (final Exception e) {
            logger.error("Error writing custom widget file", e);
        }
    }

    @Override
    void destroy() {
        instance = null;
    }
}
