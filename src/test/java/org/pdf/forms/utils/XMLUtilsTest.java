package org.pdf.forms.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class XMLUtilsTest {

    private static final String FONT_ELEMENT = ""
            + "<font>"
            + "    <font_caption>"
            + "        <property name=\"Font Name\" value=\"Tahoma\"/>"
            + "        <property name=\"Font Size\" value=\"12\"/>"
            + "        <property name=\"Font Style\" value=\"1\"/>"
            + "        <property name=\"Underline\" value=\"0\"/>"
            + "        <property name=\"Strikethrough\" value=\"0\"/>"
            + "        <property name=\"Color\" value=\"-16777216\"/>"
            + "    </font_caption>"
            + "</font>";

    private static final String FONT_CAPTION_ELEMENT = ""
            + "<font_caption>"
            + "    <property name=\"Font Name\" value=\"Tahoma\"/>"
            + "    <property name=\"Font Size\" value=\"12\"/>"
            + "    <property name=\"Font Style\" value=\"1\"/>"
            + "    <property name=\"Underline\" value=\"0\"/>"
            + "    <property name=\"Strikethrough\" value=\"0\"/>"
            + "    <property name=\"Color\" value=\"-16777216\"/>"
            + "</font_caption>";

    @Test
    void getPropertyElement_should_return_child_property_element_with_given_name() throws Exception {
        final Optional<Element> propertyElement = XMLUtils.getPropertyElement(getElement(FONT_ELEMENT), "Font Style");

        assertThat(propertyElement.isPresent(), is(true));
        assertThat(propertyElement.get().getAttribute("value"), is("1"));
    }

    @Test
    void getPropertyElement_should_return_null_when_given_name_not_found() throws Exception {
        final Optional<Element> propertyElement = XMLUtils.getPropertyElement(getElement(FONT_ELEMENT), "not existing");

        assertThat(propertyElement, is(Optional.empty()));
    }

    @Test
    void getPropertyElement_should_return_null_when_property_element_not_found() throws Exception {
        final Optional<Element> propertyElement = XMLUtils.getPropertyElement(getElement("<font><font_caption></font_caption></font>"), "not existing");

        assertThat(propertyElement, is(Optional.empty()));
    }

    @Test
    void getAttributeByIndex_should_return_attribute_value_of_child_element() throws Exception {
        final Optional<String> attributeValue = XMLUtils.getAttributeByIndex(getElement(FONT_CAPTION_ELEMENT), 5);

        assertThat(attributeValue.isPresent(), is(true));
        assertThat(attributeValue.get(), is("-16777216"));
    }

    @Test
    void getAttributeFromChildElement_should_return_attribute_value_of_child_element() throws Exception {
        final Optional<String> attributeValue = XMLUtils.getAttributeValueFromChildElement(getElement(FONT_CAPTION_ELEMENT), "Font Style");

        assertThat(attributeValue.isPresent(), is(true));
        assertThat(attributeValue.get(), is("1"));
    }

    @Test
    void getAttributeFromChildElement_should_return_null_when_element_name_is_not_existing() throws Exception {
        final Optional<String> attributeValue = XMLUtils.getAttributeValueFromChildElement(getElement(FONT_CAPTION_ELEMENT), "Not Existing");

        assertThat(attributeValue, is(Optional.empty()));
    }

    @Test
    void getAttributeFromElement_should_return_attribute_value() throws Exception {
        final Optional<String> attributeValue = XMLUtils.getAttributeValueFromElement(getElement("<property name=\"Font Style\" value=\"1\"/>"), "Font Style");

        assertThat(attributeValue.isPresent(), is(true));
        assertThat(attributeValue.get(), is("1"));
    }

    @Test
    void getAttributeFromElement_should_return_null_when_attribute_is_not_exisiting() throws Exception {
        final Optional<String> attributeValue = XMLUtils.getAttributeValueFromElement(getElement(FONT_CAPTION_ELEMENT), "Font Style");

        assertThat(attributeValue, is(Optional.empty()));
    }

    @Test
    void createAndAppendElement_should_return_new_element_node_under_given_parent_node() throws Exception {
        final Document document = XMLUtils.createNewDocument();

        final Element parent = document.createElement("root");
        final Element body = XMLUtils.createAndAppendElement(document, "body", parent);

        assertThat(body.getTagName(), is("body"));
        assertThat(((Element) body.getParentNode()).getTagName(), is("root"));
    }

    @Test
    void addBasicProperty() throws Exception {
        final Document document = XMLUtils.createNewDocument();

        final Element element = document.createElement("root");

        final String name = "foo";
        final String value = "bar";
        XMLUtils.addBasicProperty(document, name, value, element);

        final NodeList nodes = element.getChildNodes();
        assertThat(((Element) nodes.item(0)).getTagName(), is("property"));
        assertThat(((Element) nodes.item(0)).getAttribute("name"), is("foo"));
        assertThat(((Element) nodes.item(0)).getAttribute("value"), is("bar"));
    }

    @Test
    void getElementsFromNodeList_should_return_nodes_as_elements() throws Exception {
        final NodeList nodes = getElement(FONT_CAPTION_ELEMENT).getChildNodes();
        final List<Element> elements = XMLUtils.getElementsFromNodeList(nodes);

        assertThat(elements, hasSize(6));
        assertThat(elements.get(0).getAttribute("name"), is("Font Name"));
        assertThat(elements.get(1).getAttribute("name"), is("Font Size"));
        assertThat(elements.get(2).getAttribute("name"), is("Font Style"));
        assertThat(elements.get(3).getAttribute("name"), is("Underline"));
        assertThat(elements.get(4).getAttribute("name"), is("Strikethrough"));
        assertThat(elements.get(5).getAttribute("name"), is("Color"));
    }

    @Test
    void getElementsFromNodeList_should_return_empty_element_list_for_empty_node_list() throws Exception {
        final NodeList nodes = getElement("<property name=\"Font Style\" value=\"1\"/>").getChildNodes();
        final List<Element> elements = XMLUtils.getElementsFromNodeList(nodes);

        assertThat(elements, is(emptyIterable()));
    }

    private Element getElement(final String xmlElementStructure) throws ParserConfigurationException, IOException, SAXException {
        return XMLUtils.readDocument(xmlElementStructure).getDocumentElement();
    }
}
