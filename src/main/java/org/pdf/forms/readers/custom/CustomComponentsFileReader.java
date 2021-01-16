package org.pdf.forms.readers.custom;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.JAXBException;

import org.pdf.forms.model.components.CustomComponents;
import org.pdf.forms.readers.XmlJavaObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomComponentsFileReader {

    private final Logger logger = LoggerFactory.getLogger(CustomComponentsFileReader.class);

    private final File customComponentsFile;

    public CustomComponentsFileReader(final File customComponentsFile) {
        this.customComponentsFile = customComponentsFile;
    }

    public CustomComponents getCustomComponents() {
        try {
            return parseFile(customComponentsFile);
        } catch (IOException | JAXBException e) {
            logger.error("Cannot read properties file: {}. Use default value.", e.getMessage());
            return new CustomComponents();
        }
    }

    private CustomComponents parseFile(final File propertiesFile) throws IOException, JAXBException {
        final String content = Files.readString(propertiesFile.toPath());

        final XmlJavaObjectMapper<CustomComponents> mapper = new XmlJavaObjectMapper<>(CustomComponents.class);
        return mapper.convertXmlIntoObject(content);
    }
}
