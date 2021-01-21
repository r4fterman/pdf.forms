package org.pdf.forms.widgets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pdf.forms.Configuration;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.readers.properties.DesignerPropertiesFile;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.components.PdfCaption;
import org.w3c.dom.Document;

class TextWidgetTest {

    private JComponent baseComponent;
    private JComponent component;
    private FontHandler fontHandler;

    @BeforeEach
    void setUp() {
        final Configuration configuration = new Configuration();
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configuration
                .getConfigDirectory());
        this.fontHandler = new FontHandler(designerPropertiesFile);

        this.baseComponent = new PdfCaption("caption", fontHandler);
        this.component = new JTextField();
        component.setSize(1, 1);
    }

    @Test
    void persist_widget_into_xml() {
        final TextWidget widget = new TextWidget(IWidget.TEXT, baseComponent, component, fontHandler);

        final Document document = widget.getProperties();
        assertThat(document, is(notNullValue()));

        final String serialize = XMLUtils.serialize(document);
        assertThat(serialize.length(), is(1557));
    }

    @Test
    void read_persisted_xml_into_widget() throws Exception {
        final TextWidget widget = new TextWidget(IWidget.TEXT,
                baseComponent,
                component,
                XMLUtils.readDocument(getFile()).getDocumentElement(),
                fontHandler);

        final Document document = widget.getProperties();
        assertThat(document, is(notNullValue()));

        final String serialize = XMLUtils.serialize(document);
        assertThat(serialize.length(), is(2327));
    }

    private File getFile() throws URISyntaxException {
        final URL url = TextFieldWidgetTest.class.getResource("textWidget_persisted.xml");
        return new File(url.toURI());
    }
}