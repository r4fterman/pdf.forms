package org.pdf.forms.readers;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

public class XmlJavaObjectMapper<T> {

    private final Class<T> typeOfT;

    public XmlJavaObjectMapper(final Class<T> typeOfT) {
        this.typeOfT = typeOfT;
    }

    public T convertXmlIntoObject(final String xmlValue) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(typeOfT);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xmlValue));
    }

    public String convertObjectIntoXml(final T value) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(typeOfT);
        final Marshaller marshaller = context.createMarshaller();
        configureMarshaller(marshaller);

        final StringWriter writer = new StringWriter();
        marshaller.marshal(value, writer);
        return writer.toString();
    }

    private void configureMarshaller(final Marshaller marshaller) throws PropertyException {
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    }
}
