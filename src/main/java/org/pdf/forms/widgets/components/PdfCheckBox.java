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
* PdfCheckBox.java
* ---------------
*/
package org.pdf.forms.widgets.components;

import javax.swing.JCheckBox;


public class PdfCheckBox extends JCheckBox implements IPdfComponent {
//    public PdfCheckBox(String captionText) {
//        super(captionText);
//        setFont(IWidget.FONT);
//    }

    public PdfCheckBox() {
//    	super(new CheckBoxIcon());
//    	setContentAreaFilled(false);
//    	
    	setText(null);
//        Image oldImage = new ImageIcon(getClass().getResource("/org/pdf/forms/res/check_cross.gif")).getImage();
//
//        int newWidth = getPreferredSize().width;
//        int newHeight = getPreferredSize().height;
//
//        Image newImage= oldImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
//        setIcon(new FixImageIcon(newImage));
	}

    
	public void setUnderlineType(int type) {
    }

    public void setStikethrough(boolean isStrikethrough) {
    }
}