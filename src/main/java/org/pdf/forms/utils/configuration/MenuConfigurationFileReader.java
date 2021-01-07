package org.pdf.forms.utils.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.JAXBException;

import org.pdf.forms.gui.configuration.MenuConfiguration;

public class MenuConfigurationFileReader {

    private final File menuConfigurationFile;

    public MenuConfigurationFileReader(final File menuConfigurationFile) {
        this.menuConfigurationFile = menuConfigurationFile;
    }

    public MenuConfiguration getMenuConfiguration() {
        try {
            return parseFile(menuConfigurationFile);
        } catch (IOException | JAXBException e) {
            throw new RuntimeException("Cannot parse menu configuration.", e);
        }
    }

    private MenuConfiguration parseFile(final File menuConfigurationFile) throws IOException, JAXBException {
        final String content = Files.readString(menuConfigurationFile.toPath());

        final XmlJavaObjectMapper<MenuConfiguration> mapper = new XmlJavaObjectMapper<>(MenuConfiguration.class);
        return mapper.convertXmlIntoObject(content);
    }
}
