package org.pdf.forms.widgets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.JAXBException;

import org.pdf.forms.model.des.Widget;
import org.pdf.forms.readers.XmlJavaObjectMapper;

public class WidgetFileReader {

    private final Widget widget;

    public WidgetFileReader(final File file) throws IOException, JAXBException {
        final String content = Files.readString(file.toPath());

        final XmlJavaObjectMapper<org.pdf.forms.model.des.Widget> mapper = new XmlJavaObjectMapper<>(org.pdf.forms.model.des.Widget.class);
        this.widget = mapper.convertXmlIntoObject(content);
    }

    public Widget getWidget() {
        return widget;
    }
}
