package org.pdf.forms.readers.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.JAXBException;

import org.pdf.forms.model.configuration.WindowConfiguration;
import org.pdf.forms.readers.XmlJavaObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindowConfigurationFileReader {

    private final Logger logger = LoggerFactory.getLogger(WindowConfigurationFileReader.class);

    private final WindowConfiguration windowConfiguration;

    public WindowConfigurationFileReader(final File windowConfigurationFile) {
        this.windowConfiguration = parseFile(windowConfigurationFile);
    }

    WindowConfigurationFileReader(final String windowConfigurationContent) {
        this.windowConfiguration = readContent(windowConfigurationContent);
    }

    public WindowConfiguration getWindowConfiguration() {
        return windowConfiguration;
    }

    private WindowConfiguration parseFile(final File windowConfigurationFile) {
        try {
            final String content = Files.readString(windowConfigurationFile.toPath());
            return readContent(content);
        } catch (final IOException e) {
            logger.warn("Unable to read window configuration file.", e);
        }
        return WindowConfiguration.DEFAULT;

    }

    private WindowConfiguration readContent(final String content) {
        try {
            final XmlJavaObjectMapper<WindowConfiguration> mapper = new XmlJavaObjectMapper<>(WindowConfiguration.class);
            return mapper.convertXmlIntoObject(content);
        } catch (final JAXBException e) {
            logger.warn("Unable to read window configuration content.", e);
        }
        return WindowConfiguration.DEFAULT;
    }
}
