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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.pdf.forms.Configuration;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.readers.properties.DesignerPropertiesFile;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.components.PdfList;
import org.pdf.forms.widgets.components.SplitComponent;
import org.w3c.dom.Document;

@Disabled
class ListBoxWidgetTest {

    private JComponent baseComponent;
    private JComponent component;
    private FontHandler fontHandler;

    @BeforeEach
    void setUp() {
        final Configuration configuration = new Configuration();
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configuration.getConfigDirectory());
        this.fontHandler = new FontHandler(designerPropertiesFile);

        this.baseComponent = new SplitComponent("captionText", new PdfList(fontHandler), 1, fontHandler);
        this.component = new JCheckBox();
        component.setSize(1, 1);
    }

    @Test
    void persist_widget_into_xml() {
        final ListBoxWidget widget = new ListBoxWidget(IWidget.LIST_BOX,
                baseComponent,
                component,
                fontHandler);

        final Document document = widget.getProperties();
        assertThat(document, is(notNullValue()));

        final String serialize = XMLUtils.serialize(document);
        assertThat(serialize.length(), is(greaterThanOrEqualTo(2449)));
        assertThat(serialize.length(), is(lessThanOrEqualTo(2461)));
    }

    @Test
    void read_persisted_xml_into_widget() throws Exception {
        final ListBoxWidget widget = new ListBoxWidget(IWidget.LIST_BOX,
                baseComponent,
                component,
                new WidgetFileReader(getFile()).getWidget(),
                fontHandler);

        final Document document = widget.getProperties();
        assertThat(document, is(notNullValue()));

        final String serialize = XMLUtils.serialize(document);
        assertThat(serialize.length(), is(3693));
    }

    private File getFile() throws URISyntaxException {
        final URL url = ImageWidgetTest.class.getResource("listBoxWidget_persisted.xml");
        return new File(url.toURI());
    }
}
