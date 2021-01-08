package org.pdf.forms.utils.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.JAXBException;

import org.pdf.forms.gui.configuration.WindowConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindowConfigurationFileReader {

    private final Logger logger = LoggerFactory.getLogger(WindowConfigurationFileReader.class);

    private final File windowConfigurationFile;

    public WindowConfigurationFileReader(final File windowConfigurationFile) {
        this.windowConfigurationFile = windowConfigurationFile;
    }

    public WindowConfiguration getWindowConfiguration() {
        try {
            return parseFile(windowConfigurationFile);
        } catch (IOException | JAXBException e) {
            logger.error("Unable to read window configuration file.", e);
            return WindowConfiguration.DEFAULT;
        }
    }

    private WindowConfiguration parseFile(final File menuConfigurationFile) throws IOException, JAXBException {
        final String content = Files.readString(menuConfigurationFile.toPath());

        final XmlJavaObjectMapper<WindowConfiguration> mapper = new XmlJavaObjectMapper<>(WindowConfiguration.class);
        return mapper.convertXmlIntoObject(content);
    }
}
