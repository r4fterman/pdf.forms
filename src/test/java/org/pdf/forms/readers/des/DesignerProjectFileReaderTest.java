package org.pdf.forms.readers.des;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.pdf.forms.model.des.DesDocument;
import org.pdf.forms.model.des.JavaScriptContent;
import org.pdf.forms.model.des.Page;
import org.pdf.forms.model.des.Property;
import org.pdf.forms.model.des.SizeAndPosition;
import org.pdf.forms.model.des.Widget;

class DesignerProjectFileReaderTest {

    @Test
    void read_des_document_from_file() throws Exception {
        final DesDocument desDocument = new DesignerProjectFileReader(getFile()).getDesDocument();

        assertJavaScriptContent(desDocument.getJavaScript());
        assertDocumentProperty(desDocument.getProperty());
        assertPages(desDocument.getPage());
    }

    private void assertJavaScriptContent(final JavaScriptContent javaScriptContent) {
        assertThat(javaScriptContent.getInitialize(), is(emptyString()));

        assertThat(javaScriptContent.getChange(), is(nullValue()));
        assertThat(javaScriptContent.getMouseDown(), is(nullValue()));
        assertThat(javaScriptContent.getMouseEnter(), is(nullValue()));
        assertThat(javaScriptContent.getMouseExit(), is(nullValue()));
        assertThat(javaScriptContent.getMouseUp(), is(nullValue()));
        assertThat(javaScriptContent.getKeystroke(), is(nullValue()));
    }

    private void assertDocumentProperty(final Property property) {
        assertThat(property.getName(), is("version"));
        assertThat(property.getValue(), is("0.8b05"));
    }

    private void assertPages(final List<Page> pages) {
        assertThat(pages, hasSize(2));

        assertPage0(pages.get(0));
        assertPage1(pages.get(1));
    }

    private void assertPage0(final Page page) {
        assertThat(page.getProperty(), hasSize(2));
        assertThat(page.getProperty().get(0).getName(), is("pagetype"));
        assertThat(page.getProperty().get(0).getValue(), is("simplepage"));

        assertThat(page.getPageData().getProperty(), hasSize(2));
        assertThat(page.getPageData().getProperty().get(0).getName(), is("width"));
        assertThat(page.getPageData().getProperty().get(0).getValue(), is("595"));

        assertThat(page.getRadioButtonGroups().getProperty(), hasSize(1));
        assertThat(page.getRadioButtonGroups().getProperty().get(0).getName(), is("buttongroupname"));
        assertThat(page.getRadioButtonGroups().getProperty().get(0).getValue(), is("Radio Button Group1"));

        assertThat(page.getCheckBoxGroups().getProperty(), hasSize(18));
        assertThat(page.getCheckBoxGroups().getProperty().get(17).getName(), is("buttongroupname"));
        assertThat(page.getCheckBoxGroups().getProperty().get(17).getValue(), is("CheckBox Group18"));

        assertThat(page.getWidget(), hasSize(48));
        assertWidget(page.getWidget().get(7));
    }

    private void assertPage1(final Page page) {
        assertThat(page.getProperty(), hasSize(2));
        assertThat(page.getProperty().get(0).getName(), is("pagetype"));
        assertThat(page.getProperty().get(0).getValue(), is("simplepage"));

        assertThat(page.getPageData().getProperty(), hasSize(2));
        assertThat(page.getPageData().getProperty().get(0).getName(), is("width"));
        assertThat(page.getPageData().getProperty().get(0).getValue(), is("595"));

        assertThat(page.getCheckBoxGroups().getProperty(), is(empty()));
        assertThat(page.getRadioButtonGroups().getProperty(), is(empty()));

        assertThat(page.getWidget(), hasSize(41));
        assertSize(page.getWidget().get(7), 207, 200, 74, 19);

    }

    private void assertWidget(final Widget widget) {
        assertThat(widget.getProperty(), hasSize(2));
        assertThat(widget.getProperties().getFont().getFontCaption().getProperty(), hasSize(6));
        assertThat(widget.getProperties().getFont().getFontValue().getProperty(), is(empty()));

        assertThat(widget.getProperties().getObject().getField().getProperty(), hasSize(3));
        assertThat(widget.getProperties().getObject().getValue().getProperty(), hasSize(2));
        assertThat(widget.getProperties().getObject().getBinding().getProperty(), hasSize(2));

        assertThat(widget.getProperties().getLayout().getSizeAndPosition().getProperty(), hasSize(8));
        assertThat(widget.getProperties().getLayout().getMargins().getProperty(), hasSize(4));
        assertThat(widget.getProperties().getLayout().getCaption().getProperty(), hasSize(2));

        assertThat(widget.getProperties().getBorder().getBorders().getProperty(), hasSize(3));
        assertThat(widget.getProperties().getBorder().getBackgroundFill().getProperty(), hasSize(2));

        assertThat(widget.getProperties().getParagraph().getParagraphCaption().getProperty(), hasSize(2));
        assertThat(widget.getProperties().getParagraph().getParagraphValue(), is(nullValue()));

        assertThat(widget.getProperties().getCaptionProperties().getProperty(), hasSize(2));

        assertThat(widget.getJavaScript().getKeystroke(), is(nullValue()));
        assertThat(widget.getJavaScript().getMouseEnter(), is(emptyString()));
        assertThat(widget.getJavaScript().getMouseExit(), is(emptyString()));
        assertThat(widget.getJavaScript().getChange(), is(emptyString()));
        assertThat(widget.getJavaScript().getMouseUp(), is(emptyString()));
        assertThat(widget.getJavaScript().getMouseDown(), is(emptyString()));

        assertSize(widget, 113, 157, 37, 22);
    }

    private void assertSize(
            final Widget widget,
            final int x,
            final int y,
            final int width,
            final int height) {
        final SizeAndPosition sizeAndPosition = widget.getProperties().getLayout().getSizeAndPosition();
        assertThat(sizeAndPosition.getX().map(Integer::valueOf).orElse(-1), is(x));
        assertThat(sizeAndPosition.getY().map(Integer::valueOf).orElse(-1), is(y));
        assertThat(sizeAndPosition.getWidth().map(Integer::valueOf).orElse(-1), is(width));
        assertThat(sizeAndPosition.getHeight().map(Integer::valueOf).orElse(-1), is(height));
    }

    private File getFile() throws URISyntaxException {
        final URL url = DesignerProjectFileReaderTest.class.getResource("/example.des");
        return new File(url.toURI());

    }
}
