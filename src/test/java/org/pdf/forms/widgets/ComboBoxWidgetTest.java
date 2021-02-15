package org.pdf.forms.widgets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

import javax.swing.*;
import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pdf.forms.Configuration;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.readers.properties.DesignerPropertiesFile;
import org.pdf.forms.widgets.components.SplitComponent;
import org.pdf.forms.writer.MockFontDirectories;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.DifferenceEvaluators;

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
        this.fontHandler = new FontHandler(designerPropertiesFile, new MockFontDirectories());

        this.baseComponent = new SplitComponent("captionText", new JComboBox<>(), 1, fontHandler);
        this.component = new JComboBox<>();
        component.setSize(1, 1);
    }

    @Test
    void persist_widget_into_xml() throws Exception {
        final ComboBoxWidget comboBoxWidget = new ComboBoxWidget(IWidget.COMBO_BOX,
                baseComponent,
                component,
                fontHandler);

        final String serialize = new WidgetFileWriter(comboBoxWidget.getWidgetModel()).serialize();
        final String expected = Files.readString(getFile().toPath());

        assertThat(serialize, isSimilarTo(expected)
                .withNodeMatcher(new DefaultNodeMatcher(new PropertyNameSelector()))
                .withDifferenceEvaluator(DifferenceEvaluators.chain(
                        DifferenceEvaluators.Default,
                        new IgnoreFontNameDifferenceEvaluator(),
                        new PropertyWithIteratorNumberDifferenceEvaluator("Drop-down List")
                        ))
                .ignoreWhitespace()
        );
    }

    @Test
    void read_persisted_xml_into_widget() throws Exception {
        final ComboBoxWidget widget = new ComboBoxWidget(IWidget.COMBO_BOX,
                baseComponent,
                component,
                getWidgetFromFile(),
                new FontHandler(designerPropertiesFile, new MockFontDirectories()));

        final String serialize = new WidgetFileWriter(widget.getWidgetModel()).serialize();
        final String expected = Files.readString(getFile().toPath());

        assertThat(serialize, isSimilarTo(expected)
                .withNodeMatcher(new DefaultNodeMatcher(new PropertyNameSelector()))
                .ignoreWhitespace()
        );
    }

    private org.pdf.forms.model.des.Widget getWidgetFromFile() throws IOException, JAXBException, URISyntaxException {
        return new WidgetFileReader(getFile()).getWidget();
    }

    private File getFile() throws URISyntaxException {
        final URL url = ComboBoxWidgetTest.class.getResource("comboBoxWidget_persisted.xml");
        return new File(url.toURI());
    }
}
