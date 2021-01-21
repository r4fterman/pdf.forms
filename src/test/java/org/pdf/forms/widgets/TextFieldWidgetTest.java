package org.pdf.forms.widgets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
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
import org.pdf.forms.widgets.components.PdfTextField;
import org.pdf.forms.widgets.components.SplitComponent;
import org.w3c.dom.Document;

class TextFieldWidgetTest {

    private JComponent baseComponent;
    private JComponent component;
    private FontHandler fontHandler;

    @BeforeEach
    void setUp() {
        final Configuration configuration = new Configuration();
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configuration
                .getConfigDirectory());
        this.fontHandler = new FontHandler(designerPropertiesFile);

        this.baseComponent = new SplitComponent("captionText", new PdfTextField("text", fontHandler), 1, fontHandler);
        this.component = new JTextField();
        component.setSize(1, 1);
    }

    @Test
    void persist_widget_into_xml() {
        final TextFieldWidget widget = new TextFieldWidget(IWidget.TEXT_FIELD,
                baseComponent,
                component,
                fontHandler);
        final Document document = widget.getProperties();
        assertThat(document, is(notNullValue()));

        final String serialize = XMLUtils.serialize(document);
        assertThat(serialize.length(), is(greaterThanOrEqualTo(2659)));
        assertThat(serialize.length(), is(lessThanOrEqualTo(2671)));
    }

    @Test
    void read_persisted_xml_into_widget() throws Exception {
        final TextFieldWidget widget = new TextFieldWidget(IWidget.TEXT_FIELD,
                baseComponent,
                component,
                XMLUtils.readDocument(getFile()).getDocumentElement(),
                fontHandler);

        final Document document = widget.getProperties();
        assertThat(document, is(notNullValue()));

        final String serialize = XMLUtils.serialize(document);
        assertThat(serialize.length(), is(3959));
    }

    private File getFile() throws URISyntaxException {
        final URL url = TextFieldWidgetTest.class.getResource("textFieldWidget_persisted.xml");
        return new File(url.toURI());
    }
}
