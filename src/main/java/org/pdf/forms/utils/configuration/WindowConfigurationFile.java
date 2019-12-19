package org.pdf.forms.utils.configuration;

import java.io.File;

import org.pdf.forms.Configuration;
import org.pdf.forms.utils.XMLUtils;
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
            final Configuration configuration) {
        super(new File(configDir, "windows.xml"), configuration);
    }

    public boolean isWindowVisible(final String windowCommand) {
        final Element windowConfigurationElement = getDoc().getDocumentElement();
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
        return visible.toLowerCase().equals("true");
    }

    @Override
    protected void writeDefaultConfiguration() throws Exception {
        final Document doc = XMLUtils.createNewDocument();
        setDoc(doc);

        final Element windowConfigurationElement = getDoc().createElement("window_configuration");
        getDoc().appendChild(windowConfigurationElement);

        writeDefaultWindowConfiguration(windowConfigurationElement);
    }

    private void writeDefaultWindowConfiguration(final Element windowConfigurationElement) {
        windowConfigurationElement.appendChild(createVisibleItem("Script Editor", SCRIPT_EDITOR));
        windowConfigurationElement.appendChild(createVisibleItem("Hierarchy", HIERARCHY));
        windowConfigurationElement.appendChild(createVisibleItem("Library", LIBRARY));
        windowConfigurationElement.appendChild(createVisibleItem("Properties", PROPERTIES));
    }

    private Element createVisibleItem(
            final String name,
            final String command) {
        final Element item = getDoc().createElement("window");
        item.setAttribute("name", name);
        item.setAttribute("visible", "true");
        item.setAttribute("command", command);
        return item;
    }
}
