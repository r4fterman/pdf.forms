package org.pdf.forms.widgets;

import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jpedal.objects.acroforms.creation.JPedalBorderFactory;
import org.jpedal.utils.Strip;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.listeners.DesignerMouseMotionListener;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.components.IPdfComponent;
import org.pdf.forms.widgets.components.PdfCaption;
import org.pdf.forms.widgets.components.SplitComponent;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.pdf.forms.widgets.utils.WidgetSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Widget {

    private final Logger logger = LoggerFactory.getLogger(Widget.class);

    private final JComponent baseComponent;
    private final Point position;
    private final int type;
    private final Icon icon;
    private final FontHandler fontHandler;

    private JComponent component;
    private Document properties;
    private boolean isComponentSplit;
    private boolean allowEditCaptionAndValue;
    private boolean allowEditOfCaptionOnClick;
    private String widgetName;
    private int arrayNumber;
    private int lastX;
    private int lastY;
    private double resizeHeightRatio;
    private double resizeWidthRatio;
    private double resizeFromTopRatio;
    private double resizeWidthFromLeftRatio;

    public Widget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final String iconLocation,
            final FontHandler fontHandler) {
        this.type = type;
        this.component = component;
        this.baseComponent = baseComponent;
        this.fontHandler = fontHandler;

        position = new Point(0, 0);
        icon = new ImageIcon(getClass().getResource(iconLocation));
    }

    public JComponent getWidget() {
        return component;
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public void setPosition(
            final int x,
            final int y) {
        position.x = x;
        position.y = y;
    }

    public void setX(final int x) {
        position.x = x;
    }

    public void setY(final int y) {
        position.y = y;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public int getWidth() {
        return component.getWidth();
    }

    public int getHeight() {
        return component.getHeight();
    }

    public void setSize(
            final int width,
            final int height) {
        component = WidgetFactory.createResizedComponent(baseComponent, width, height);
    }

    public boolean allowEditCaptionAndValue() {
        return allowEditCaptionAndValue;
    }

    public boolean allowEditOfCaptionOnClick() {
        return allowEditOfCaptionOnClick;
    }

    public Dimension getBoxSize() {
        return new Dimension(getWidth() + WidgetSelection.BOX_MARGIN * 2, getHeight() + WidgetSelection.BOX_MARGIN * 2);
    }

    public int getResizeTypeForSplitComponent(
            final int mouseX,
            final int mouseY) {
        final SplitComponent sc = (SplitComponent) baseComponent;

        if (sc.getCaptionPosition() == SplitComponent.CAPTION_NONE) {
            return -1;
        }

        final int widgetX = getX();
        final int widgetY = getY();

        int resizeType = -1;

        final int dividorLocation = sc.getDividerLocation();
        final int orientation = sc.getOrientation();

        if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
            if (mouseY >= widgetY && mouseY <= widgetY + getHeight()
                    && mouseX > (widgetX + dividorLocation) - 3
                    && mouseX < (widgetX + dividorLocation + 3)) {

                resizeType = DesignerMouseMotionListener.RESIZE_SPLIT_HORIZONTAL_CURSOR;
            }
        } else {
            if (mouseX >= widgetX && mouseX <= widgetX + getWidth()
                    && mouseY > (widgetY + dividorLocation) - 3
                    && mouseY < (widgetY + dividorLocation + 3)) {

                resizeType = DesignerMouseMotionListener.RESIZE_SPLIT_VERTICAL_CURSOR;
            }
        }
        return resizeType;
    }

    public JComponent getValueComponent() {
        final JComponent value;
        if (isComponentSplit) {
            value = ((SplitComponent) baseComponent).getValue();
        } else {
            value = baseComponent;
        }

        return value;
    }

    public PdfCaption getCaptionComponent() {
        PdfCaption caption = null;
        if (isComponentSplit) {
            caption = ((SplitComponent) baseComponent).getCaption();
        }

        return caption;
    }

    public void setLastX(final int lastX) {
        this.lastX = lastX;
    }

    public void setLastY(final int lastY) {
        this.lastY = lastY;
    }

    public int getLastX() {
        return lastX;
    }

    public int getLastY() {
        return lastY;
    }

    public Point getAbsoluteLocationsOfValue() {
        Point location = null;

        if (isComponentSplit) {
            final int captionPosition = ((SplitComponent) baseComponent).getCaptionPosition();

            switch (captionPosition) {
                case SplitComponent.CAPTION_LEFT:
                    location = new Point(getX() + getCaptionComponent().getBounds().width + SplitComponent.DIVIDER_SIZE, getY());
                    break;
                case SplitComponent.CAPTION_TOP:
                    location = new Point(getX(), getY() + getCaptionComponent().getBounds().height + SplitComponent.DIVIDER_SIZE);
                    break;
                case SplitComponent.CAPTION_RIGHT:
                case SplitComponent.CAPTION_BOTTOM:
                case SplitComponent.CAPTION_NONE:
                    location = new Point(getX(), getY());
                    break;
                default:
                    break;
            }
        }

        return location;
    }

    public Point getAbsoluteLocationsOfCaption() {
        Point location = null;

        if (isComponentSplit) {
            final int captionPosition = ((SplitComponent) baseComponent).getCaptionPosition();

            switch (captionPosition) {
                case SplitComponent.CAPTION_LEFT:
                case SplitComponent.CAPTION_TOP:
                    location = new Point(getX(), getY());
                    break;
                case SplitComponent.CAPTION_RIGHT:
                    location = new Point(getX() + getValueComponent().getBounds().width + SplitComponent.DIVIDER_SIZE, getY());
                    break;
                case SplitComponent.CAPTION_BOTTOM:
                    location = new Point(getX(), getY() + getValueComponent().getBounds().height + SplitComponent.DIVIDER_SIZE);
                    break;
                default:
                    break;
            }
        }

        if (location == null) {
            location = new Point(getX(), getY());
        }

        return location;
    }

    public boolean isComponentSplit() {
        return isComponentSplit;
    }

    public void setResizeHeightRatio(final double resizeHeightRatio) {
        this.resizeHeightRatio = resizeHeightRatio;
    }

    public void setResizeWidthRatio(final double resizeWidthRatio) {
        this.resizeWidthRatio = resizeWidthRatio;
    }

    public double getResizeHeightRatio() {
        return resizeHeightRatio;
    }

    public double getResizeWidthRatio() {
        return resizeWidthRatio;
    }

    public void setResizeFromTopRatio(final double resizeFromTopRatio) {
        this.resizeFromTopRatio = resizeFromTopRatio;
    }

    public void setResizeFromLeftRatio(final double resizeWidthFromLeftRatio) {
        this.resizeWidthFromLeftRatio = resizeWidthFromLeftRatio;
    }

    public double getResizeFromTopRatio() {
        return resizeFromTopRatio;
    }

    public double getResizeFromLeftRatio() {
        return resizeWidthFromLeftRatio;
    }

    public List<IWidget> getWidgetsInGroup() {
        return Collections.emptyList();
    }

    public void setWidgetsInGroup(final List<IWidget> widgetsInGroup) {
    }

    public int getType() {
        return type;
    }

    public String getWidgetName() {
        return widgetName;
    }

    public int getArrayNumber() {
        return arrayNumber;
    }

    public Icon getIcon() {
        return icon;
    }

    public Document getProperties() {
        return properties;
    }

    public void setParagraphProperties(
            final Element paragraphProperties,
            final int currentlyEditing) {
    }

    public void setLayoutProperties(final Element paragraphProperties) {
    }

    public void setFontProperties(
            final Element parentElement,
            final int currentlyEditing) {
    }

    protected Element setupProperties() {
        try {
            properties = XMLUtils.createNewDocument();
        } catch (ParserConfigurationException e) {
            logger.error("Error setting up properties", e);
        }

        return XMLUtils.createAndAppendElement(properties, "widget", properties);
    }

    protected void setFontProperties(
            final Element properties,
            final IPdfComponent component) {
        final Optional<String> fontNameProperty = XMLUtils.getAttributeValueFromChildElement(properties, "Font Name");
        final Optional<String> fontSizeProperty = XMLUtils.getAttributeValueFromChildElement(properties, "Font Size");
        final Optional<String> fontStyleProperty = XMLUtils.getAttributeValueFromChildElement(properties, "Font Style");
        final Optional<String> underlineProperty = XMLUtils.getAttributeValueFromChildElement(properties, "Underline");
        final Optional<String> strikethroughProperty = XMLUtils.getAttributeValueFromChildElement(properties, "Strikethrough");
        final Optional<String> colorProperty = XMLUtils.getAttributeValueFromChildElement(properties, "Color");

        final Font baseFont = fontHandler.getFontFromName(fontNameProperty.get());

        final Map<TextAttribute, Float> fontAttrs = new HashMap<>();
        fontAttrs.put(TextAttribute.SIZE, Float.valueOf(fontSizeProperty.get()));

        final int fontStyle = Integer.parseInt(fontStyleProperty.get());

        switch (fontStyle) {
            case 0:
                fontAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
                break;
            case 1:
                fontAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
                break;
            case 2:
                fontAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
                fontAttrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
                break;
            case 3:
                fontAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
                fontAttrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
                break;
            default:
                break;
        }

        final Font fontToUse = baseFont.deriveFont(fontAttrs);

        component.setFont(fontToUse);

        final int underline = Integer.parseInt(underlineProperty.get());
        component.setUnderlineType(underline);

        final boolean isStrikethrough = Integer.parseInt(strikethroughProperty.get()) == IWidget.STRIKETHROUGH_ON;
        component.setStrikethrough(isStrikethrough);

        final Color color = new Color(Integer.parseInt(colorProperty.get()));
        component.setForeground(color);
    }

    void setSizeAndPosition(final Element layoutPropertiesElement) {
        final Element sizeAndPositionElement = (Element) layoutPropertiesElement.getElementsByTagName("sizeandposition").item(0);

        final int x = Integer.parseInt(XMLUtils.getAttributeValueFromChildElement(sizeAndPositionElement, "X").orElse("0"));
        final int width = Integer.parseInt(XMLUtils.getAttributeValueFromChildElement(sizeAndPositionElement, "Width").orElse("25"));
        final int y = Integer.parseInt(XMLUtils.getAttributeValueFromChildElement(sizeAndPositionElement, "Y").orElse("0"));
        final int height = Integer.parseInt(XMLUtils.getAttributeValueFromChildElement(sizeAndPositionElement, "Height").orElse("25"));

        setPosition(x, y);
        setSize(width, height);
    }

    protected void setParagraphProperties(
            final Element captionPropertiesElement,
            final IPdfComponent component) {
        final Optional<String> horizontalAlignment = XMLUtils.getAttributeValueFromChildElement(captionPropertiesElement, "Horizontal Alignment");
        final Optional<String> verticallAlignment = XMLUtils.getAttributeValueFromChildElement(captionPropertiesElement, "Vertical Alignment");

        if (component instanceof PdfCaption) {
            String text = component.getText();
            text = Strip.stripXML(text).toString();
            text = "<html><p align=" + horizontalAlignment + ">" + text;
            component.setText(text);
        }

        if (horizontalAlignment.isPresent()) {
            final String alignment = horizontalAlignment.get();
            switch (alignment) {
                case "justify":
                case "left":
                    component.setHorizontalAlignment(SwingConstants.LEFT);
                    break;
                case "right":
                    component.setHorizontalAlignment(SwingConstants.RIGHT);
                    break;
                case "center":
                    component.setHorizontalAlignment(SwingConstants.CENTER);
                    break;
                default:
                    logger.warn("Unexpected horizontal alignment {}", alignment);
                    break;
            }
        }

        if (verticallAlignment.isPresent()) {
            final String alignment = verticallAlignment.get();
            switch (alignment) {
                case "center":
                    component.setVerticalAlignment(SwingConstants.CENTER);
                    break;
                case "top":
                    component.setVerticalAlignment(SwingConstants.TOP);
                    break;
                case "bottom":
                    component.setVerticalAlignment(SwingConstants.BOTTOM);
                    break;
                default:
                    logger.warn("Unexpected vertical alignment {}", alignment);
                    break;
            }
        }

        setSize(getWidth(), getHeight());
    }

    public void setAllProperties() {

        final Element root = properties.getDocumentElement();

        setParagraphProperties(root, IWidget.COMPONENT_BOTH);
        setLayoutProperties(root);
        setFontProperties(root, IWidget.COMPONENT_BOTH);
        setObjectProperties(root);
        setCaptionProperties(root);
    }

    void addJavaScript(final Element rootElement) {
        final Element javaScriptElement = XMLUtils.createAndAppendElement(properties, "javascript", rootElement);

        final Element mouseEnterElement = XMLUtils.createAndAppendElement(properties, "mouseEnter", javaScriptElement);
        mouseEnterElement.appendChild(properties.createTextNode(""));

        final Element mouseExitElement = XMLUtils.createAndAppendElement(properties, "mouseExit", javaScriptElement);
        mouseExitElement.appendChild(properties.createTextNode(""));

        final Element changeElement = XMLUtils.createAndAppendElement(properties, "change", javaScriptElement);
        changeElement.appendChild(properties.createTextNode(""));

        final Element mouseUpElement = XMLUtils.createAndAppendElement(properties, "mouseUp", javaScriptElement);
        mouseUpElement.appendChild(properties.createTextNode(""));

        final Element mouseDownElement = XMLUtils.createAndAppendElement(properties, "mouseDown", javaScriptElement);
        mouseDownElement.appendChild(properties.createTextNode(""));

        if (getType() == IWidget.TEXT_FIELD) {
            final Element keystrokeElement = XMLUtils.createAndAppendElement(properties, "keystroke", javaScriptElement);
            keystrokeElement.appendChild(properties.createTextNode(""));
        }
    }

    public void setObjectProperties(final Element parentElement) {
    }

    void setBindingProperties(final Element objectPropertiesElement) {
        final Element bindingPropertiesElement = (Element) objectPropertiesElement.getElementsByTagName("binding").item(0);

        widgetName = XMLUtils.getAttributeValueFromChildElement(bindingPropertiesElement, "Name").orElse("");
        arrayNumber = Integer.parseInt(XMLUtils.getAttributeValueFromChildElement(bindingPropertiesElement, "Array Number").orElse("0"));

        setSize(getWidth(), getHeight());
    }

    public void setCaptionProperties(final Element captionPropertiesElement) {
        if (isComponentSplit
                && ((SplitComponent) baseComponent).getCaptionPosition() != SplitComponent.CAPTION_NONE) {

            final Element captionProperties = (Element) properties.getElementsByTagName("caption_properties").item(0);

            final String captionText = XMLUtils.getAttributeValueFromChildElement(captionProperties, "Text").orElse("");
            getCaptionComponent().setText(captionText);

            final Optional<String> stringLocation = XMLUtils.getAttributeValueFromChildElement(captionProperties, "Divisor Location");
            if (stringLocation.isPresent() && !stringLocation.get().equals("")) {
                final int divisorLocation = Integer.parseInt(stringLocation.get());
                final SplitComponent ptf = (SplitComponent) baseComponent;
                ptf.setDividerLocation(divisorLocation);
            }

            setSize(getWidth(), getHeight());
        }
    }

    public void setBorderAndBackgroundProperties(final Element borderPropertiesElement) {
        final JComponent component = getValueComponent();

        final Element borderProperties = (Element) borderPropertiesElement.getElementsByTagName("borders").item(0);

        final String borderStyle = XMLUtils.getAttributeValueFromChildElement(borderProperties, "Border Style").orElse("None");
        if (borderStyle.equals("None")) {
            component.setBorder(null);
        } else {
            final Optional<String> leftEdgeWidth = XMLUtils.getAttributeValueFromChildElement(borderProperties, "Border Width");
            final Optional<String> leftEdgeColor = XMLUtils.getAttributeValueFromChildElement(borderProperties, "Border Color");

            final Map<String, String> borderPropertiesMap = new HashMap<>();
            if (borderStyle.equals("Beveled")) {
                borderPropertiesMap.put("S", "/B");
            }
            if (borderStyle.equals("Solid")) {
                borderPropertiesMap.put("S", "/S");
            }
            if (borderStyle.equals("Dashed")) {
                borderPropertiesMap.put("S", "/D");
            }

            if (leftEdgeWidth.isPresent() && leftEdgeWidth.get().length() > 0) {
                borderPropertiesMap.put("W", leftEdgeWidth.get());
            }

            final Color color = new Color(Integer.parseInt(leftEdgeColor.get()));
            final Border border = JPedalBorderFactory.createBorderStyle(borderPropertiesMap, color, color);

            component.setBorder(border);
        }

        final Element backgroundFillProperties =
                (Element) borderPropertiesElement.getElementsByTagName("backgroundfill").item(0);

        final String backgroundColor = XMLUtils.getAttributeValueFromChildElement(backgroundFillProperties, "Fill Color").orElse(String.valueOf(Color.WHITE.getRGB()));
        component.setBackground(new Color(Integer.parseInt(backgroundColor)));

        setSize(getWidth(), getHeight());
    }

    public void setComponent(final JComponent component) {
        this.component = component;
    }

    public void setProperties(final Document properties) {
        this.properties = properties;
    }

    public void setComponentSplit(final boolean componentSplit) {
        isComponentSplit = componentSplit;
    }

    public void setAllowEditCaptionAndValue(final boolean allowEditCaptionAndValue) {
        this.allowEditCaptionAndValue = allowEditCaptionAndValue;
    }

    public void setAllowEditOfCaptionOnClick(final boolean allowEditOfCaptionOnClick) {
        this.allowEditOfCaptionOnClick = allowEditOfCaptionOnClick;
    }

    public void setWidgetName(final String widgetName) {
        this.widgetName = widgetName;
    }

    public void setArrayNumber(final int arrayNumber) {
        this.arrayNumber = arrayNumber;
    }

    public JComponent getComponent() {
        return component;
    }

    public JComponent getBaseComponent() {
        return baseComponent;
    }

    public boolean isAllowEditCaptionAndValue() {
        return allowEditCaptionAndValue;
    }

    public boolean isAllowEditOfCaptionOnClick() {
        return allowEditOfCaptionOnClick;
    }

    public Point getPosition() {
        return position;
    }

    public double getResizeWidthFromLeftRatio() {
        return resizeWidthFromLeftRatio;
    }
}
