package org.pdf.forms.fonts;

import java.util.List;

import javax.swing.*;

import org.pdf.forms.gui.UIDialogTest;
import org.pdf.forms.model.properties.Font;

class FontManagementDialogTest extends UIDialogTest {

    @Override
    protected JDialog createDialog(final JFrame frame) {
        final List<Font> systemFonts = List.of(
                new Font("Font1", "/path/to/font"),
                new Font("Font2", "/path/to/font"),
                new Font("Font3", "/path/to/font")
        );
        return new FontManagementDialog(frame, systemFonts, List.of());
    }
}
