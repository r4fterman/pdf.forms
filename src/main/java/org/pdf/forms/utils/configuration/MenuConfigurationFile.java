package org.pdf.forms.utils.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.pdf.forms.gui.configuration.MenuConfiguration;
import org.pdf.forms.widgets.IWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuConfigurationFile {

//    private final List<JMenuItem> closeItems = new ArrayList<>();
//    private final List<JMenuItem> previewItems = new ArrayList<>();
//    private final List<JMenuItem> alignAndOrderMenuItems = new ArrayList<>();
//    private final List<JMenuItem> groupMenuItems = new ArrayList<>();
//    private final Map<String, JMenuItem> windowNames = new HashMap<>();

    private final Logger logger = LoggerFactory.getLogger(MenuConfigurationFile.class);
    private final File menuConfigurationFile;

    public MenuConfigurationFile(final File directory) {
        this.menuConfigurationFile = getFile(directory);
    }

    public MenuConfiguration getMenuConfiguration() {
        return new MenuConfigurationFileReader(menuConfigurationFile)
                .getMenuConfiguration();
    }

    private void setCloseState(final boolean state) {
//        for (final JMenuItem menuItem: closeItems) {
//            menuItem.setEnabled(state);
//        }
    }

    public void setState(final boolean state) {
//        setCloseState(state);
//
//        for (final JMenuItem menuItem: previewItems) {
//            menuItem.setEnabled(state);
//        }
    }

    public void setProperties(final Set<IWidget> widgets) {
//        if (widgets.isEmpty()) {
//            setAlignAndOrderMenuItemsEnabled(false);
//            groupMenuItems.get(0).setEnabled(false);
//            groupMenuItems.get(1).setEnabled(false);
//            return;
//        }
//
//        setAlignAndOrderMenuItemsEnabled(true);
//        if (widgets.size() == 1 && widgets.iterator().next().getType() == IWidget.GROUP) {
//            groupMenuItems.get(0).setEnabled(false);
//            groupMenuItems.get(1).setEnabled(true);
//        } else if (widgets.size() > 1) {
//            groupMenuItems.get(0).setEnabled(true);
//            groupMenuItems.get(1).setEnabled(false);
//        }
    }

    private void setAlignAndOrderMenuItemsEnabled(final boolean enabled) {
//        for (final JMenuItem menu: alignAndOrderMenuItems) {
//            menu.setEnabled(enabled);
//        }
    }

    public void setDockableVisible(
            final String dockableName,
            final boolean visible) {
//        final JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) windowNames.get(dockableName);
//        menuItem.setSelected(visible);
    }

    public void writeMenuConfiguration(
            final MenuConfiguration menuConfiguration,
            final File directory) {
        try {
            final String xmlContent = convertMenuConfiguration(menuConfiguration);
            Files.writeString(getFile(directory).toPath(),
                    xmlContent,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE_NEW);
        } catch (final IOException | JAXBException e) {
            logger.error("Unable to write configuration to file: {}", menuConfigurationFile);
        }
    }

    private String convertMenuConfiguration(final MenuConfiguration menuConfiguration) throws JAXBException {
        final XmlJavaObjectMapper<MenuConfiguration> mapper = new XmlJavaObjectMapper<>(MenuConfiguration.class);
        return mapper.convertObjectIntoXml(menuConfiguration);
    }

    private File getFile(final File directory) {
        return new File(directory, "menus.xml");
    }
}
