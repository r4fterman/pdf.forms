package org.pdf.forms.utils.configuration;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

class XmlJavaObjectMapper<T> {

    private final Class<T> typeOfT;

    XmlJavaObjectMapper(final Class<T> typeOfT) {
        this.typeOfT = typeOfT;
    }

    T convertXmlIntoObject(final String xmlValue) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(typeOfT);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xmlValue));
    }

    String convertObjectIntoXml(final T value) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(typeOfT);
        final Marshaller marshaller = context.createMarshaller();
        final StringWriter writer = new StringWriter();
        marshaller.marshal(value, writer);
        return writer.toString();
    }
}
