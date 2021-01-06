package org.pdf.forms.writer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.commands.OpenDesignerFileCommand;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.DesignerPropertiesFile;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RadioCheckField;

class WriterTest extends EasyMockSupport {

    private static final String DESIGNER_FILE = "/example.des";

    @Disabled(value = "Does not work on headless Travis CI")
    void write_should_persist_ui_document(@TempDir final Path path) throws Exception {
        final MockMainFrame mainFrame = new MockMainFrame();

        final IDesigner designer = createMock(IDesigner.class);
        mainFrame.setDesigner(designer);
        designer.close();

        final DesignerPropertiesFile designerPropertiesFile = new DesignerPropertiesFile(path.toFile());
        final FontHandler fontHandler = new FontHandler(designerPropertiesFile);
        final WidgetFactory widgetFactory = new WidgetFactory(fontHandler);
        final Writer writer = new Writer(mainFrame, fontHandler);

        replayAll();
        final File source = getFile();
        final File target = createTargetFile(path, source);


        new OpenDesignerFileCommand(mainFrame, "DEV-TEST", widgetFactory, designerPropertiesFile).openDesignerFile(target.getAbsolutePath());

        final File outputFile = new File(path.toFile(), "output.des");
        final org.w3c.dom.Document properties = XMLUtils.readDocument(source);

        final List<IWidget> page1Widgets = mainFrame.getFormsDocument().getPage(1).getWidgets();
        final List<IWidget> page2Widgets = mainFrame.getFormsDocument().getPage(2).getWidgets();
        final Map<Integer, List<IWidget>> widgetsMap = Map.of(
                0, page1Widgets,
                1, page2Widgets
        );

        writer.write(outputFile, widgetsMap, properties);
        verifyAll();

        assertThat(outputFile.length(), is(15161L));
    }

    @Test
    void testWriter(@TempDir final Path path) throws Exception {
        final File file = new File(path.toFile(), "output.des");

        final Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        final RadioCheckField bt = new RadioCheckField(writer, new Rectangle(100, 100, 200, 200), "radio", "v1");
        bt.setCheckType(RadioCheckField.TYPE_CIRCLE);
        bt.setBackgroundColor(getBaseColor(Color.cyan));
        bt.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
        bt.setBorderColor(getBaseColor(Color.red));
        bt.setTextColor(getBaseColor(Color.yellow));
        bt.setBorderWidth(BaseField.BORDER_WIDTH_THICK);
        bt.setChecked(false);
        final PdfFormField f1 = bt.getRadioField();
        bt.setOnValue("v2");
        bt.setChecked(true);
        bt.setBox(new Rectangle(100, 300, 200, 400));
        final PdfFormField f2 = bt.getRadioField();
        bt.setChecked(false);
        final PdfFormField top = bt.getRadioGroup(true, false);
        bt.setOnValue("v3");
        bt.setBox(new Rectangle(100, 500, 200, 600));
        final PdfFormField f3 = bt.getRadioField();
        top.addKid(f1);
        top.addKid(f2);
        top.addKid(f3);
        writer.addAnnotation(top);

        final RadioCheckField bt2 = new RadioCheckField(writer, new Rectangle(300, 300, 400, 400), "check1", "Yes");
        bt2.setCheckType(RadioCheckField.TYPE_CHECK);
        bt2.setBorderWidth(BaseField.BORDER_WIDTH_THIN);
        bt2.setBorderColor(getBaseColor(Color.black));
        bt2.setBackgroundColor(getBaseColor(Color.white));
        final PdfFormField ck = bt2.getCheckField();
        writer.addAnnotation(ck);
        document.close();

        assertThat("File: " + file.getAbsolutePath(), file.length(), is(greaterThan(4480L)));
    }

    private BaseColor getBaseColor(final Color color) {
        return new GrayColor(color.getRGB());
    }

    private File getFile() throws URISyntaxException {
        final URL url = WriterTest.class.getResource(DESIGNER_FILE);
        assertThat("File not found: " + DESIGNER_FILE, DESIGNER_FILE, not(nullValue()));

        return new File(url.toURI());
    }

    private File createTargetFile(
            @TempDir final Path path,
            final File source) throws IOException {
        final File target = new File(path.toFile(), "example.des");
        Files.copy(new FileInputStream(source), target.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return target;
    }
}
