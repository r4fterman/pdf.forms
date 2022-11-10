package org.pdf.forms.readers.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.pdf.forms.model.configuration.WindowConfiguration;
import org.pdf.forms.readers.XmlJavaObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.JAXBException;

public class WindowConfigurationFile {

    private final Logger logger = LoggerFactory.getLogger(WindowConfigurationFile.class);
    private final File directory;

    public WindowConfigurationFile(final File directory) {
        this.directory = directory;
    }

    public WindowConfiguration getWindowConfiguration() {
        return new WindowConfigurationFileReader(getFile(directory))
                .getWindowConfiguration();
    }

    public void writeWindowConfiguration(
            final WindowConfiguration windowConfiguration,
            final File directory) {
        final File windowConfigurationFile = getFile(directory);
        try {
            final String xmlContent = convertWindowConfiguration(windowConfiguration);
            Files.writeString(windowConfigurationFile.toPath(),
                    xmlContent,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE_NEW);
        } catch (final IOException | JAXBException e) {
            logger.error("Unable to write configuration to file: {}", windowConfigurationFile);
        }
    }

    private String convertWindowConfiguration(final WindowConfiguration windowConfiguration) throws JAXBException {
        final XmlJavaObjectMapper<WindowConfiguration> mapper = new XmlJavaObjectMapper<>(WindowConfiguration.class);
        return mapper.convertObjectIntoXml(windowConfiguration);
    }

    private File getFile(final File directory) {
        return new File(directory, "windows.xml");
    }
}
