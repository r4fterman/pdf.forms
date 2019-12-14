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
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;
import javax.xml.parsers.ParserConfigurationException;

import org.pdf.forms.gui.commands.ImportPdfCommand;
import org.pdf.forms.gui.commands.OpenDesignerFileCommand;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DefaultTransferHandler extends TransferHandler {

    private final Logger logger = LoggerFactory.getLogger(DefaultTransferHandler.class);

    private final IDesigner designerPanel;
    private final IMainFrame mainFrame;
    private final String version;
    private final WidgetFactory widgetFactory;

    DefaultTransferHandler(
            final IDesigner designerPanel,
            final IMainFrame mainFrame,
            final String version,
            final WidgetFactory widgetFactory) {
        this.designerPanel = designerPanel;
        this.mainFrame = mainFrame;
        this.version = version;
        this.widgetFactory = widgetFactory;
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
                if (bestTextFlavor != null) {
                    // this could be a file from a web page being dragged in
                    final Reader r = bestTextFlavor.getReaderForText(transferable);

                    // acquire the text data from the reader.
                    final String textData = readTextData(r);

                    // need to remove all the 0 characters that will appear in the String when importing on Linux
                    final String clearedTextData = removeChar0(textData);

                    // get the URL from the text data
                    final String url = getURL(clearedTextData);

                    // make sure only one url is in the String
                    if (url.indexOf("file:/") != url.lastIndexOf("file:/")) {
                        JOptionPane.showMessageDialog(src, "You may only import 1 file at a time");
                    } else {
                        openFile(url, src);
                    }
                } else if (listFlavor != null) {
                    // this is most likely a file being dragged in
                    final List list = (List) transferable.getTransferData(listFlavor);
                    if (list.size() == 1) {
                        // we can process
                        final File file = (File) list.get(0);
                        openFile(file.getAbsolutePath(), src);
                    } else {
                        JOptionPane.showMessageDialog(src, "You may only import 1 file at a time");
                    }
                }
            } catch (final Exception e) {
                logger.error("Caught exception decoding drag'n'drop transferable", e);
                return false;
            }

            return true;
        }
        return false;
    }

    private void openFile(
            final String file,
            final Component src) {
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
                    new ImportPdfCommand(mainFrame, version, widgetFactory).importPDF(file);
                } else if (isDes) {
                    new OpenDesignerFileCommand(mainFrame, version, widgetFactory).openDesignerFile(file);
                    //} else if (isImage) {
                    //currentCommands.openFile(file);
                } else {
                    JOptionPane.showMessageDialog(src, "You may only import a valid PDF, des file or image");
                }
            }
        }
    }

    private String removeChar0(final String s) {
        final char c = (char) 0;
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
     */
    private String getURL(final String textData) throws ParserConfigurationException, SAXException, IOException {
        if (textData.startsWith("http://") || textData.startsWith("file://")) {
            return textData;
        }

        // its not a url so it must be a file
        final Document doc = XMLUtils.readDocument(textData);
        final Element a = (Element) doc.getElementsByTagName("a").item(0);
        return getHrefAttribute(a);
    }

    /**
     * Acquire text data from a reader. <br/><br/>
     * Firefox this will be some html containing an "a" element with the "href" attribute linking to the to the PDF. <br/><br/>
     * IE a simple one line String containing the URL will be returned
     *
     * @param reader
     *         the reader to read from
     * @return the text data from the reader
     */
    private String readTextData(final Reader reader) {
        final BufferedReader br = new BufferedReader(reader);
        try {
            final StringBuilder builder = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                builder.append(line);
                line = br.readLine();
            }
            return builder.toString();
        } catch (final IOException e) {
            return "";
        } finally {
            closeReader(br);
        }

    }

    private void closeReader(final BufferedReader bufferedReader) {
        try {
            bufferedReader.close();
        } catch (final IOException e) {
            // do nothing
        }
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
