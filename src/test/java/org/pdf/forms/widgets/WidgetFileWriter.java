package org.pdf.forms.widgets;

import javax.xml.bind.JAXBException;

import org.pdf.forms.model.des.Widget;
import org.pdf.forms.readers.XmlJavaObjectMapper;

public class WidgetFileWriter {

    private final Widget widget;

    public WidgetFileWriter(final Widget widget) {
        this.widget = widget;
    }

    public String serialize() throws JAXBException {
        final XmlJavaObjectMapper<Widget> mapper = new XmlJavaObjectMapper<>(Widget.class);
        return mapper.convertObjectIntoXml(widget);
    }
}
