package org.pdf.forms.utils.configuration;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class WindowConfigurationFile extends ConfigurationFile {

    public static final String PROPERTIES = "PROPERTIES";
    public static final String LIBRARY = "LIBRARY";
    public static final String HIERARCHY = "HIERARCHY";
    public static final String SCRIPT_EDITOR = "SCRIPT_EDITOR";

    public WindowConfigurationFile(
            final File configDir,
            final File directory) {
        super(new File(configDir, "windows.xml"), directory);
    }

    public boolean isWindowVisible(final String windowCommand) {
        final Element windowConfigurationElement = getDocument().getDocumentElement();
        final NodeList windowElements = windowConfigurationElement.getElementsByTagName("window");

        for (int i = 0; i < windowElements.getLength(); i++) {
            final Element element = (Element) windowElements.item(i);
            final String command = element.getAttribute("command");
            if (command.equals(windowCommand)) {
                return isElementVisible(element);
            }
        }

        return false;
    }

    private boolean isElementVisible(final Element element) {
        final String visible = element.getAttribute("visible");
        return visible.equalsIgnoreCase("true");
    }

    @Override
    protected void writeToDefaultConfiguration(final Document document) {
        final Element windowConfigurationElement = document.createElement("window_configuration");
        document.appendChild(windowConfigurationElement);

        writeDefaultWindowConfiguration(document, windowConfigurationElement);
    }

    private void writeDefaultWindowConfiguration(
            final Document document,
            final Element windowConfigurationElement) {
        windowConfigurationElement.appendChild(createVisibleItem(document, "Script Editor", SCRIPT_EDITOR));
        windowConfigurationElement.appendChild(createVisibleItem(document, "Hierarchy", HIERARCHY));
        windowConfigurationElement.appendChild(createVisibleItem(document, "Library", LIBRARY));
        windowConfigurationElement.appendChild(createVisibleItem(document, "Properties", PROPERTIES));
    }

    private Element createVisibleItem(
            final Document document,
            final String name,
            final String command) {
        final Element item = document.createElement("window");
        item.setAttribute("name", name);
        item.setAttribute("visible", "true");
        item.setAttribute("command", command);
        return item;
    }
}
