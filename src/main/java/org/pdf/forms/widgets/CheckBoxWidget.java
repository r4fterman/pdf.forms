package org.pdf.forms.widgets;

import java.awt.Color;
import java.awt.Image;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.components.CheckBoxIcon;
import org.pdf.forms.widgets.components.PdfCheckBox;
import org.pdf.forms.widgets.components.SplitComponent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CheckBoxWidget extends Widget implements IWidget {

    private static int nextWidgetNumber = 1;

    private final FontHandler fontHandler;

    public CheckBoxWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final FontHandler fontHandler) {
        super(type, baseComponent, component, "/org/pdf/forms/res/Check Box.gif", fontHandler);
        this.fontHandler = fontHandler;

        setComponentSplit(true);
        setAllowEditCaptionAndValue(false);
        setAllowEditOfCaptionOnClick(true);

        final String widgetName = "Check Box" + nextWidgetNumber;
        nextWidgetNumber++;

        setWidgetName(widgetName);

        final Element rootElement = setupProperties();

        XMLUtils.addBasicProperty(getProperties(), "type", "CHECK_BOX", rootElement);
        XMLUtils.addBasicProperty(getProperties(), "name", widgetName, rootElement);

        addProperties(rootElement);

        addJavaScript(rootElement);
    }

    public CheckBoxWidget(
            final int type,
            final JComponent baseComponent,
            final JComponent component,
            final Element root,
            final FontHandler fontHandler) {
        super(type, baseComponent, component, "/org/pdf/forms/res/Check Box.gif", fontHandler);
        this.fontHandler = fontHandler;

        setComponentSplit(true);
        setAllowEditCaptionAndValue(false);
        setAllowEditOfCaptionOnClick(true);

        final Element bindingElement = XMLUtils.getElementsFromNodeList(root.getElementsByTagName("binding")).get(0);
        setWidgetName(XMLUtils.getAttributeFromChildElement(bindingElement, "Name").orElse(""));
        setArrayNumber(Integer.parseInt(XMLUtils.getAttributeFromChildElement(bindingElement, "Array Number").orElse("0")));

        final Element rootElement = setupProperties();
        final Node newRoot = getProperties().importNode(root, true);

        getProperties().replaceChild(newRoot, rootElement);

        setAllProperties();
    }

    private void addProperties(final Element rootElement) {
        final Element propertiesElement = XMLUtils.createAndAppendElement(getProperties(), "properties", rootElement);

        addFontProperties(propertiesElement);

        addObjectProperties(propertiesElement);

        addLayoutProperties(propertiesElement);

        addBorderProperties(propertiesElement);

        addParagraphProperties(propertiesElement);

        addCaptionProperties(propertiesElement);
    }

    private void addCaptionProperties(final Element propertiesElement) {
        final Element captionElement = XMLUtils.createAndAppendElement(getProperties(), "caption_properties", propertiesElement);
        XMLUtils.addBasicProperty(getProperties(), "Text", "Check Box", captionElement);
        XMLUtils.addBasicProperty(getProperties(), "Divisor Location", "", captionElement);
    }

    private void addFontProperties(final Element propertiesElement) {
        final Element fontElement = XMLUtils.createAndAppendElement(getProperties(), "font", propertiesElement);

        final Element caption = XMLUtils.createAndAppendElement(getProperties(), "font_caption", fontElement);
        XMLUtils.addBasicProperty(getProperties(), "Font Name", fontHandler.getDefaultFont().getFontName(), caption);
        XMLUtils.addBasicProperty(getProperties(), "Font Size", "11", caption);
        XMLUtils.addBasicProperty(getProperties(), "Font Style", "0", caption);
        XMLUtils.addBasicProperty(getProperties(), "Underline", "0", caption);
        XMLUtils.addBasicProperty(getProperties(), "Strikethrough", "0", caption);
        XMLUtils.addBasicProperty(getProperties(), "Color", Color.BLACK.getRGB() + "", caption);
    }

    private void addObjectProperties(final Element propertiesElement) {
        final Element objectElement = XMLUtils.createAndAppendElement(getProperties(), "object", propertiesElement);

        final Element fieldElement = XMLUtils.createAndAppendElement(getProperties(), "field", objectElement);
        XMLUtils.addBasicProperty(getProperties(), "Appearance", "Sunken Box", fieldElement);
        XMLUtils.addBasicProperty(getProperties(), "Group Name", "", fieldElement);
        XMLUtils.addBasicProperty(getProperties(), "Presence", "Visible", fieldElement);

        final Element valueElement = XMLUtils.createAndAppendElement(getProperties(), "value", objectElement);
        XMLUtils.addBasicProperty(getProperties(), "Type", "User Entered - Optional", valueElement);
        XMLUtils.addBasicProperty(getProperties(), "Default", "Off", valueElement);

        final Element bindingElement = XMLUtils.createAndAppendElement(getProperties(), "binding", objectElement);
        XMLUtils.addBasicProperty(getProperties(), "Name", getWidgetName(), bindingElement);
        XMLUtils.addBasicProperty(getProperties(), "Array Number", "0", bindingElement);
    }

    private void addLayoutProperties(final Element propertiesElement) {
        final Element layoutElement = XMLUtils.createAndAppendElement(getProperties(), "layout", propertiesElement);

        final Element sizeAndPositionElement = XMLUtils.createAndAppendElement(getProperties(), "sizeandposition", layoutElement);
        XMLUtils.addBasicProperty(getProperties(), "X", "", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Width", "", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Y", "", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Height", "", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Expand to fit", "false", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Expand to fit", "false", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Anchor", "Top Left", sizeAndPositionElement);
        XMLUtils.addBasicProperty(getProperties(), "Rotation", "0", sizeAndPositionElement);

        final Element margins = XMLUtils.createAndAppendElement(getProperties(), "margins", layoutElement);
        XMLUtils.addBasicProperty(getProperties(), "Left", "2", margins);
        XMLUtils.addBasicProperty(getProperties(), "Right", "4", margins);
        XMLUtils.addBasicProperty(getProperties(), "Top", "2", margins);
        XMLUtils.addBasicProperty(getProperties(), "Bottom", "4", margins);

        final Element caption = XMLUtils.createAndAppendElement(getProperties(), "caption", layoutElement);
        XMLUtils.addBasicProperty(getProperties(), "Position", "Right", caption);
        XMLUtils.addBasicProperty(getProperties(), "Reserve", "4", caption);
    }

    private void addBorderProperties(final Element propertiesElement) {
        final Element borderElement = XMLUtils.createAndAppendElement(getProperties(), "border", propertiesElement);

        final Element borders = XMLUtils.createAndAppendElement(getProperties(), "borders", borderElement);
        XMLUtils.addBasicProperty(getProperties(), "Border Style", "None", borders);
        XMLUtils.addBasicProperty(getProperties(), "Border Width", "1", borders);
        XMLUtils.addBasicProperty(getProperties(), "Border Color", Color.BLACK.getRGB() + "", borders);

        final Element backgroundFill = XMLUtils.createAndAppendElement(getProperties(), "backgroundfill", borderElement);
        XMLUtils.addBasicProperty(getProperties(), "Style", "Solid", backgroundFill);
        XMLUtils.addBasicProperty(getProperties(), "Fill Color", Color.WHITE.getRGB() + "", backgroundFill);
    }

    private void addParagraphProperties(final Element propertiesElement) {
        final Element paragraphElement = XMLUtils.createAndAppendElement(getProperties(), "paragraph", propertiesElement);

        final Element value = XMLUtils.createAndAppendElement(getProperties(), "paragraph_caption", paragraphElement);
        XMLUtils.addBasicProperty(getProperties(), "Horizontal Alignment", "left", value);
        XMLUtils.addBasicProperty(getProperties(), "Vertical Alignment", "center", value);
    }

    @Override
    public void setParagraphProperties(
            final Element paragraphPropertiesElement,
            final int currentlyEditing) {

        final SplitComponent radioButton = (SplitComponent) getBaseComponent();

        final Element paragraphCaptionElement =
                (Element) paragraphPropertiesElement.getElementsByTagName("paragraph_caption").item(0);

        setParagraphProperties(paragraphCaptionElement, radioButton.getCaption());

        setSize(getWidth(), getHeight());
    }

    @Override
    public void setLayoutProperties(final Element layoutProperties) {
        setSizeAndPosition(layoutProperties);
        setCaptionLocation(layoutProperties);

        setSize(getWidth(), getHeight());
    }

    private void setCaptionLocation(final Element layoutProperties) {
        final int captionLocation = extractCaptionLocation(layoutProperties);

        final SplitComponent radioButton = (SplitComponent) getBaseComponent();
        if (radioButton.getCaptionPosition() != captionLocation) {
            radioButton.setCaptionPosition(captionLocation);
        }
    }

    private int extractCaptionLocation(final Element layoutProperties) {
        final Element captionElement = (Element) layoutProperties.getElementsByTagName("caption").item(0);
        return XMLUtils.getAttributeFromChildElement(captionElement, "Position")
                .map(caption -> new Caption(caption).getLocation())
                .orElse(Caption.DEFAULT_LOCATION);
    }

    @Override
    public void setFontProperties(
            final Element fontProperties,
            final int currentlyEditing) {
        final SplitComponent radioButton = (SplitComponent) getBaseComponent();

        final Element captionProperties = (Element) fontProperties.getElementsByTagName("font_caption").item(0);

        setFontProperties(captionProperties, radioButton.getCaption());

        setSize(getWidth(), getHeight());
    }

    @Override
    public void setObjectProperties(final Element objectProperties) {
        final JCheckBox comboBox = (JCheckBox) getValueComponent();

        final Element valueElement = (Element) objectProperties.getElementsByTagName("value").item(0);

        final String state = XMLUtils.getAttributeFromChildElement(valueElement, "Default").orElse("Off");

        comboBox.setSelected(state.equals("On"));

        /* set binding getProperties() */
        setBindingProperties(objectProperties);

        setSize(getWidth(), getHeight());
    }

    public void setCheckBoxGroupName(final String name) {
        final Element objectElement = XMLUtils.getElementsFromNodeList(getProperties().getElementsByTagName("object")).get(0);
        final Element groupNameProperty = XMLUtils.getPropertyElement(objectElement, "Group Name").get();
        groupNameProperty.getAttributeNode("value").setValue(name);
    }

    public String getCheckBoxGroupName() {
        final Element objectElement = XMLUtils.getElementsFromNodeList(getProperties().getElementsByTagName("object")).get(0);
        final Element groupNameProperty = XMLUtils.getPropertyElement(objectElement, "Group Name").get();
        return groupNameProperty.getAttributeNode("value").getValue();
    }

    public void setOnOffImage(
            final Image onImage,
            final Image offImage) {
        final PdfCheckBox checkBox = (PdfCheckBox) getValueComponent();
        final CheckBoxIcon checkBoxIcon = new CheckBoxIcon();
        checkBoxIcon.setOnOffImage(onImage, offImage);
        checkBox.setIcon(checkBoxIcon);
    }
}
