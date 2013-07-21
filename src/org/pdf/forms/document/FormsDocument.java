/**
 * ===========================================
 * PDF Forms Designer
 * ===========================================
 *
 * Project Info:  http://pdfformsdesigne.sourceforge.net
 * (C) Copyright 2006-2008..
 * Lead Developer: Simon Barnett (n6vale@googlemail.com)
 *
 * 	This file is part of the PDF Forms Designer
 *
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


 *
 * ---------------
 * FormsDocument.java
 * ---------------
 */
package org.pdf.forms.document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.pdf.forms.gui.designer.Designer;
import org.pdf.forms.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FormsDocument {

    private List pages = new ArrayList();
    private Document documentProperties;

    public FormsDocument() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            documentProperties = db.newDocument();

            Element rootElement = documentProperties.createElement("document");
            documentProperties.appendChild(rootElement);

            addVersion(rootElement); // todo we really need this to be generated each time the file is saved
            addJavaScript(rootElement);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FormsDocument(Element loadedRoot) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            documentProperties = db.newDocument();

//            Element rootElement = documentProperties.createElement("document");
//            documentProperties.appendChild(rootElement);

            Node newRoot = documentProperties.importNode(loadedRoot, true);
            documentProperties.appendChild(newRoot);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void movePage(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            Object objectToMove = pages.get(fromIndex);
            pages.add(toIndex, objectToMove);

            pages.remove(pages.lastIndexOf(objectToMove));
        } else {
            toIndex++;
            Object objectToMove = pages.get(fromIndex);
            pages.add(toIndex, objectToMove);

            pages.remove(pages.indexOf(objectToMove));
        }
    }

    public LinkedHashMap getPdfFilesUsed() {
        LinkedHashMap pdfFilesUsed = new LinkedHashMap();

        for (Iterator it = pages.iterator(); it.hasNext(); ) {
            Page page = (Page) it.next();

            String pdfFileLocation = page.getPdfFileLocation();

            if (pdfFileLocation != null && pdfFilesUsed.get(pdfFileLocation) == null) {
                File file = new File(pdfFileLocation);
                Double size = new Double(round((file.length() / 1000d), 1));

                pdfFilesUsed.put(pdfFileLocation, size);
            }
        }

        return pdfFilesUsed;
    }

    private double round(double number, int decPlaces) {
        double exponential = Math.pow(10, decPlaces);

        number *= exponential;
        number = Math.round(number);
        number /= exponential;

        return number;
    }

    public void addPage(int index, Page page) {
        pages.add(index - 1, page);
    }

    public void removePage(int index) {
        pages.remove(index - 1);
    }

    public List getPages() {
        return pages;
    }

    public Page getPage(int pageNumber) {
        return (Page) pages.get(pageNumber - 1);
    }

    public int getNoOfPages() {
        return pages.size();
    }

    //todo try removing this
    public Document getDocument() {
        return documentProperties;
    }

    public Document getDocumentProperties() {

        List pageNodes = XMLUtils.getElementsFromNodeList(documentProperties.getElementsByTagName("page"));

        /** remove all pages from document so we can rebuild */
        for (Iterator it = pageNodes.iterator(); it.hasNext(); ) {
            Element element = (Element) it.next();
            Node parent = element.getParentNode();
            parent.removeChild(element);
        }

//        List radioButtonGroupsList = XMLUtils.getElementsFromNodeList(documentProperties.getElementsByTagName("radio_button_groups"));
//
//        /** remove any radio button groups from the document */
//        for (Iterator it = radioButtonGroupsList.iterator(); it.hasNext();) {
//            Element element = (Element) it.next();
//            Node parent = element.getParentNode();
//            parent.removeChild(element);
//        }

        /** add all pages to the document*/
        for (Iterator it = pages.iterator(); it.hasNext(); ) {
            Page page = (Page) it.next();

            Document pageProperties = page.getPageProperties();

            Element pageRoot = pageProperties.getDocumentElement();

            documentProperties.getDocumentElement().appendChild(documentProperties.importNode(pageRoot, true));
        }

        /** add radio button groups to the document */

//            try {
//                StringWriter sw = new StringWriter();
//                InputStream stylesheet = this.getClass().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");
//                TransformerFactory transformerFactory = TransformerFactory.newInstance();
//                Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
//                transformer.transform(new DOMSource(documentProperties), new StreamResult(sw));
//                System.out.println(sw.toString());
//            } catch (TransformerException e) {
//                e.printStackTrace();
//            }

        return documentProperties;
    }

    private void addVersion(Element rootElement) {
        XMLUtils.addBasicProperty(documentProperties, "version", Designer.version, rootElement);
    }

    private void addJavaScript(Element rootElement) {
        Element javaScriptElement = XMLUtils.createAndAppendElement(documentProperties, "javascript", rootElement);

        Element mouseEnterElement = XMLUtils.createAndAppendElement(documentProperties, "initialize", javaScriptElement);
        mouseEnterElement.appendChild(documentProperties.createTextNode(""));
    }
}
