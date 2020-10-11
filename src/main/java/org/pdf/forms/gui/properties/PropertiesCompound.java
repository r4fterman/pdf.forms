package org.pdf.forms.gui.properties;

import java.util.Set;

import org.pdf.forms.gui.properties.border.BorderPropertiesTab;
import org.pdf.forms.gui.properties.font.FontPropertiesTab;
import org.pdf.forms.gui.properties.layout.LayoutPropertiesTab;
import org.pdf.forms.gui.properties.object.ObjectPropertiesTab;
import org.pdf.forms.gui.properties.paragraph.ParagraphPropertiesTab;
import org.pdf.forms.widgets.IWidget;

import com.vlsolutions.swing.docking.CompoundDockable;
import com.vlsolutions.swing.docking.DockKey;

public class PropertiesCompound extends CompoundDockable {

    private final DockKey key = new DockKey("Properties");

    private final FontPropertiesTab fontPropertiesTab;
    private final ObjectPropertiesTab objectPropertiesTab;
    private final LayoutPropertiesTab layoutPropertiesTab;
    private final BorderPropertiesTab borderPropertiesTab;
    private final ParagraphPropertiesTab paragraphPropertiesTab;

    public PropertiesCompound(
            final ObjectPropertiesTab objectPropertiesTab,
            final FontPropertiesTab fontPropertiesTab,
            final LayoutPropertiesTab layoutPropertiesTab,
            final BorderPropertiesTab borderPropertiesTab,
            final ParagraphPropertiesTab paragraphPropertiesTab) {

        super(new DockKey("Properties"));

        this.objectPropertiesTab = objectPropertiesTab;
        this.fontPropertiesTab = fontPropertiesTab;
        this.layoutPropertiesTab = layoutPropertiesTab;
        this.borderPropertiesTab = borderPropertiesTab;
        this.paragraphPropertiesTab = paragraphPropertiesTab;
        //key.setName("name");
    }

    @Override
    public DockKey getDockKey() {
        return key;
    }

    public void setProperties(final Set<IWidget> widget) {
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
