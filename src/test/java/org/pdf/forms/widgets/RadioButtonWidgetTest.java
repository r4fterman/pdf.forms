package org.pdf.forms.widgets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

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
import org.pdf.forms.widgets.components.SplitComponent;

@Disabled
class RadioButtonWidgetTest {

    private JComponent baseComponent;
    private JComponent component;
    private FontHandler fontHandler;

    @BeforeEach
    void setUp() {
        final Configuration configuration = new Configuration();
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configuration
                .getConfigDirectory());
        this.fontHandler = new FontHandler(designerPropertiesFile);

        this.baseComponent = new SplitComponent("captionText", new JRadioButton(), 1, fontHandler);
        this.component = new JRadioButton();
        component.setSize(1, 1);
    }

    @Test
    void persist_widget_into_xml() throws Exception {
        final RadioButtonWidget widget = new RadioButtonWidget(IWidget.RADIO_BUTTON,
                baseComponent,
                component,
                fontHandler);
        final String serialize = new WidgetFileWriter(widget.getWidgetModel()).serialize();
        assertThat(serialize.length(), is(greaterThanOrEqualTo(2101)));
        assertThat(serialize.length(), is(lessThanOrEqualTo(2107)));
    }

    @Test
    void read_persisted_xml_into_widget() throws Exception {
        final RadioButtonWidget widget = new RadioButtonWidget(IWidget.RADIO_BUTTON,
                baseComponent,
                component,
                new WidgetFileReader(getFile()).getWidget(),
                fontHandler);
        final String serialize = new WidgetFileWriter(widget.getWidgetModel()).serialize();
        assertThat(serialize.length(), is(3153));
    }

    private File getFile() throws URISyntaxException {
        final URL url = RadioButtonWidgetTest.class.getResource("radioButtonWidget_persisted.xml");
        return new File(url.toURI());
    }
}
