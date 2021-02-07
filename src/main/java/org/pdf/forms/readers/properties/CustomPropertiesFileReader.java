package org.pdf.forms.readers.properties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.JAXBException;

import org.pdf.forms.model.properties.CustomProperties;
import org.pdf.forms.readers.XmlJavaObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomPropertiesFileReader {

    private final Logger logger = LoggerFactory.getLogger(CustomPropertiesFileReader.class);

    private final File propertiesFile;

    public CustomPropertiesFileReader(final File propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    public CustomProperties getCustomProperties() {
        try {
            return parseFile(propertiesFile);
        } catch (IOException | JAXBException e) {
            logger.error("Cannot read properties file: {}. Use default value.", e.getMessage());
            return new CustomProperties();
        }
    }

    private CustomProperties parseFile(final File propertiesFile) throws IOException, JAXBException {
        final String content = Files.readString(propertiesFile.toPath());

        final XmlJavaObjectMapper<CustomProperties> mapper = new XmlJavaObjectMapper<>(CustomProperties.class);
        return mapper.convertXmlIntoObject(content);
    }
}
