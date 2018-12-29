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
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.support.io.TempDirectory;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.w3c.dom.Element;
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

    private Writer writer;

    private IMainFrame mainFrame;

    @BeforeEach
    void setUp() {
        this.mainFrame = createMock(IMainFrame.class);

        this.writer = new Writer(mainFrame);
    }

    @Test
    void write_should_persist_ui_document(@TempDirectory.TempDir final Path path) throws Exception {
        final String fileName = "/example.des";
        final File file = getFile(fileName);
        final org.w3c.dom.Document properties = readDocument(file);

        final List<IWidget>[] widgets = buildWidgets();

        final File outputFile = new File(path.toFile(), "output.des");
        writer.write(outputFile, widgets, properties);

        assertThat(outputFile.length(), is(41062L));
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

    private List<IWidget>[] buildWidgets() {
        final List<IWidget>[] widgets = new ArrayList[2];
        widgets[0] = widgetList();
        widgets[1] = widgetList();

        return widgets;
    }

    private List<IWidget> widgetList() {
        final List<IWidget> widgetList = new ArrayList<>();
        widgetList.add(WidgetFactory.createWidget(IWidget.TEXT_FIELD, (Element) null));
        return widgetList;
    }

    private org.w3c.dom.Document readDocument(final File file) throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        return documentBuilder.parse(file);
    }

    private File getFile(final String fileName) throws URISyntaxException {
        final URL url = WriterTest.class.getResource(fileName);
        assertThat("File not found: " + fileName, fileName, not(nullValue()));

        return new File(url.toURI());
    }
}
