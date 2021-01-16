package org.pdf.forms.readers.custom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.file.Path;
import java.util.Set;

import javax.swing.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.readers.des.DesignerPropertiesFile;
import org.pdf.forms.utils.CustomWidgetsFile;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.TextWidget;
import org.pdf.forms.widgets.components.PdfCaption;

class CustomWidgetsFileTest {

    @Test
    void checkAllElementsPresent_should_return_true_for_new_created_properties_file(@TempDir final Path configDir) {
        final CustomWidgetsFile customWidgetFile = CustomWidgetsFile.getInstance(configDir.toFile());

        assertThat(customWidgetFile.checkForModelUpdate(customWidgetFile.getDocument()), is(false));
    }

    @Test
    void isNameTaken_should_always_return_false_on_new_created_properties_file(@TempDir final Path configDir) {
        final CustomWidgetsFile customWidgetFile = CustomWidgetsFile.getInstance(configDir.toFile());

        assertThat(customWidgetFile.isNameTaken("foo"), is(false));
    }

    @Test
    void addCustomWidget_should_add_widget_component(@TempDir final Path configDir) {
        final TextWidget textWidget = createCustomWidget(configDir);

        final CustomWidgetsFile customWidgetFile = CustomWidgetsFile.getInstance(configDir.toFile());
        customWidgetFile.addCustomWidget("foo", Set.of(textWidget));

        assertThat(customWidgetFile.isNameTaken("foo"), is(true));
    }

    private TextWidget createCustomWidget(final Path configDir) {
        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(configDir.toFile());
        final FontHandler fontHandler = new FontHandler(designerPropertiesFile);
        final PdfCaption pdfCaption = new PdfCaption("Text", fontHandler);

        final JLabel label = new JLabel("Text");
        return new TextWidget(IWidget.TEXT, pdfCaption, label, fontHandler);
    }

}
