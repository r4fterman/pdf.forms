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

    public boolean isWindowVisible(String windowCommand) {
        Element windowConfigurationElement = doc.getDocumentElement();
        NodeList windowElements = windowConfigurationElement.getElementsByTagName("window");

        for (int i = 0; i < windowElements.getLength(); i++) {
            Element element = (Element) windowElements.item(i);
            String command = element.getAttribute("command");
            if (command.equals(windowCommand)) {
                String visible = element.getAttribute("visible");
                return visible.toLowerCase().equals("true");
            }
        }

        return false;
    }

    protected void writeDefaultConfiguration() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        doc = db.newDocument();

        Element windowConfigurationElement = doc.createElement("window_configuration");
        doc.appendChild(windowConfigurationElement);

        writeDefaultWindowConfiguration(windowConfigurationElement);
    }

    private void writeDefaultWindowConfiguration(Element windowConfigurationElement) {
        addItemToXMLTree(windowConfigurationElement, "Script Editor", SCRIPT_EDITOR, "true");
        addItemToXMLTree(windowConfigurationElement, "Hierarchy", HIERARCHY, "true");
        addItemToXMLTree(windowConfigurationElement, "Library", LIBRARY, "true");
        addItemToXMLTree(windowConfigurationElement, "Properties", PROPERTIES, "true");
    }

    private void addItemToXMLTree(Element element, String name, String command, String visible) {
        Element item = doc.createElement("window");
        item.setAttribute("name", name);
        item.setAttribute("visible", visible);
        item.setAttribute("command", command);
        element.appendChild(item);
    }
}
