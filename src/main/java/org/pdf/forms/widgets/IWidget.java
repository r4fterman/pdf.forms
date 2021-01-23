package org.pdf.forms.widgets;

import java.awt.*;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import org.pdf.forms.model.des.JavaScriptContent;
import org.pdf.forms.widgets.components.PdfCaption;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface IWidget {

    int NONE = -1;
    int TEXT_FIELD = 0;
    int TEXT = 1;
    int BUTTON = 2;
    int RADIO_BUTTON = 3;
    int CHECK_BOX = 4;
    int COMBO_BOX = 5;
    int LIST_BOX = 6;
    int IMAGE = 7;
    int GROUP = 8;

    Map<String, Integer> WIDGET_TYPES = Map.of(
            "none", IWidget.NONE,
            "text_field", IWidget.TEXT_FIELD,
            "text", IWidget.TEXT,
            "button", IWidget.BUTTON,
            "radio_button", IWidget.RADIO_BUTTON,
            "check_box", IWidget.CHECK_BOX,
            "combo_box", IWidget.COMBO_BOX,
            "list_box", IWidget.LIST_BOX,
            "image", IWidget.IMAGE,
            "group", IWidget.GROUP
    );

    int COMPONENT_BOTH = 0;
    int COMPONENT_CAPTION = 1;
    int COMPONENT_VALUE = 2;

    int UNDERLINE_NONE = 0;
    int UNDERLINE_SINGLE = 1;
    int UNDERLINE_DOUBLE = 2;
    int UNDERLINE_WORD_SINGLE = 3;
    int UNDERLINE_WORD_DOUBLE = 4;

    int STYLE_PLAIN = 0;
    int STYLE_BOLD = 1;
    int STYLE_ITALIC = 2;
    int STYLE_BOLDITALIC = 3;

    int STRIKETHROUGH_OFF = 0;
    int STRIKETHROUGH_ON = 1;

    JComponent getWidget();

    void setPosition(
            int x,
            int y);

    void setX(int x);

    void setY(int y);

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    void setSize(
            int width,
            int height);

    @Override
    String toString();

    int getResizeTypeForSplitComponent(
            int mouseX,
            int mouseY);

    boolean allowEditCaptionAndValue();

    boolean allowEditOfCaptionOnClick();

    Dimension getBoxSize();

    Rectangle getBounds();

    JComponent getValueComponent();

    PdfCaption getCaptionComponent();

    void setLastX(int lastX);

    void setLastY(int lastY);

    int getLastX();

    int getLastY();

    Point getAbsoluteLocationsOfCaption();

    Point getAbsoluteLocationsOfValue();

    boolean isComponentSplit();

    double getResizeHeightRatio();

    double getResizeWidthRatio();

    void setResizeHeightRatio(double resizeHeightRatio);

    void setResizeWidthRatio(double resizeWidthRation);

    double getResizeFromTopRatio();

    double getResizeFromLeftRatio();

    void setResizeFromTopRatio(double resizeHeightRatio);

    void setResizeFromLeftRatio(double resizeWidthRation);

    List<IWidget> getWidgetsInGroup();

    void setWidgetsInGroup(List<IWidget> widgetsInGroup);

    int getType();

    String getWidgetName();

    Icon getIcon();

    Document getProperties();

    void setObjectProperties(Element parentElement);

    void setBorderAndBackgroundProperties(Element borderProperties);

    void setParagraphProperties(
            Element parentElement,
            int currentlyEditing);

    void setLayoutProperties(Element parentElement);

    void setFontProperties(
            Element parentElement,
            int currentlyEditing);

    void setCaptionProperties(Element captionProperties);

    void setAllProperties();

    int getArrayNumber();

    JavaScriptContent getJavaScript();

    org.pdf.forms.model.des.Widget getWidgetModel();
}
