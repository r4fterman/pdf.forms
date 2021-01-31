package org.pdf.forms.widgets;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.*;
import javax.swing.border.Border;

import org.apache.commons.text.StringEscapeUtils;
import org.jpedal.objects.acroforms.creation.JPedalBorderFactory;
import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.designer.listeners.DesignerMouseMotionListener;
import org.pdf.forms.model.des.BackgroundFill;
import org.pdf.forms.model.des.BindingProperties;
import org.pdf.forms.model.des.Borders;
import org.pdf.forms.model.des.CaptionProperties;
import org.pdf.forms.model.des.FontCaption;
import org.pdf.forms.model.des.JavaScriptContent;
import org.pdf.forms.model.des.SizeAndPosition;
import org.pdf.forms.widgets.components.IPdfComponent;
import org.pdf.forms.widgets.components.PdfCaption;
import org.pdf.forms.widgets.components.SplitComponent;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.pdf.forms.widgets.utils.WidgetSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Widget implements IWidget {

    private final Logger logger = LoggerFactory.getLogger(Widget.class);

    private final JComponent baseComponent;
    private final Point position;
    private final int type;
    private final Icon icon;
    private final FontHandler fontHandler;

    private JComponent component;
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
    private final org.pdf.forms.model.des.Widget widget;

    public Widget(
            final org.pdf.forms.model.des.Widget widget,
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final String iconLocation,
            final FontHandler fontHandler) {
        this.widget = widget;
        this.type = type;
        this.component = component;
        this.baseComponent = baseComponent;
        this.fontHandler = fontHandler;

        position = new Point(0, 0);
        icon = new ImageIcon(getClass().getResource(iconLocation));
    }

    @Override
    public JComponent getWidget() {
        return component;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void setPosition(
            final int x,
            final int y) {
        position.x = x;
        position.y = y;
    }

    @Override
    public void setX(final int x) {
        position.x = x;
    }

    @Override
    public void setY(final int y) {
        position.y = y;
    }

    @Override
    public int getX() {
        return position.x;
    }

    @Override
    public int getY() {
        return position.y;
    }

    @Override
    public int getWidth() {
        return component.getWidth();
    }

    @Override
    public int getHeight() {
        return component.getHeight();
    }

    @Override
    public void setSize(
            final int width,
            final int height) {
        component = WidgetFactory.createResizedComponent(baseComponent, width, height);
    }

    @Override
    public boolean allowEditCaptionAndValue() {
        return allowEditCaptionAndValue;
    }

    @Override
    public boolean allowEditOfCaptionOnClick() {
        return allowEditOfCaptionOnClick;
    }

    @Override
    public Dimension getBoxSize() {
        return new Dimension(getWidth() + WidgetSelection.BOX_MARGIN * 2, getHeight() + WidgetSelection.BOX_MARGIN * 2);
    }

    @Override
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

    @Override
    public JComponent getValueComponent() {
        final JComponent value;
        if (isComponentSplit) {
            value = ((SplitComponent) baseComponent).getValue();
        } else {
            value = baseComponent;
        }

        return value;
    }

    @Override
    public PdfCaption getCaptionComponent() {
        PdfCaption caption = null;
        if (isComponentSplit) {
            caption = ((SplitComponent) baseComponent).getCaption();
        }

        return caption;
    }

    @Override
    public void setLastX(final int lastX) {
        this.lastX = lastX;
    }

    @Override
    public void setLastY(final int lastY) {
        this.lastY = lastY;
    }

    @Override
    public int getLastX() {
        return lastX;
    }

    @Override
    public int getLastY() {
        return lastY;
    }

    @Override
    public Point getAbsoluteLocationsOfValue() {
        Point location = null;

        if (isComponentSplit) {
            final int captionPosition = ((SplitComponent) baseComponent).getCaptionPosition();

            switch (captionPosition) {
                case SplitComponent.CAPTION_LEFT:
                    location = new Point(getX() + getCaptionComponent().getBounds().width + SplitComponent.DIVIDER_SIZE,
                            getY());
                    break;
                case SplitComponent.CAPTION_TOP:
                    location = new Point(getX(),
                            getY() + getCaptionComponent().getBounds().height + SplitComponent.DIVIDER_SIZE);
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

    @Override
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
                    location = new Point(getX() + getValueComponent().getBounds().width + SplitComponent.DIVIDER_SIZE,
                            getY());
                    break;
                case SplitComponent.CAPTION_BOTTOM:
                    location = new Point(getX(),
                            getY() + getValueComponent().getBounds().height + SplitComponent.DIVIDER_SIZE);
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

    @Override
    public boolean isComponentSplit() {
        return isComponentSplit;
    }

    @Override
    public void setResizeHeightRatio(final double resizeHeightRatio) {
        this.resizeHeightRatio = resizeHeightRatio;
    }

    @Override
    public void setResizeWidthRatio(final double resizeWidthRatio) {
        this.resizeWidthRatio = resizeWidthRatio;
    }

    @Override
    public double getResizeHeightRatio() {
        return resizeHeightRatio;
    }

    @Override
    public double getResizeWidthRatio() {
        return resizeWidthRatio;
    }

    @Override
    public void setResizeFromTopRatio(final double resizeFromTopRatio) {
        this.resizeFromTopRatio = resizeFromTopRatio;
    }

    @Override
    public void setResizeFromLeftRatio(final double resizeWidthFromLeftRatio) {
        this.resizeWidthFromLeftRatio = resizeWidthFromLeftRatio;
    }

    @Override
    public double getResizeFromTopRatio() {
        return resizeFromTopRatio;
    }

    @Override
    public double getResizeFromLeftRatio() {
        return resizeWidthFromLeftRatio;
    }

    @Override
    public List<IWidget> getWidgetsInGroup() {
        return Collections.emptyList();
    }

    @Override
    public void setWidgetsInGroup(final List<IWidget> widgetsInGroup) {
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getWidgetName() {
        return widgetName;
    }

    @Override
    public int getArrayNumber() {
        return arrayNumber;
    }

    @Override
    public JavaScriptContent getJavaScript() {
        return widget.getJavaScript();
    }

    @Override
    public org.pdf.forms.model.des.Widget getWidgetModel() {
        return widget;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public void setParagraphProperties(final int currentlyEditing) {
        // By default do nothing
    }

    @Override
    public void setLayoutProperties() {
        // By default do nothing
    }

    @Override
    public void setFontProperties(final int currentlyEditing) {
        // By default do nothing
    }

    protected void setFontProperties(final IPdfComponent component) {
        final FontCaption fontCaption = getWidgetModel().getProperties().getFont().getFontCaption();

        final String fontName = fontCaption.getFontName().orElse("Arial");
        final Font baseFont = fontHandler.getFontFromName(fontName);

        final Map<TextAttribute, Float> fontAttrs = new HashMap<>();
        final int fontSize = fontCaption.getFontSize().map(Integer::parseInt).orElse(12);
        fontAttrs.put(TextAttribute.SIZE, (float) fontSize);

        final int fontStyle = fontCaption.getFontStyle().map(Integer::parseInt).orElse(IWidget.STYLE_PLAIN);
        switch (fontStyle) {
            case IWidget.STYLE_PLAIN:
                fontAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
                break;
            case IWidget.STYLE_BOLD:
                fontAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
                break;
            case IWidget.STYLE_ITALIC:
                fontAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
                fontAttrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
                break;
            case IWidget.STYLE_BOLDITALIC:
                fontAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
                fontAttrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
                break;
            default:
                break;
        }

        final Font fontToUse = baseFont.deriveFont(fontAttrs);

        component.setFont(fontToUse);

        final int underline = fontCaption.getUnderline().map(Integer::parseInt).orElse(IWidget.UNDERLINE_NONE);
        component.setUnderlineType(underline);

        final int strikeThroughOnOff = fontCaption.getStrikeThrough().map(Integer::parseInt)
                .orElse(IWidget.STRIKETHROUGH_OFF);
        component.setStrikethrough(strikeThroughOnOff == IWidget.STRIKETHROUGH_ON);

        final Color color = fontCaption.getColor().map(c -> new Color(Integer.parseInt(c))).orElse(Color.BLACK);
        component.setForeground(color);
    }

    void setSizeAndPosition() {
        final SizeAndPosition sizeAndPosition = getWidgetModel().getProperties().getLayout().getSizeAndPosition();

        final int x = sizeAndPosition.getX().map(Integer::parseInt).orElse(0);
        final int width = sizeAndPosition.getWidth().map(Integer::parseInt).orElse(25);
        final int y = sizeAndPosition.getY().map(Integer::parseInt).orElse(0);
        final int height = sizeAndPosition.getHeight().map(Integer::parseInt).orElse(25);

        setPosition(x, y);
        setSize(width, height);
    }

    protected void setParagraphProperties(final IPdfComponent component) {
        final Optional<String> horizontalAlignment = getWidgetModel().getProperties().getParagraph()
                .getParagraphCaption().getHorizontalAlignment();
        final Optional<String> verticalAlignment = getWidgetModel().getProperties().getParagraph().getParagraphCaption()
                .getVerticalAlignment();

        if (component instanceof PdfCaption) {
            String text = component.getText();
            text = StringEscapeUtils.escapeXml11(text);
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

        if (verticalAlignment.isPresent()) {
            final String alignment = verticalAlignment.get();
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

    @Override
    public void setAllProperties() {
        setParagraphProperties(IWidget.COMPONENT_BOTH);
        setLayoutProperties();
        setFontProperties(IWidget.COMPONENT_BOTH);
        setObjectProperties();
        setCaptionProperties();
    }

    void addJavaScript() {
        final JavaScriptContent javaScript = getWidgetModel().getJavaScript();

        javaScript.setMouseEnter("");
        javaScript.setMouseExit("");
        javaScript.setChange("");
        javaScript.setMouseUp("");
        javaScript.setMouseDown("");

        if (getType() == IWidget.TEXT_FIELD) {
            javaScript.setKeystroke("");
        }
    }

    @Override
    public void setObjectProperties() {
    }

    void setBindingProperties() {
        final BindingProperties bindingProperties = getWidgetModel().getProperties().getObject().getBinding();

        widgetName = bindingProperties.getName().orElse("");
        arrayNumber = bindingProperties.getArrayNumber().map(Integer::parseInt).orElse(0);

        setSize(getWidth(), getHeight());
    }

    @Override
    public void setCaptionProperties() {
        if (isComponentSplit
                && ((SplitComponent) baseComponent).getCaptionPosition() != SplitComponent.CAPTION_NONE) {
            final CaptionProperties captionProperties = getWidgetModel().getProperties().getCaptionProperties();

            final String captionText = captionProperties.getTextValue().orElse("");
            getCaptionComponent().setText(captionText);

            captionProperties.getDividerLocation()
                    .filter(location -> !location.isEmpty())
                    .map(Integer::parseInt)
                    .ifPresent(((SplitComponent) baseComponent)::setDividerLocation);

            setSize(getWidth(), getHeight());
        }
    }

    @Override
    public void setBorderAndBackgroundProperties() {
        final JComponent component = getValueComponent();

        final Borders borders = getWidgetModel().getProperties().getBorder().getBorders();

        final String borderStyle = borders.getBorderStyle().orElse("None");
        if (borderStyle.equals("None")) {
            component.setBorder(null);
        } else {
            final Optional<String> leftEdgeWidth = borders.getBorderWidth();

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

            final Color color = borders.getBorderColor().map(c -> new Color(Integer.parseInt(c))).orElse(Color.BLACK);
            final Border border = JPedalBorderFactory.createBorderStyle(borderPropertiesMap, color, color);

            component.setBorder(border);
        }

        final BackgroundFill backgroundFill = getWidgetModel().getProperties().getBorder().getBackgroundFill();
        final Color backgroundColor = backgroundFill.getFillColor().map(c -> new Color(Integer.parseInt(c))).orElse(
                Color.WHITE);
        component.setBackground(backgroundColor);

        setSize(getWidth(), getHeight());
    }

    public void setComponent(final JComponent component) {
        this.component = component;
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

    public Point getPosition() {
        return position;
    }

    FontHandler getFontHandler() {
        return fontHandler;
    }
}
