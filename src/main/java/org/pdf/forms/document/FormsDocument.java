/*
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 * <p>
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 * <p>
 * This file is part of the PDF Forms Designer
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * <p>
 * <p>
 * <p>
 * ---------------
 * FormsDocument.java
 * ---------------
 */
package org.pdf.forms.document;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.pdf.forms.gui.commands.Version;
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

    public FormsDocument(final Version version) {
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();
            documentProperties = db.newDocument();

            final Element rootElement = documentProperties.createElement("document");
            documentProperties.appendChild(rootElement);

            addVersion(rootElement, version); // todo we really need this to be generated each time the file is saved
            addJavaScript(rootElement);
        } catch (final ParserConfigurationException e) {
            logger.error("Error creating new FormsDocument", e);
        }
    }

    public FormsDocument(final Element loadedRoot) {
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();
            documentProperties = db.newDocument();

            final Node newRoot = documentProperties.importNode(loadedRoot, true);
            documentProperties.appendChild(newRoot);
        } catch (final ParserConfigurationException e) {
            logger.error("Error creating new FormsDocument from element", e);
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

    public List getPages() {
        return pages;
    }

    public Page getPage(final int pageNumber) {
        return pages.get(pageNumber - 1);
    }

    public int getNoOfPages() {
        return pages.size();
    }

    //todo try removing this
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
            final Version version) {
        XMLUtils.addBasicProperty(documentProperties, "version", version.getValue(), rootElement);
    }

    private void addJavaScript(final Element rootElement) {
        final Element javaScriptElement = XMLUtils.createAndAppendElement(documentProperties, "javascript", rootElement);

        final Element mouseEnterElement = XMLUtils.createAndAppendElement(documentProperties, "initialize", javaScriptElement);
        mouseEnterElement.appendChild(documentProperties.createTextNode(""));
    }
}
