/**
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 *
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 *
 * 	This file is part of the PDF Forms Designer
 *
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


 *
 * ---------------
 * Tester.java
 * ---------------
 */
package org.pdf.forms.writer;

import java.awt.*;
import java.io.FileOutputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseField;
import com.lowagie.text.pdf.PdfBorderDictionary;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RadioCheckField;

public class Tester {

    @Rule
    public final TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void testWriter() throws Exception {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter
                .getInstance(document, new FileOutputStream(testFolder.newFile()));
        document.open();

        RadioCheckField bt = new RadioCheckField(writer, new Rectangle(100, 100, 200, 200), "radio", "v1");
        bt.setCheckType(RadioCheckField.TYPE_CIRCLE);
        bt.setBackgroundColor(Color.cyan);
        bt.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
        bt.setBorderColor(Color.red);
        bt.setTextColor(Color.yellow);
        bt.setBorderWidth(BaseField.BORDER_WIDTH_THICK);
        bt.setChecked(false);
        PdfFormField f1 = bt.getRadioField();
        bt.setOnValue("v2");
        bt.setChecked(true);
        bt.setBox(new Rectangle(100, 300, 200, 400));
        PdfFormField f2 = bt.getRadioField();
        bt.setChecked(false);
        PdfFormField top = bt.getRadioGroup(true, false);
        bt.setOnValue("v3");
        bt.setBox(new Rectangle(100, 500, 200, 600));
        PdfFormField f3 = bt.getRadioField();
        top.addKid(f1);
        top.addKid(f2);
        top.addKid(f3);
        writer.addAnnotation(top);
        bt = new RadioCheckField(writer, new Rectangle(300, 300, 400, 400), "check1", "Yes");
        bt.setCheckType(RadioCheckField.TYPE_CHECK);
        bt.setBorderWidth(BaseField.BORDER_WIDTH_THIN);
        bt.setBorderColor(Color.black);
        bt.setBackgroundColor(Color.white);
        PdfFormField ck = bt.getCheckField();
        writer.addAnnotation(ck);
        document.close();
    }
}
