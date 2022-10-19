package org.pdf.forms.writer.des;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.pdf.forms.model.des.DesDocument;
import org.pdf.forms.readers.XmlJavaObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.JAXBException;

public class DesignerProjectFileWriter {

    private final Logger logger = LoggerFactory.getLogger(DesignerProjectFileWriter.class);

    public void writeToFile(
            final DesDocument designerDocument,
            final File fileToWriteTo) {
        try {
            final XmlJavaObjectMapper<DesDocument> mapper = new XmlJavaObjectMapper<>(DesDocument.class);
            final String xml = mapper.convertObjectIntoXml(designerDocument);
            Files.writeString(fileToWriteTo.toPath(), xml, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException | JAXBException e) {
            logger.error("Error writing designer project to file {}", fileToWriteTo.getAbsolutePath(), e);
        }
    }
}
