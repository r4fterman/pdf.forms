package org.pdf.forms.document;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.pdf.forms.utils.XMLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FormsDocument {

    private final Logger logger = LoggerFactory.getLogger(FormsDocument.class);
    private final List<Page> pages = new ArrayList<>();

    private Document documentProperties;

    public FormsDocument(final String version) {
        try {
            documentProperties = XMLUtils.createNewDocument();

            final Element rootElement = documentProperties.createElement("document");
            documentProperties.appendChild(rootElement);

            //todo: we really need this to be generated each time the file is saved
            addVersion(rootElement, version);
            addJavaScript(rootElement);
        } catch (final ParserConfigurationException e) {
            logger.error("Error building forms document", e);
        }
    }

    public FormsDocument(final Element loadedRoot) {
        try {
            documentProperties = XMLUtils.createNewDocument();

            final Node newRoot = documentProperties.importNode(loadedRoot, true);
            documentProperties.appendChild(newRoot);
        } catch (final ParserConfigurationException e) {
            logger.error("Error building forms document", e);
        }
    }

    public void movePage(
            final int fromIndex,
            final int toIndex) {
        if (fromIndex > toIndex) {
            final Page objectToMove = pages.get(fromIndex);
            pages.add(toIndex, objectToMove);

            pages.remove(pages.lastIndexOf(objectToMove));
        } else {
            final Page objectToMove = pages.get(fromIndex);
            pages.add(toIndex + 1, objectToMove);

            pages.remove(objectToMove);
        }
    }

    public void addPage(
            final int index,
            final Page page) {
        pages.add(index - 1, page);
    }

    public void removePage(final int index) {
        pages.remove(index - 1);
    }

    public List<Page> getPages() {
        return pages;
    }

    public Page getPage(final int pageNumber) {
        return pages.get(pageNumber - 1);
    }

    public int getNoOfPages() {
        return pages.size();
    }

    //todo: try removing this
    public Document getDocument() {
        return documentProperties;
    }

    public Document getDocumentProperties() {
        final List<Element> pageNodes = XMLUtils.getElementsFromNodeList(documentProperties.getElementsByTagName("page"));

        /* remove all pages from document so we can rebuild */
        for (final Element element : pageNodes) {
            final Node parent = element.getParentNode();
            parent.removeChild(element);
        }

        /* add all pages to the document*/
        for (final Page page : pages) {
            final Document pageProperties = page.getPageProperties();

            final Element pageRoot = pageProperties.getDocumentElement();

            documentProperties.getDocumentElement().appendChild(documentProperties.importNode(pageRoot, true));
        }

        return documentProperties;
    }

    private void addVersion(
            final Element rootElement,
            final String version) {
        XMLUtils.addBasicProperty(documentProperties, "version", version, rootElement);
    }

    private void addJavaScript(final Element rootElement) {
        final Element javaScriptElement = XMLUtils.createAndAppendElement(documentProperties, "javascript", rootElement);

        final Element mouseEnterElement = XMLUtils.createAndAppendElement(documentProperties, "initialize", javaScriptElement);
        mouseEnterElement.appendChild(documentProperties.createTextNode(""));
    }
}
