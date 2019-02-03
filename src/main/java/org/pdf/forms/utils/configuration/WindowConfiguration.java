package org.pdf.forms.utils.configuration;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class WindowConfiguration extends ConfigurationFile {

    public static final String PROPERTIES = "PROPERTIES";
    public static final String LIBRARY = "LIBRARY";
    public static final String HIERARCHY = "HIERARCHY";
    public static final String SCRIPT_EDITOR = "SCRIPT_EDITOR";

    public WindowConfiguration(final File configDir) {
        super(new File(configDir, "windows.xml"));
    }

    public boolean isWindowVisible(final String windowCommand) {
        final Element windowConfigurationElement = getDoc().getDocumentElement();
        final NodeList windowElements = windowConfigurationElement.getElementsByTagName("window");

        for (int i = 0; i < windowElements.getLength(); i++) {
            final Element element = (Element) windowElements.item(i);
            final String command = element.getAttribute("command");
            if (command.equals(windowCommand)) {
                final String visible = element.getAttribute("visible");
                return visible.toLowerCase().equals("true");
            }
        }

        return false;
    }

    @Override
    protected void writeDefaultConfiguration() throws Exception {
        final DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        setDoc(documentBuilder.newDocument());

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
