package org.pdf.forms.gui.properties;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.vlsolutions.swing.docking.CompoundDockable;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.docking.DockingUtilities;
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

    private final Map<String, Dockable> dockedTabbes;

    public PropertiesPanel(
            final IDesigner designer,
            final FontHandler fontHandler) {
        super(new DockKey("properties", "Properties", "Element properties"));

        this.paragraphPropertiesTab = new ParagraphPropertiesTab(designer);
        this.borderPropertiesTab = new BorderPropertiesTab(designer);
        this.layoutPropertiesTab = new LayoutPropertiesTab(designer);
        this.objectPropertiesTab = new ObjectPropertiesTab(designer);
        this.fontPropertiesTab = new FontPropertiesTab(designer, fontHandler);

        this.dockedTabbes = Map.of(
                fontPropertiesTab.getDockKey().getKey(), fontPropertiesTab,
                objectPropertiesTab.getDockKey().getKey(), objectPropertiesTab,
                paragraphPropertiesTab.getDockKey().getKey(), paragraphPropertiesTab,
                layoutPropertiesTab.getDockKey().getKey(), layoutPropertiesTab,
                borderPropertiesTab.getDockKey().getKey(), borderPropertiesTab
        );
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

    public void addDockables(final DockingDesktop dockingDesktop) {
        dockingDesktop.addDockable(this, fontPropertiesTab);
        dockingDesktop.createTab(fontPropertiesTab, objectPropertiesTab, 1);
        dockingDesktop.createTab(fontPropertiesTab, layoutPropertiesTab, 2);
        dockingDesktop.createTab(fontPropertiesTab, borderPropertiesTab, 3);
        dockingDesktop.createTab(fontPropertiesTab, paragraphPropertiesTab, 4);
    }

    public Optional<String> getNameFromDockable(final Dockable dockableName) {
        return dockedTabbes.entrySet().stream()
                .filter(entry -> dockableName == entry.getValue())
                .findFirst()
                .map(Map.Entry::getKey);
    }

    public void setDockableVisible(
            final DockingDesktop dockingDesktop,
            final String dockableName,
            final boolean visible) {
        Optional.ofNullable(dockedTabbes.get(dockableName))
                .ifPresent(dockable -> {
                    if (visible) {
                        addDockable(dockingDesktop, dockable);
                    } else {
                        removeClosable(dockingDesktop, dockable);
                    }
                });
    }

    private void addDockable(
            final DockingDesktop dockingDesktop,
            final Dockable dockable) {
        dockedTabbes.values().stream()
                .map(DockingUtilities::findTabbedDockableContainer)
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresentOrElse(tabbedDockableContainer ->
                                dockingDesktop.createTab(tabbedDockableContainer.getSelectedDockable(), dockable, tabbedDockableContainer.getTabCount(), true),
                        () -> dockingDesktop.addDockable(this, dockable));
    }

    private void removeClosable(
            final DockingDesktop dockingDesktop,
            final Dockable dockable) {
        dockingDesktop.close(dockable);
    }
}
