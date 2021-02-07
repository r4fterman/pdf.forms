package org.pdf.forms.readers.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.JAXBException;

import org.pdf.forms.model.configuration.MenuConfiguration;
import org.pdf.forms.readers.XmlJavaObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuConfigurationFileReader {

    private final Logger logger = LoggerFactory.getLogger(MenuConfigurationFileReader.class);

    private final File menuConfigurationFile;

    public MenuConfigurationFileReader(final File menuConfigurationFile) {
        this.menuConfigurationFile = menuConfigurationFile;
    }

    public MenuConfiguration getMenuConfiguration() {
        try {
            return parseFile(menuConfigurationFile);
        } catch (IOException | JAXBException e) {
            logger.error("Cannot read menu configuration file {}. Use default value.", e.getMessage());
            return MenuConfiguration.DEFAULT;
        }
    }

    private MenuConfiguration parseFile(final File menuConfigurationFile) throws IOException, JAXBException {
        final String content = Files.readString(menuConfigurationFile.toPath());

        final XmlJavaObjectMapper<MenuConfiguration> mapper = new XmlJavaObjectMapper<>(MenuConfiguration.class);
        return mapper.convertXmlIntoObject(content);
    }
}
