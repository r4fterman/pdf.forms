package org.pdf.forms.widgets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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

@Disabled
class ImageWidgetTest {

    private JComponent baseComponent;
    private JComponent component;
    private DesignerPropertiesFile designerPropertiesFile;
    private FontHandler fontHandler;

    @BeforeEach
    void setUp() {
        final Configuration configuration = new Configuration();
        this.designerPropertiesFile =
                new DesignerPropertiesFile(configuration.getConfigDirectory());
        this.fontHandler = new FontHandler(designerPropertiesFile);

        this.baseComponent = new JLabel("label");
        this.component = new JCheckBox();
    }

    @Test
    void persist_widget_into_xml() throws Exception {
        final ImageWidget widget = new ImageWidget(IWidget.IMAGE,
                baseComponent,
                component,
                new FontHandler(designerPropertiesFile));

        final String serialize = new WidgetFileWriter(widget.getWidgetModel()).serialize();
        assertThat(serialize.length(), is(851));
    }

    @Test
    void read_persisted_xml_into_widget() throws Exception {
        final ImageWidget widget = new ImageWidget(IWidget.IMAGE,
                baseComponent,
                component,
                new WidgetFileReader(getFile()).getWidget(),
                fontHandler);

        final String serialize = new WidgetFileWriter(widget.getWidgetModel()).serialize();
        assertThat(serialize.length(), is(1271));
    }

    private File getFile() throws URISyntaxException {
        final URL url = ImageWidgetTest.class.getResource("imageWidget_persisted.xml");
        return new File(url.toURI());
    }
}
