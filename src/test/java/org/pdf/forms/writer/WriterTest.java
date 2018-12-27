/**
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

import java.awt.Color;
import java.io.FileOutputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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

public class WriterTest {

    @Rule
    public final TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void testWriter() throws Exception {
        final Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        final PdfWriter writer = PdfWriter
                .getInstance(document, new FileOutputStream(testFolder.newFile()));
        document.open();

        RadioCheckField bt = new RadioCheckField(writer, new Rectangle(100, 100, 200, 200), "radio", "v1");
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
        bt = new RadioCheckField(writer, new Rectangle(300, 300, 400, 400), "check1", "Yes");
        bt.setCheckType(RadioCheckField.TYPE_CHECK);
        bt.setBorderWidth(BaseField.BORDER_WIDTH_THIN);
        bt.setBorderColor(getBaseColor(Color.black));
        bt.setBackgroundColor(getBaseColor(Color.white));
        final PdfFormField ck = bt.getCheckField();
        writer.addAnnotation(ck);
        document.close();
    }

    private BaseColor getBaseColor(final Color color) {
        return new GrayColor(color.getRGB());
    }
}
