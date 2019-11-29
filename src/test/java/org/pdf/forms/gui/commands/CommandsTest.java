package org.pdf.forms.gui.commands;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandsTest {

    private Commands command;

    @BeforeEach
    void setUp() {
        this.command = new Commands(null, "DEV-TEST");
    }

    @Test
    void getShortenedFileName_with_empty_path_should_return_empty_path() {
        final String path = "";
        final String shortenPath = command.getShortenedFileName(path, "/");

        assertThat(shortenPath, is(""));
    }

    @Test
    void getShortenedFileName_with_unix_path_should_return_shorten_path() {
        final String path = "/Users/macOS/Documents/myTestDocument.des";
        final String shortenPath = command.getShortenedFileName(path, "/");

        assertThat(shortenPath, is("/Users/macOS/.../myTestDocument.des"));
    }

    @Test
    void getShortenedFileName_with_windows_path_should_return_shorten_path() {
        final String path = "C:\\User\\windows\\Documents\\myTestDocument.des";
        final String shortenPath = command.getShortenedFileName(path, "\\\\");

        assertThat(shortenPath, is("C:\\User\\windows\\...\\myTestDocument.des"));
    }
}
