package org.pdf.forms.utils;

import java.io.File;
import java.util.Optional;
import java.util.Set;

import javax.xml.transform.TransformerException;

import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * holds values stored in XML file on disk.
 */
public final class CustomWidgetsFile extends PropertiesFile {

    public static CustomWidgetsFile getInstance(final File directory) {
        return new CustomWidgetsFile(new File(directory, ".custom_components.xml"));
    }

    private final Logger logger = LoggerFactory.getLogger(CustomWidgetsFile.class);

    private CustomWidgetsFile(final File customWidgetFile) {
        super(customWidgetFile);
    }

    @Override
    public boolean checkForModelUpdate(final Document document) {
        boolean modelUpdated = false;

        if (isElementMissing(document, "custom_components")) {
            createNewModelInstance(document);
            updateElementInModel(document, "custom_components");
            modelUpdated = true;
        }

        return modelUpdated;
    }

    public boolean isNameTaken(final String name) {
        return XMLUtils.getElementsFromNodeList(getDocument().getElementsByTagName("custom_component")).stream()
                .map(element -> XMLUtils.getAttributeValueFromChildElement(element, "name"))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .anyMatch(text -> text.equals(name));
    }

    public void addCustomWidget(
            final String name,
            final Set<IWidget> selectedWidgets) {

        final Element customComponentElement = XMLUtils.createAndAppendElement(getDocument(),
                "custom_component",
                getDocument().getDocumentElement());
        XMLUtils.addBasicProperty(getDocument(), "name", name, customComponentElement);

        for (final IWidget widget: selectedWidgets) {
            final Document widgetProperties = widget.getProperties();
            final Element widgetRoot = widgetProperties.getDocumentElement();
            customComponentElement.appendChild(getDocument().importNode(widgetRoot, true));
        }

        try {
            writeDoc();
        } catch (TransformerException e) {
            logger.error("Error writing custom widget file", e);
        }
    }
}
