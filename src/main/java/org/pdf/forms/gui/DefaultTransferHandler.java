/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* This file is part of the PDF Forms Designer
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
* DefaultTransferHandler.java
* ---------------
*/
package org.pdf.forms.gui;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.pdf.forms.gui.commands.Commands;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DefaultTransferHandler extends TransferHandler {

    private final Commands currentCommands;
    private final IDesigner designerPanel;

    public DefaultTransferHandler(
            final Commands currentCommands,
            final IDesigner designerPanel) {
        this.currentCommands = currentCommands;
        this.designerPanel = designerPanel;
    }

    @Override
    public boolean canImport(
            final JComponent dest,
            final DataFlavor[] flavors) {
        return true;
    }

    @Override
    public boolean importData(
            final JComponent src,
            final Transferable transferable) {

        final int widgetToAdd = designerPanel.getWidgetToAdd();

        if (widgetToAdd == IWidget.NONE) {
            final DataFlavor[] flavors = transferable.getTransferDataFlavors();
            DataFlavor listFlavor = null;
            final int lastFlavor = flavors.length - 1;

            // Check the flavors and see if we find one we like.
            // If we do, save it.
            for (int f = 0; f <= lastFlavor; f++) {
                if (flavors[f].isFlavorJavaFileListType()) {
                    listFlavor = flavors[f];
                }
            }

            // Ok, now try to display the content of the drop.
            try {
                final DataFlavor bestTextFlavor = DataFlavor.selectBestTextFlavor(flavors);
                if (bestTextFlavor != null) { // this could be a file from a web page being dragged in
                    final Reader r = bestTextFlavor.getReaderForText(transferable);

                    /* acquire the text data from the reader. */
                    String textData = readTextDate(r);

                    System.out.println("textData = " + textData);

                    /* need to remove all the 0 characters that will appear in the String when importing on Linux */
                    textData = removeChar(textData, (char) 0);

                    /* get the URL from the text data */
                    final String url = getURL(textData);

                    // make sure only one url is in the String
                    if (url.indexOf("file:/") != url.lastIndexOf("file:/")) {
                        JOptionPane.showMessageDialog(src, "You may only import 1 file at a time");
                    } else {
                        openFile(url, src);
                    }

                } else if (listFlavor != null) { // this is most likely a file being dragged in
                    final List list = (List) transferable.getTransferData(listFlavor);
                    //System.out.println("list = " + list);
                    if (list.size() == 1) {
                        // we can process
                        final File file = (File) list.get(0);
                        openFile(file.getAbsolutePath(), src);
                    } else {
                        JOptionPane.showMessageDialog(src, "You may only import 1 file at a time");
                    }
                }
            } catch (final Exception e) {
                //<start-full><start-demo>
                System.out.println("Caught exception decoding transfer:");
                e.printStackTrace();
                //<end-demo><end-full>
                return false;
            }

            return true;
        }
        return false;
    }

    private void openFile(
            final String file,
            final Component src) {

        /*
         * open any default file and selected page
         */
        if (file != null) {

            final File testExists = new File(file);
            boolean isURL = false;
            if (file.startsWith("http:") || file.startsWith("file:")) {
                isURL = true;
            }

            if ((!isURL) && (!testExists.exists())) {
                JOptionPane.showMessageDialog(src, file + '\n' + "does not exist");
            } else if ((!isURL) && (testExists.isDirectory())) {
                JOptionPane.showMessageDialog(src, file + '\n' + "This file is a Directory and cannot be opened");
            } else {

                final boolean isPdf = file.endsWith(".pdf");
                final boolean isDes = file.endsWith(".des");
                final boolean isImage = file.endsWith(".des") || file.endsWith(".tif")
                        || file.endsWith(".tiff") || file.endsWith(".png")
                        || file.endsWith(".jpg") || file.endsWith(".jpeg")
                        || file.endsWith(".gif");

                if (isPdf) {
                    System.out.println("file = " + file);
                    currentCommands.importPDF(file);
                } else if (isDes) {
                    currentCommands.openDesignerFile(file);
                } else if (isImage) {
                    //currentCommands.openFile(file);
                    System.out.println("DefaultTransferHandler.openFile");
                } else {
                    JOptionPane.showMessageDialog(src, "You may only import a valid PDF, des file or image");
                }
            }
        }
    }

    private String removeChar(
            final String s,
            final char c) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) {
                builder.append(s.charAt(i));
            }
        }
        return builder.toString();
    }

    /**
     * Returns the URL from the text data acquired from the transferable object.
     *
     * @param textData
     *         text data acquired from the transferable.
     * @return the URL of the file to open
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private String getURL(final String textData) throws ParserConfigurationException, SAXException, IOException {
        if (!textData.startsWith("http://") && !textData.startsWith("file://")) { // its not a url so it must be a file
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final Document doc = db.parse(new ByteArrayInputStream(textData.getBytes()));

            final Element a = (Element) doc.getElementsByTagName("a").item(0);
            return getHrefAttribute(a);
        }

        return textData;
    }

    /**
     * Acquire text data from a reader. <br/><br/>
     * Firefox this will be some html containing an "a" element with the "href" attribute linking to the to the PDF. <br/><br/>
     * IE a simple one line String containing the URL will be returned
     *
     * @param r
     *         the reader to read from
     * @return the text data from the reader
     * @throws IOException
     */
    private String readTextDate(final Reader r) throws IOException {
        final BufferedReader br = new BufferedReader(r);

        final StringBuilder builder = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            builder.append(line);
            line = br.readLine();
        }
        br.close();

        return builder.toString();
    }

    /**
     * Returns the URL held in the href attribute from an element.
     *
     * @param element
     *         the element containing the href attribute
     * @return the URL held in the href attribute
     */
    private String getHrefAttribute(final Element element) {
        final NamedNodeMap attrs = element.getAttributes();

        final Node nameNode = attrs.getNamedItem("href");
        if (nameNode != null) {
            return nameNode.getNodeValue();
        }

        return null;
    }
}
