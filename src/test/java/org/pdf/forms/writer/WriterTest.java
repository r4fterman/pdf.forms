/*
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 * <p>
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 * <p>
 * This file is part of the PDF Forms Designer
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * <p>
 * <p>
 * <p>
 * ---------------
 * WriterTest.java
 * ---------------
 */
package org.pdf.forms.writer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.awt.Color;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.support.io.TempDirectory;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;
import org.xml.sax.SAXException;

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

@ExtendWith(TempDirectory.class)
class WriterTest extends EasyMockSupport {

    private static final String DESIGNER_FILE = "/example.des";
    private Writer writer;

    private MockMainFrame mainFrame;

    @BeforeEach
    void setUp() {
        mainFrame = new MockMainFrame();

        writer = new Writer(mainFrame);
    }

    @Disabled
    void write_should_persist_ui_document(@TempDirectory.TempDir final Path path) throws Exception {
        final IDesigner designer = createMock(IDesigner.class);
        mainFrame.setDesigner(designer);
        designer.close();

        replayAll();
        final File source = getFile();
        final File target = createTargetFile(path, source);

        //final Commands commands = new Commands(mainFrame, Version.DEV);
        //commands.openDesignerFile(target.getAbsolutePath());

        final File outputFile = new File(path.toFile(), "output.des");
        final org.w3c.dom.Document properties = readDocument(source);

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
    void testWriter(@TempDirectory.TempDir final Path path) throws Exception {
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
    }

    private BaseColor getBaseColor(final Color color) {
        return new GrayColor(color.getRGB());
    }

    private org.w3c.dom.Document readDocument(final File file) throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        return documentBuilder.parse(file);
    }

    private File getFile() throws URISyntaxException {
        final URL url = WriterTest.class.getResource(DESIGNER_FILE);
        assertThat("File not found: " + DESIGNER_FILE, DESIGNER_FILE, not(nullValue()));

        return new File(url.toURI());
    }

    private File createTargetFile(
            @TempDirectory.TempDir final Path path,
            final File source) throws IOException {
        final File target = new File(path.toFile(), "example.des");
        Files.copy(new FileInputStream(source), target.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return target;
    }
}
