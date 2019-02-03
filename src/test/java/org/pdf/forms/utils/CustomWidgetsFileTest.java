package org.pdf.forms.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.file.Path;
import java.util.Set;

import javax.swing.JLabel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.support.io.TempDirectory;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.TextWidget;
import org.pdf.forms.widgets.components.PdfCaption;

@ExtendWith(TempDirectory.class)
class CustomWidgetsFileTest {

    private CustomWidgetsFile customWidgetFile;

    @AfterEach
    void tearDown() {
        if (customWidgetFile != null) {
            customWidgetFile.destroy();
        }
    }

    @Test
    void checkAllElementsPresent_should_return_true_for_new_created_properties_file(@TempDirectory.TempDir final Path configDir) throws Exception {
        customWidgetFile = CustomWidgetsFile.getInstance(configDir.toFile());

        assertThat(customWidgetFile.checkAllElementsPresent(), is(true));
    }

    @Test
    void isNameTaken_should_always_return_false_on_new_created_properties_file(@TempDirectory.TempDir final Path configDir) {
        customWidgetFile = CustomWidgetsFile.getInstance(configDir.toFile());

        assertThat(customWidgetFile.isNameTaken("foo"), is(false));
    }

    @Test
    void addCustomWidget_should_add_widget_component(@TempDirectory.TempDir final Path configDir) {
        customWidgetFile = CustomWidgetsFile.getInstance(configDir.toFile());

        customWidgetFile.addCustomWidget("foo", Set.of(new TextWidget(IWidget.TEXT, new PdfCaption("Text"), new JLabel("Text"))));

        assertThat(customWidgetFile.isNameTaken("foo"), is(true));
    }

}
