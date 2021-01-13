package org.pdf.forms.readers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.JAXBException;

import org.pdf.forms.model.des.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesFileReader {

    private final Logger logger = LoggerFactory.getLogger(DesFileReader.class);

    private final File desFile;

    public DesFileReader(final File desFile) {
        this.desFile = desFile;
    }

    public Document getDesDocument() {
        try {
            return parseFile(desFile);
        } catch (IOException | JAXBException e) {
            logger.error("Cannot read des file.", e);
            return Document.DEFAULT;
        }
    }

    private Document parseFile(final File desFile) throws IOException, JAXBException {
        final String content = Files.readString(desFile.toPath());

        final XmlJavaObjectMapper<Document> mapper = new XmlJavaObjectMapper<>(Document.class);
        return mapper.convertXmlIntoObject(content);
    }
}
