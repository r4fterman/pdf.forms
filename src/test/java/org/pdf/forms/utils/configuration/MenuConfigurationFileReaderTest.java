package org.pdf.forms.utils.configuration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.pdf.forms.gui.configuration.Item;
import org.pdf.forms.gui.configuration.Menu;
import org.pdf.forms.gui.configuration.MenuConfiguration;

class MenuConfigurationFileReaderTest {

    @Test
    void read_menu_configuration_from_file() throws Exception {
        final MenuConfigurationFileReader reader = new MenuConfigurationFileReader(getFile("/default_menus.xml"));

        final MenuConfiguration menuConfiguration = reader.getMenuConfiguration();

        assertThat(menuConfiguration, is(notNullValue()));

        final List<Menu> menu = menuConfiguration.getMenu();
        assertThat(menu, hasSize(5));

        final Menu menu0 = menu.get(0);
        assertThat(menu0.isVisible(), is(true));

        final List<Item> items0 = menu0.getItem();
        assertThat(items0, hasSize(16));

        final Menu menu4 = menu.get(4);
        assertThat(menu4.isVisible(), is(true));

        final List<Item> items4 = menu4.getItem();
        assertThat(items4, hasSize(2));
        assertWebsite(items4.get(0));
        assertAbout(items4.get(1));
    }

    private void assertWebsite(final Item item) {
        assertThat(item.getCommand(), is("WEBSITE"));
        assertThat(item.getName(), is("Visit Website"));
        assertThat(item.isVisible(), is(true));
    }

    private void assertAbout(final Item item) {
        assertThat(item.getCommand(), is("ABOUT"));
        assertThat(item.getName(), is("About"));
        assertThat(item.isVisible(), is(true));
    }

    private File getFile(final String fileName) throws URISyntaxException {
        final URL url = MenuConfigurationFileReaderTest.class.getResource(fileName);
        assertThat("File not found: " + fileName, url, is(notNullValue()));

        return new File(url.toURI());
    }
}
