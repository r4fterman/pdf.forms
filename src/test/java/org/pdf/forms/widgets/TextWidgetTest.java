package org.pdf.forms.widgets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import javax.swing.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pdf.forms.Configuration;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.readers.properties.DesignerPropertiesFile;
import org.pdf.forms.utils.XMLUtils;
import org.w3c.dom.Document;

class TextWidgetTest {

    private TextWidget widget;

    @BeforeEach
    void setUp() {
        final Configuration configuration = new Configuration();
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configuration.getConfigDirectory());

        final JComponent baseComponent = new JCheckBox();
        final JComponent component = new JCheckBox();
        this.widget = new TextWidget(IWidget.TEXT, baseComponent, component, new FontHandler(designerPropertiesFile));
    }

    @Test
    void persist_widget_into_xml() {
        final Document document = widget.getProperties();
        assertThat(document, is(notNullValue()));

        final String serialize = XMLUtils.serialize(document);
        assertThat(serialize.length(), is(1557));
        System.out.println(serialize);
    }
}