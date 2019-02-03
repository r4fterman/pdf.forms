package org.pdf.forms.utils.configuration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.nio.file.Path;

import javax.swing.JMenu;

import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.support.io.TempDirectory;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.commands.CommandListener;
import org.pdf.forms.gui.designer.IDesigner;

@ExtendWith(TempDirectory.class)
class MenuConfigurationTest extends EasyMockSupport {

    @Test
    void getMenus(@TempDirectory.TempDir final Path configDir) {
        final CommandListener cmdListener = createMock(CommandListener.class);
        final IDesigner designer = createMock(IDesigner.class);
        final IMainFrame mainFrame = createMock(IMainFrame.class);

        final MenuConfiguration menuConfiguration = new MenuConfiguration(cmdListener, designer, mainFrame, configDir.toFile());

        final JMenu[] menus = menuConfiguration.getMenus();

        assertThat(menus.length, is(5));

        assertFileMenu(menus[0]);
        assertInsertMenu(menus[1]);
        assertLayoutMenu(menus[2]);
        assertWindowMenu(menus[3]);
        assertHelpMenu(menus[4]);
    }

    @Test
    void writeDefaultConfiguration() {
    }

    @Test
    void setState() {
    }

    @Test
    void getRecentDesignerFilesMenu() {
    }

    @Test
    void getRecentImportedFilesMenu() {
    }

    @Test
    void setProperties() {
    }

    @Test
    void setDockableVisible() {
    }

    private void assertFileMenu(final JMenu menu) {
        assertThat(menu.getText(), is("File"));

        assertThat(menu.getItemCount(), is(16));

        assertThat(menu.getItem(0).getText(), is("New"));
        assertThat(menu.getItem(1).getText(), is("Open Designer File"));
        assertThat(menu.getItem(2).getText(), is("Recently Designer Files"));
        assertThat(menu.getItem(3).getText(), is("Close"));
        assertThat(menu.getItem(4), is(nullValue()));
        assertThat(menu.getItem(5).getText(), is("Import PDF Document"));
        assertThat(menu.getItem(6).getText(), is("Recently Imported PDF Documents"));
        assertThat(menu.getItem(7), is(nullValue()));
        assertThat(menu.getItem(8).getText(), is("Save Designer File"));
        assertThat(menu.getItem(9).getText(), is("Save Designer File As"));
        assertThat(menu.getItem(10), is(nullValue()));
        assertThat(menu.getItem(11).getText(), is("Publish PDF Document"));
        assertThat(menu.getItem(12), is(nullValue()));
        assertThat(menu.getItem(13).getText(), is("Font Management"));
        assertThat(menu.getItem(14), is(nullValue()));
        assertThat(menu.getItem(15).getText(), is("Exit"));
    }

    private void assertInsertMenu(final JMenu menu) {
        assertThat(menu.getText(), is("Insert"));

        assertThat(menu.getItemCount(), is(2));
        assertThat(menu.getItem(0).getText(), is("Insert Page"));
        assertThat(menu.getItem(1).getText(), is("Remove Page"));
    }

    private void assertLayoutMenu(final JMenu menu) {
        assertThat(menu.getText(), is("Layout"));

        assertThat(menu.getItemCount(), is(9));
        assertThat(menu.getItem(0).getText(), is("Align"));
        assertThat(menu.getItem(1), is(nullValue()));
        assertThat(menu.getItem(2).getText(), is("Group"));
        assertThat(menu.getItem(3).getText(), is("Ungroup"));
        assertThat(menu.getItem(4), is(nullValue()));
        assertThat(menu.getItem(5).getText(), is("Bring to Front"));
        assertThat(menu.getItem(6).getText(), is("Send to Back"));
        assertThat(menu.getItem(7).getText(), is("Bring Forwards"));
        assertThat(menu.getItem(8).getText(), is("Send Backwards"));
    }

    private void assertWindowMenu(final JMenu menu) {
        assertThat(menu.getText(), is("Window"));

        assertThat(menu.getItemCount(), is(14));
        assertThat(menu.getItem(0).getText(), is("Toolbars"));
        assertThat(menu.getItem(1).getText(), is("Script Editor"));
        assertThat(menu.getItem(2), is(nullValue()));
        assertThat(menu.getItem(3).getText(), is("Hierarchy"));
        assertThat(menu.getItem(4), is(nullValue()));
        assertThat(menu.getItem(5).getText(), is("Library"));
        assertThat(menu.getItem(6), is(nullValue()));
        assertThat(menu.getItem(7).getText(), is("Properties"));
        assertThat(menu.getItem(8).getText(), is("Layout"));
        assertThat(menu.getItem(9).getText(), is("Border"));
        assertThat(menu.getItem(10).getText(), is("Object"));
        assertThat(menu.getItem(11), is(nullValue()));
        assertThat(menu.getItem(12).getText(), is("Font"));
        assertThat(menu.getItem(13).getText(), is("Paragraph"));
    }

    private void assertHelpMenu(final JMenu menu) {
        assertThat(menu.getText(), is("Help"));

        assertThat(menu.getItemCount(), is(2));
        assertThat(menu.getItem(0).getText(), is("Visit Website"));
        assertThat(menu.getItem(1).getText(), is("About"));
    }
}
