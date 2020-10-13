package org.pdf.forms.gui.properties;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.vlsolutions.swing.docking.CompoundDockable;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockingDesktop;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.border.BorderPropertiesTab;
import org.pdf.forms.gui.properties.font.FontPropertiesTab;
import org.pdf.forms.gui.properties.layout.LayoutPropertiesTab;
import org.pdf.forms.gui.properties.object.ObjectPropertiesTab;
import org.pdf.forms.gui.properties.paragraph.ParagraphPropertiesTab;
import org.pdf.forms.widgets.IWidget;

public class PropertiesPanel extends CompoundDockable {

    private final ParagraphPropertiesTab paragraphPropertiesTab;
    private final BorderPropertiesTab borderPropertiesTab;
    private final LayoutPropertiesTab layoutPropertiesTab;
    private final ObjectPropertiesTab objectPropertiesTab;
    private final FontPropertiesTab fontPropertiesTab;

    private final Map<String, Dockable> dockableNames;

    public PropertiesPanel(
            final IDesigner designer,
            final FontHandler fontHandler) {
        super(new DockKey("Properties"));

        this.paragraphPropertiesTab = new ParagraphPropertiesTab(designer);
        this.borderPropertiesTab = new BorderPropertiesTab(designer);
        this.layoutPropertiesTab = new LayoutPropertiesTab(designer);
        this.objectPropertiesTab = new ObjectPropertiesTab(designer);
        this.fontPropertiesTab = new FontPropertiesTab(designer, fontHandler);

        dockableNames = Map.of(
                "Paragraph", paragraphPropertiesTab,
                "Border", borderPropertiesTab,
                "Layout", layoutPropertiesTab,
                "Object", objectPropertiesTab,
                "Font", fontPropertiesTab
        );
    }

    @Override
    public DockKey getDockKey() {
        return new DockKey("Properties");
    }

    public void setProperties(final Set<IWidget> widgets) {
        fontPropertiesTab.setProperties(widgets);
        objectPropertiesTab.setProperties(widgets);
        layoutPropertiesTab.setProperties(widgets);
        borderPropertiesTab.setProperties(widgets);
        paragraphPropertiesTab.setProperties(widgets);
    }

    public void updateAvailableFonts() {
        fontPropertiesTab.updateAvailiableFonts();
    }

    public void addDockables(final DockingDesktop desk) {
        desk.addDockable(this, fontPropertiesTab);
        desk.createTab(fontPropertiesTab, objectPropertiesTab, 1);
        desk.createTab(fontPropertiesTab, layoutPropertiesTab, 2);
        desk.createTab(fontPropertiesTab, borderPropertiesTab, 3);
        desk.createTab(fontPropertiesTab, paragraphPropertiesTab, 4);
    }

    public Optional<String> getNameFromDockable(final Dockable dockableName) {
        return dockableNames.entrySet().stream()
                .filter(entry -> dockableName == entry.getValue())
                .findFirst()
                .map(Map.Entry::getKey);
    }
}
