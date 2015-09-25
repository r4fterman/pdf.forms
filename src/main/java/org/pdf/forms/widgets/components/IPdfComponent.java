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
* IPdfComponent.java
* ---------------
*/
package org.pdf.forms.widgets.components;

import java.awt.Color;
import java.awt.Font;

public interface IPdfComponent {
    public void setUnderlineType(int type);

    public void setStikethrough(boolean isStrikethrough);

    public void setHorizontalAlignment(int alignment);

    public void setVerticalAlignment(int alignment);

    public void setFont(Font font);

    public void setForeground(Color color);

    public String getText();

    public void setText(String text);
}
