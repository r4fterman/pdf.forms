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
import org.pdf.forms.widgets.components.SplitComponent;
import org.w3c.dom.Document;

class ComboBoxWidgetTest {

    private FontHandler fontHandler;
    private DesignerPropertiesFile designerPropertiesFile;
    private JComponent baseComponent;
    private JComponent component;

    @BeforeEach
    void setUp() {
        final Configuration configuration = new Configuration();
        this.designerPropertiesFile = new DesignerPropertiesFile(configuration
                .getConfigDirectory());
        this.fontHandler = new FontHandler(designerPropertiesFile);

        this.baseComponent = new SplitComponent("captionText", new JComboBox<>(), 1, fontHandler);
        this.component = new JComboBox<>();
        component.setSize(1, 1);
    }

    @Test
    void persist_widget_into_xml() {
        final ComboBoxWidget widget = new ComboBoxWidget(IWidget.COMBO_BOX,
                baseComponent,
                component,
                fontHandler);

        final Document document = widget.getProperties();
        assertThat(document, is(notNullValue()));

        final String serializedComboBox = XMLUtils.serialize(document);
        assertThat(serializedComboBox.length(), is(2550));
    }

    @Test
    void read_persisted_xml_into_widget() throws Exception {
        final ComboBoxWidget widget = new ComboBoxWidget(IWidget.COMBO_BOX,
                baseComponent,
                component,
                XMLUtils.readDocument(getFile()).getDocumentElement(),
                new FontHandler(designerPropertiesFile));

        final Document document = widget.getProperties();
        assertThat(document, is(notNullValue()));

        final String serialize = XMLUtils.serialize(document);
        assertThat(serialize.length(), is(3807));
    }

    private File getFile() throws URISyntaxException {
        final URL url = ComboBoxWidgetTest.class.getResource("comboBoxWidget_persisted.xml");
        return new File(url.toURI());
    }
}