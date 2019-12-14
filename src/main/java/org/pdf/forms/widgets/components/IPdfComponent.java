package org.pdf.forms.widgets.components;

import java.awt.Color;
import java.awt.Font;

public interface IPdfComponent {

    void setUnderlineType(int type);

    void setStrikethrough(boolean isStrikethrough);

    void setHorizontalAlignment(int alignment);

    void setVerticalAlignment(int alignment);

    void setFont(Font font);

    void setForeground(Color color);

    String getText();

    void setText(String text);
}
