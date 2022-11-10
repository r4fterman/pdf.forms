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
import org.pdf.forms.model.des.BindingProperties;
import org.pdf.forms.model.des.Borders;
import org.pdf.forms.model.des.CaptionProperties;
import org.pdf.forms.model.des.FontCaption;
import org.pdf.forms.model.des.FontProperties;
import org.pdf.forms.model.des.JavaScriptContent;
import org.pdf.forms.model.des.ParagraphCaption;
import org.pdf.forms.model.des.ParagraphProperties;
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

    private final org.pdf.forms.model.des.Widget widget;
    private final JComponent baseComponent;
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

    Widget(
            final org.pdf.forms.model.des.Widget widget,
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final String iconLocation,
            final FontHandler fontHandler) {
        this.widget = widget;
        this.type = type;
        this.baseComponent = baseComponent;
        this.component = component;
        this.fontHandler = fontHandler;
        this.icon = new ImageIcon(getClass().getResource(iconLocation));

//        final SizeAndPosition sizeAndPosition = widget.getProperties().getLayout().getSizeAndPosition();
//        if (sizeAndPosition.getX().map(Integer::valueOf).orElse(1).equals(1)) {
//            sizeAndPosition.setX(component.getX());
//        }
//        if (sizeAndPosition.getY().map(Integer::valueOf).orElse(1).equals(1)) {
//            sizeAndPosition.setY(component.getY());
//        }
//        if (sizeAndPosition.getWidth().map(Integer::valueOf).orElse(1).equals(1)) {
//            sizeAndPosition.setWidth(component.getWidth());
//        }
//        if (sizeAndPosition.getHeight().map(Integer::valueOf).orElse(1).equals(1)) {
//            sizeAndPosition.setHeight(component.getHeight());
//        }
    }

    @Override
    public JComponent getComponent() {
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
        final SizeAndPosition sizeAndPosition = getWidgetModel().getProperties().getLayout().getSizeAndPosition();
        sizeAndPosition.setX(x);
        sizeAndPosition.setY(y);
    }

    @Override
    public void setX(final int x) {
        final SizeAndPosition sizeAndPosition = getWidgetModel().getProperties().getLayout().getSizeAndPosition();
        sizeAndPosition.setX(x);
    }

    @Override
    public void setY(final int y) {
        final SizeAndPosition sizeAndPosition = getWidgetModel().getProperties().getLayout().getSizeAndPosition();
        sizeAndPosition.setY(y);
    }

    @Override
    public int getX() {
        final SizeAndPosition sizeAndPosition = getWidgetModel().getProperties().getLayout().getSizeAndPosition();
        return sizeAndPosition.getX().map(Integer::valueOf).orElse(1);
    }

    @Override
    public int getY() {
        final SizeAndPosition sizeAndPosition = getWidgetModel().getProperties().getLayout().getSizeAndPosition();
        return sizeAndPosition.getY().map(Integer::valueOf).orElse(1);
    }

    @Override
    public int getWidth() {
        final SizeAndPosition sizeAndPosition = getWidgetModel().getProperties().getLayout().getSizeAndPosition();
        return sizeAndPosition.getWidth().map(Integer::valueOf).orElse(1);
    }

    @Override
    public int getHeight() {
        final SizeAndPosition sizeAndPosition = getWidgetModel().getProperties().getLayout().getSizeAndPosition();
        return sizeAndPosition.getHeight().map(Integer::valueOf).orElse(1);
    }

    @Override
    public void setSize(
            final int width,
            final int height) {
        final SizeAndPosition sizeAndPosition = getWidgetModel().getProperties().getLayout().getSizeAndPosition();
        sizeAndPosition.setWidth(width);
        sizeAndPosition.setHeight(height);

        this.component = WidgetFactory.createResizedComponent(baseComponent, width, height);
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
        final Optional<FontProperties> font = getWidgetModel().getProperties().getFont();
        if (font.isEmpty()) {
            return;
        }

        final FontCaption fontCaption = font.get().getFontCaption();

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
        final Optional<ParagraphProperties> paragraphProperties = getWidgetModel().getProperties().getParagraph();
        final Optional<String> horizontalAlignment = paragraphProperties.flatMap(paragraph -> paragraph.getParagraphCaption().flatMap(ParagraphCaption::getHorizontalAlignment));

        if (component instanceof PdfCaption) {
            final String componentText = component.getText();
            final String escapedComponentText = StringEscapeUtils.escapeXml11(componentText);
            final String htmlText = "<html><p align=" + horizontalAlignment + ">" + escapedComponentText;
            component.setText(htmlText);
        }

        horizontalAlignment.ifPresent(alignment -> setHorizontalAlignmentToComponent(alignment, component));

        final Optional<String> verticalAlignment = paragraphProperties.flatMap(paragraph -> paragraph.getParagraphCaption().flatMap(ParagraphCaption::getVerticalAlignment));
        verticalAlignment.ifPresent(alignment -> setVerticalAlignmentToComponent(alignment, component));

        setSize(getWidth(), getHeight());
    }

    private void setHorizontalAlignmentToComponent(
            final String alignment,
            final IPdfComponent component) {
        if ("justify".equals(alignment) || "left".equals(alignment)) {
            component.setHorizontalAlignment(SwingConstants.LEFT);
        } else if ("right".equals(alignment)) {
            component.setHorizontalAlignment(SwingConstants.RIGHT);
        } else if ("center".equals(alignment)) {
            component.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            logger.warn("Unexpected horizontal alignment [{}]", alignment);
        }
    }

    private void setVerticalAlignmentToComponent(
            final String alignment,
            final IPdfComponent component) {
        if ("center".equals(alignment)) {
            component.setVerticalAlignment(SwingConstants.CENTER);
        } else if ("top".equals(alignment)) {
            component.setVerticalAlignment(SwingConstants.TOP);
        } else if ("bottom".equals(alignment)) {
            component.setVerticalAlignment(SwingConstants.BOTTOM);
        } else {
            logger.warn("Unexpected vertical alignment [{}]", alignment);
        }
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
        final JavaScriptContent javaScript = new JavaScriptContent();

        javaScript.setMouseEnter("");
        javaScript.setMouseExit("");
        javaScript.setChange("");
        javaScript.setMouseUp("");
        javaScript.setMouseDown("");

        if (getType() == IWidget.TEXT_FIELD) {
            javaScript.setKeystroke("");
        }

        getWidgetModel().setJavaScript(javaScript);
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
        final JComponent valueComponent = getValueComponent();

        getWidgetModel().getProperties().getBorder()
                .ifPresent(borderProperties -> setBordersOnValueComponent(valueComponent,
                        borderProperties.getBorders()));

        setBackgroundOnValueComponent(valueComponent);

        setSize(getWidth(), getHeight());
    }

    private void setBordersOnValueComponent(
            final JComponent valueComponent,
            final Borders borders) {
        final String borderStyle = borders.getBorderStyle().orElse("None");
        if (borderStyle.equals("None")) {
            valueComponent.setBorder(null);
            return;
        }

        final Map<String, String> borderPropertiesMap = buildBorderPropertiesMap(borderStyle, borders.getBorderWidth());
        final Color color = borders.getBorderColor()
                .map(c -> new Color(Integer.parseInt(c)))
                .orElse(Color.BLACK);
        final Border border = JPedalBorderFactory.createBorderStyle(borderPropertiesMap, color, color);

        valueComponent.setBorder(border);
    }

    private Map<String, String> buildBorderPropertiesMap(
            final String borderStyle,
            final Optional<String> borderWidth) {
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
        borderWidth
                .map(Integer::valueOf)
                .filter(width -> width > 0)
                .ifPresent(width -> borderPropertiesMap.put("W", String.valueOf(width)));

        return borderPropertiesMap;
    }

    private void setBackgroundOnValueComponent(final JComponent valueComponent) {
        final Color backgroundColor = getWidgetModel().getProperties().getBorder()
                .map(borderProperties -> borderProperties.getBackgroundFill().getFillColor()
                        .map(c -> new Color(Integer.parseInt(c)))
                        .orElse(Color.WHITE))
                .orElse(Color.WHITE);

        valueComponent.setBackground(backgroundColor);
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

    public JComponent getBaseComponent() {
        return baseComponent;
    }

    FontHandler getFontHandler() {
        return fontHandler;
    }
}
