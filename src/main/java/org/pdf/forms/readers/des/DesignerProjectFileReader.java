package org.pdf.forms.readers.des;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.pdf.forms.model.des.DesDocument;
import org.pdf.forms.readers.XmlJavaObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.JAXBException;

public class DesignerProjectFileReader {

    private final Logger logger = LoggerFactory.getLogger(DesignerProjectFileReader.class);

    private final File desFile;

    public DesignerProjectFileReader(final File desFile) {
        this.desFile = desFile;
    }

    public DesDocument getDesDocument() {
        try {
            return parseFile(desFile);
        } catch (IOException | JAXBException e) {
            logger.error("Cannot read des file {}. Use default value.", e.getMessage());
            return new DesDocument();
        }
    }

    private DesDocument parseFile(final File desFile) throws IOException, JAXBException {
        final String content = Files.readString(desFile.toPath());

        final XmlJavaObjectMapper<DesDocument> mapper = new XmlJavaObjectMapper<>(DesDocument.class);
        return mapper.convertXmlIntoObject(content);
    }
}
