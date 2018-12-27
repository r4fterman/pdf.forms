package org.pdf.forms.utils.configuration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class WindowConfiguration extends ConfigurationFile {

    public static final String PROPERTIES = "PROPERTIES";
    public static final String LIBRARY = "LIBRARY";
    public static final String HIERARCHY = "HIERARCHY";
    public static final String SCRIPT_EDITOR = "SCRIPT_EDITOR";

    public WindowConfiguration() {
        super("windows.xml");
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
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();

        setDoc(db.newDocument());

        final Element windowConfigurationElement = getDoc().createElement("window_configuration");
        getDoc().appendChild(windowConfigurationElement);

        writeDefaultWindowConfiguration(windowConfigurationElement);
    }

    private void writeDefaultWindowConfiguration(final Element windowConfigurationElement) {
        addItemToXMLTree(windowConfigurationElement, "Script Editor", SCRIPT_EDITOR, "true");
        addItemToXMLTree(windowConfigurationElement, "Hierarchy", HIERARCHY, "true");
        addItemToXMLTree(windowConfigurationElement, "Library", LIBRARY, "true");
        addItemToXMLTree(windowConfigurationElement, "Properties", PROPERTIES, "true");
    }

    private void addItemToXMLTree(
            final Element element,
            final String name,
            final String command,
            final String visible) {
        final Element item = getDoc().createElement("window");
        item.setAttribute("name", name);
        item.setAttribute("visible", visible);
        item.setAttribute("command", command);
        element.appendChild(item);
    }
}
