package org.pdf.forms.gui.commands;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class FileUtilTest {

    @Test
    void getShortenedFileName_with_empty_path_should_return_empty_path() {
        final String path = "";
        final String shortenPath = FileUtil.getShortenedFileName(path, "/");

        assertThat(shortenPath, is(""));
    }

    @Test
    void getShortenedFileName_with_unix_path_should_return_shorten_path() {
        final String path = "/Users/macOS/Documents/myTestDocument.des";
        final String shortenPath = FileUtil.getShortenedFileName(path, "/");

        assertThat(shortenPath, is("/Users/macOS/.../myTestDocument.des"));
    }

    @Test
    void getShortenedFileName_with_windows_path_should_return_shorten_path() {
        final String path = "C:\\User\\windows\\Documents\\myTestDocument.des";
        final String shortenPath = FileUtil.getShortenedFileName(path, "\\\\");

        assertThat(shortenPath, is("C:\\User\\windows\\...\\myTestDocument.des"));
    }
}
