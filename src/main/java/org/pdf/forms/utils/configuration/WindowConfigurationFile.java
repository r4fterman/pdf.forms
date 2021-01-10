package org.pdf.forms.utils.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import javax.xml.bind.JAXBException;

import org.pdf.forms.gui.configuration.WindowConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindowConfigurationFile {

    public static final String PROPERTIES = "PROPERTIES";
    public static final String LIBRARY = "LIBRARY";
    public static final String HIERARCHY = "HIERARCHY";
    public static final String SCRIPT_EDITOR = "SCRIPT_EDITOR";

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
