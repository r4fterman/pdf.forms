/*
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
* PdfList.java
* ---------------
*/
package org.pdf.forms.widgets.components;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.pdf.forms.fonts.FontHandler;

public class PdfList extends JScrollPane implements IPdfComponent {

    private final Lst list;

    public JList getList() {
        return list;
    }

    public PdfList() {

        list = new Lst(new DefaultListModel());

        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        setViewportView(list);
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void setText(final String text) {
    }

    @Override
    public void setUnderlineType(final int type) {
    }

    @Override
    public void setStikethrough(final boolean isStrikethrough) {
    }

    @Override
    public void setHorizontalAlignment(final int alignment) {

    }

    @Override
    public void setVerticalAlignment(final int alignment) {
    }
}

class Lst extends JList {

    public Lst(final DefaultListModel defaultListModel) {
        super(defaultListModel);
        setFont(FontHandler.getInstance().getDefaultFont().deriveFont(11f));
    }

}

