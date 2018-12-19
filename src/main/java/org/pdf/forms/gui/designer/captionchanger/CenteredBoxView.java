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
 * CenteredBoxView.java
 * ---------------
 */
package org.pdf.forms.gui.designer.captionchanger;

import javax.swing.text.BoxView;
import javax.swing.text.Element;

class CenteredBoxView extends BoxView {
    public CenteredBoxView(
            final Element elem,
            final int axis) {
        super(elem, axis);
    }

    @Override
    protected void layoutMajorAxis(
            final int targetSpan,
            final int axis,
            final int[] offsets,
            final int[] spans) {
        super.layoutMajorAxis(targetSpan, axis, offsets, spans);
        int textBlockHeight = 0;
        final int offset;

        for (final int span : spans) {
            textBlockHeight += span;
        }
        offset = (targetSpan - textBlockHeight) / 2;
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] += offset;
        }
    }
}
