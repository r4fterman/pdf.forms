package org.pdf.forms.readers.custom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.pdf.forms.model.components.CustomComponent;
import org.pdf.forms.model.components.CustomComponents;
import org.pdf.forms.model.des.Properties;

class CustomComponentsFileReaderTest {

    @Test
    void read_custom_component_document_from_file() throws Exception {
        final CustomComponents customComponents = new CustomComponentsFileReader(getFile())
                .getCustomComponents();

        assertThat(customComponents.getCustomComponentList(), hasSize(1));
        assertCustomComponent(customComponents.getCustomComponentList().get(0));
    }

    private void assertCustomComponent(final CustomComponent customComponent) {
        assertThat(customComponent.getProperty(), hasSize(1));
        assertThat(customComponent.getProperty().get(0).getName(), is("name"));
        assertThat(customComponent.getProperty().get(0).getValue(), is("foo"));

        assertThat(customComponent.getWidget(), hasSize(1));
        assertThat(customComponent.getWidget().get(0).getProperty(), hasSize(2));
        assertThat(customComponent.getWidget().get(0).getJavaScript(), is(nullValue()));
        assertWidgetProperties(customComponent.getWidget().get(0).getProperties());
    }

    private void assertWidgetProperties(final Properties properties) {
        assertThat(properties.getBorder().get().getBorders().getProperty(), hasSize(3));
        assertThat(properties.getBorder().get().getBackgroundFill().getProperty(), hasSize(2));
        assertThat(properties.getCaptionProperties().getProperty(), hasSize(1));
        assertThat(properties.getFont().get().getFontCaption().getProperty(), hasSize(6));
        assertThat(properties.getFont().get().getFontValue().getProperty(), is(empty()));
        assertThat(properties.getLayout().getCaption(), is(nullValue()));
        assertThat(properties.getLayout().getMargins().getProperty(), hasSize(4));
        assertThat(properties.getLayout().getSizeAndPosition().getProperty(), hasSize(8));
        assertThat(properties.getObject().getBinding(), is(nullValue()));
        assertThat(properties.getObject().getField().getProperty(), hasSize(1));
        assertThat(properties.getObject().getValue(), is(nullValue()));
        assertThat(properties.getParagraph().get().getParagraphCaption().getProperty(), hasSize(2));
        assertThat(properties.getParagraph().get().getParagraphValue(), is(nullValue()));
    }

    private File getFile() throws URISyntaxException {
        final URL url = CustomComponentsFileReaderTest.class.getResource("/.custom_components.xml");
        return new File(url.toURI());
    }
}
