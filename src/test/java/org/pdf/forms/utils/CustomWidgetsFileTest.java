package org.pdf.forms.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.file.Path;
import java.util.Set;

import javax.swing.JLabel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.TextWidget;
import org.pdf.forms.widgets.components.PdfCaption;

class CustomWidgetsFileTest {

    private CustomWidgetsFile customWidgetFile;

    @AfterEach
    void tearDown() {
        if (customWidgetFile != null) {
            customWidgetFile.destroy();
        }
    }

    @Test
    void checkAllElementsPresent_should_return_true_for_new_created_properties_file(@TempDir final Path configDir) throws Exception {
        customWidgetFile = CustomWidgetsFile.getInstance(configDir.toFile());

        assertThat(customWidgetFile.checkAllElementsPresent(), is(true));
    }

    @Test
    void isNameTaken_should_always_return_false_on_new_created_properties_file(@TempDir final Path configDir) {
        customWidgetFile = CustomWidgetsFile.getInstance(configDir.toFile());

        assertThat(customWidgetFile.isNameTaken("foo"), is(false));
    }

    @Disabled(value = "Does not work on headless Travis CI")
    void addCustomWidget_should_add_widget_component(@TempDir final Path configDir) {
        customWidgetFile = CustomWidgetsFile.getInstance(configDir.toFile());

        final PdfCaption pdfCaption = new PdfCaption("Text");
        final JLabel jLabel = new JLabel("Text");
        final TextWidget textWidget = new TextWidget(IWidget.TEXT, pdfCaption, jLabel);
        final Set<IWidget> widgets = Set.of(textWidget);
        customWidgetFile.addCustomWidget("foo", widgets);

        assertThat(customWidgetFile.isNameTaken("foo"), is(true));
    }

}
