package org.pdf.forms.utils.configuration;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

class XmlJavaObjectMapper<T> {

    private final Class<T> typeOfT;

    XmlJavaObjectMapper(final Class<T> typeOfT) {
        this.typeOfT = typeOfT;
    }

    T convertXmlIntoObject(final String xmlValue) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(typeOfT);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final Object object = unmarshaller.unmarshal(new StringReader(xmlValue));
        return (T) object;
    }
}
