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
 * PropertiesCompound.java
 * ---------------
 */
package org.pdf.forms.gui.properties;

import java.util.Set;

import com.vlsolutions.swing.docking.CompoundDockable;
import com.vlsolutions.swing.docking.DockKey;
import org.pdf.forms.gui.properties.border.BorderPropertiesTab;
import org.pdf.forms.gui.properties.font.FontPropertiesTab;
import org.pdf.forms.gui.properties.layout.LayoutPropertiesTab;
import org.pdf.forms.gui.properties.object.ObjectPropertiesTab;
import org.pdf.forms.gui.properties.paragraph.ParagraphPropertiesTab;

public class PropertiesCompound extends CompoundDockable {

    private DockKey key = new DockKey("Properties");

    private FontPropertiesTab fontPropertiesTab;
    private ObjectPropertiesTab objectPropertiesTab;
    private LayoutPropertiesTab layoutPropertiesTab;
    private BorderPropertiesTab borderPropertiesTab;
    private ParagraphPropertiesTab paragraphPropertiesTab;

    public PropertiesCompound(ObjectPropertiesTab objectPropertiesTab, FontPropertiesTab fontPropertiesTab,
                              LayoutPropertiesTab layoutPropertiesTab, BorderPropertiesTab borderPropertiesTab,
                              ParagraphPropertiesTab paragraphPropertiesTab) {

        super(new DockKey("Properties"));

        this.objectPropertiesTab = objectPropertiesTab;
        this.fontPropertiesTab = fontPropertiesTab;
        this.layoutPropertiesTab = layoutPropertiesTab;
        this.borderPropertiesTab = borderPropertiesTab;
        this.paragraphPropertiesTab = paragraphPropertiesTab;
        //key.setName("name");
    }

    public DockKey getDockKey() {
        return key;
    }

    public void setProperties(Set widget) {
        fontPropertiesTab.setProperties(widget);
        objectPropertiesTab.setProperties(widget);
        layoutPropertiesTab.setProperties(widget);
        borderPropertiesTab.setProperties(widget);
        paragraphPropertiesTab.setProperties(widget);
    }

    public void updateAvailiableFonts() {
        fontPropertiesTab.updateAvailiableFonts();

    }
}
