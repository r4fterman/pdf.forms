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
 * Commands.java
 * ---------------
 */
package org.pdf.forms.gui.commands;

import static java.util.Map.entry;

import java.awt.Component;
import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ProgressMonitor;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.SwingWorker;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.windows.FileFinder;
import org.pdf.forms.gui.windows.PDFImportChooser;
import org.pdf.forms.utils.DesignerPropertiesFile;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.GroupWidget;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.pdf.forms.widgets.utils.WidgetParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Commands {

    public static final int NEW = 77184; //"NEW".hashCode();
    public static final int OPEN = 2432586;
    public static final int RECENT_OPEN = 159636910;
    public static final int CLOSE = 64218584;
    public static final int IMPORT = -2131466331;
    public static final int RECENT_IMPORT = -1381966327;
    public static final int SAVE_FILE = -1295101186;
    public static final int SAVE_FILE_AS = -668118765;
    public static final int PUBLISH = 482617583;
    public static final int FONT_MANAGEMENT = 1780825651;
    public static final int EXIT = 2142494;

    public static final int INSERT_PAGE = 2079238229;
    public static final int REMOVE_PAGE = 1888835946;

    public static final int ALIGN = 62365413;
    public static final int GROUP = 68091487;
    public static final int UNGROUP = 429566822;
    public static final int BRING_TO_FRONT = 88902514;
    public static final int SEND_TO_BACK = -452041100;
    public static final int BRING_FORWARDS = 1102378619;
    public static final int SEND_BACKWARDS = 122730713;

    public static final int TOOLBARS = -200551336;
    public static final int SCRIPT_EDITOR = 947938145;
    public static final int HIERARCHY = 606773781;
    public static final int LIBRARY = 884191387;
    public static final int PROPERTIES = -440960717;
    public static final int LAYOUT = -2056392918;
    public static final int BORDER = 1964992556;
    public static final int OBJECT = -1970038977;
    public static final int FONT = 2163791;
    public static final int PARAGRAPH = 440916302;

    public static final int WEBSITE = 1942318203;
    public static final int ABOUT = 62073709;

    public static final int ZOOM_IN = 2759635;
    public static final int ZOOM = 608001297;
    public static final int ZOOM_OUT = 1668177090;
    public static final int ADD_SELECTION_TO_LIBRARY = 1778177090;
    public static final int BUG_REPORT = 1888177090;
    public static final int NONE = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(Commands.class);

    private final IMainFrame mainFrame;
    private final String version;

    private final JMenuItem[] recentDesignerDocuments;
    private final JMenuItem[] recentImportedDocuments;
    private final int noOfRecentDocs;

    private final Map<Integer, Command> commandMap;

    public Commands(
            final IMainFrame mainFrame,
            final String version) {
        this.mainFrame = mainFrame;
        this.version = version;

        final File configDir = new File(System.getProperty("user.dir"));
        noOfRecentDocs = DesignerPropertiesFile.getInstance(configDir).getNoRecentDocumentsToDisplay();
        recentDesignerDocuments = new JMenuItem[noOfRecentDocs];
        recentImportedDocuments = new JMenuItem[noOfRecentDocs];

        commandMap = Map.ofEntries(
                entry(NEW, new NewPdfCommand(mainFrame, version)),
                entry(OPEN, new OpenDesignerFileCommand(mainFrame, version)),
                entry(CLOSE, new ClosePdfCommand(mainFrame, version)),
                entry(IMPORT, new ImportPdfCommand(mainFrame, version)),
                entry(SAVE_FILE, new SaveDesignerFileCommand(mainFrame, version)),
                entry(SAVE_FILE_AS, new SaveDesignerFileAsCommand(mainFrame, version)),
                entry(PUBLISH, new PublishPdfCommand(mainFrame)),
                entry(FONT_MANAGEMENT, new FontManagementCommand(mainFrame)),
                entry(EXIT, () -> System.exit(0)),
                entry(ZOOM_IN, new ZoomInCommand(mainFrame)),
                entry(ZOOM_OUT, new ZoomOutCommand(mainFrame)),
                entry(ZOOM, new ZoomCommand(mainFrame)),
                entry(INSERT_PAGE, new InsertPageCommand(mainFrame)),
                entry(REMOVE_PAGE, new RemovePageCommand(mainFrame)),
                entry(ADD_SELECTION_TO_LIBRARY, new AddSelectionToLibraryCommand(mainFrame)),
                entry(GROUP, new GroupCommand(mainFrame)),
                entry(UNGROUP, new UnGroupCommand(mainFrame)),
                entry(WEBSITE, new VisitWebsiteCommand()),
                entry(ABOUT, new ShowAboutPanelCommand(mainFrame)),
                entry(BUG_REPORT, new BugReportCommand(mainFrame))
        );
    }

    public void executeCommand(final int id) {
        commandMap.getOrDefault(id, new NoopCommand()).execute();
    }

    public void recentDocumentsOption(
            final String type,
            final JMenu file) {
        final JMenuItem[] recentDocuments;
        if (type.equals("recentdesfiles")) {
            recentDocuments = recentDesignerDocuments;
        } else {
            // "recentpdffiles"
            recentDocuments = recentImportedDocuments;
        }

        final File configDir = new File(System.getProperty("user.dir"));
        final DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance(configDir);

        final String[] recentDocs = properties.getRecentDocuments(type);
        if (recentDocs == null) {
            return;
        }

        for (int i = 0; i < noOfRecentDocs; i++) {
            if (recentDocs[i] == null) {
                recentDocs[i] = "";
            }

            final String fileNameToAdd = recentDocs[i];
            final String shortenedFileName = getShortenedFileName(fileNameToAdd, File.separator);

            recentDocuments[i] = new JMenuItem(i + 1 + ": " + shortenedFileName);

            if (recentDocuments[i].getText().equals(i + 1 + ": ")) {
                recentDocuments[i].setVisible(false);
            }

            recentDocuments[i].setName(fileNameToAdd);
            recentDocuments[i].addActionListener(e -> {
                final JMenuItem item = (JMenuItem) e.getSource();
                final String fileName = item.getName();

                if (type.equals("recentdesfiles")) {
                    // TODO: handle missing files here
                    openDesignerFile(fileName);
                } else {
                    // "recentpdffiles"
                    final int importType = aquirePDFImportType();

                    importPDF(importType, fileName);
                }
            });

            file.add(recentDocuments[i]);
        }
    }

    private void updateRecentDocuments(
            final String[] recentDocs,
            final String type) {
        if (recentDocs == null) {
            return;
        }

        final JMenuItem[] recentDocuments;
        if (type.equals("recentdesfiles")) {
            recentDocuments = recentDesignerDocuments;
        } else {
            // "recentpdffiles"
            recentDocuments = recentImportedDocuments;
        }

        for (int i = 0; i < recentDocs.length; i++) {
            if (recentDocs[i] != null) {
                final String shortenedFileName = getShortenedFileName(recentDocs[i], File.separator);
                if (recentDocuments[i] == null) {
                    recentDocuments[i] = new JMenuItem();
                }
                recentDocuments[i].setText(i + 1 + ": " + shortenedFileName);
                if (recentDocuments[i].getText().equals(i + 1 + ": ")) {
                    recentDocuments[i].setVisible(false);
                } else {
                    recentDocuments[i].setVisible(true);
                }

                recentDocuments[i].setName(recentDocs[i]);
            }
        }
    }

    String getShortenedFileName(
            final String fileNameToAdd,
            final String fileSeparator) {
        final int maxChars = 30;

        if (fileNameToAdd.length() <= maxChars) {
            return fileNameToAdd;
        }

        final String[] arrayedFilePath = fileNameToAdd.split(fileSeparator);
        final int numberOfTokens = arrayedFilePath.length;

        final String filePathBody = fileNameToAdd.substring(arrayedFilePath[0].length(),
                fileNameToAdd.length() - arrayedFilePath[numberOfTokens - 1].length());

        final StringBuilder builder = new StringBuilder(filePathBody);

        for (int i = numberOfTokens - 2; i > 0; i--) {
            final int start = builder.lastIndexOf(arrayedFilePath[i]);

            final int end = start + arrayedFilePath[i].length();
            builder.replace(start, end, "...");

            if (builder.toString().length() <= maxChars) {
                break;
            }
        }

        return arrayedFilePath[0] + builder.toString() + arrayedFilePath[numberOfTokens - 1];
    }

    private void setPanelsState(final boolean state) {
        mainFrame.setPanelsState(state);
    }

    public void openDesignerFile(final String designerFileToOpen) {
        closePDF();

        setPanelsState(true);

        mainFrame.setCurrentDesignerFileName(designerFileToOpen);

        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();

            final String fileName = mainFrame.getCurrentDesignerFileName();
            final Document designerDocumentProperties;
            if (fileName.startsWith("http:") || fileName.startsWith("file:")) {
                designerDocumentProperties = db.parse(fileName);
            } else {
                designerDocumentProperties = db.parse(new File(fileName));
            }

            final Element root = designerDocumentProperties.getDocumentElement();

            mainFrame.setFormsDocument(new FormsDocument(root));

            final List<Element> pages = XMLUtils.getElementsFromNodeList(root.getElementsByTagName("page"));

            final Map<String, String> changedFiles = getChangedPdfFileLocations(pages);

            for (final Element page : pages) {
                final Optional<String> pageType = XMLUtils.getAttributeFromChildElement(page, "pagetype");
                final Optional<String> pageName = XMLUtils.getAttributeFromChildElement(page, "pagename");

                final Element pageData = (Element) page.getElementsByTagName("pagedata").item(0);

                final Optional<String> value1 = XMLUtils.getAttributeByIndex(pageData, 0);
                final Optional<String> value2 = XMLUtils.getAttributeByIndex(pageData, 1);

                final Page newPage;
                if (pageType.isPresent() && pageType.get().equals("pdfpage")) {
                    // PDF page
                    final String pdfFileToUse = changedFiles.get(value1.get());
                    final int pdfPageNumber = Integer.parseInt(value2.get());
                    //todo check for skip PDF files
                    newPage = new Page(pageName.get(), pdfFileToUse, pdfPageNumber);
                } else {
                    // simple page
                    final int width = Integer.parseInt(value1.get());
                    final int height = Integer.parseInt(value2.get());
                    newPage = new Page(pageName.get(), width, height);
                }

                /* add radio button groups to page */
                addButtonGroupsToPage(page, newPage, IWidget.RADIO_BUTTON);

                /* add check box groups to page */
                addButtonGroupsToPage(page, newPage, IWidget.CHECK_BOX);

                mainFrame.setCurrentPage(mainFrame.getCurrentPage() + 1);

                addPage(mainFrame.getCurrentPage(), newPage);

                final List<IWidget> widgets = getWidgetsFromXMLElement(page);

                for (final IWidget widget : widgets) {
                    mainFrame.addWidgetToHierarchy(widget);
                }

                newPage.setWidgets(widgets);
            }
        } catch (final Exception e) {
            LOGGER.error("Error opening designer file {}", designerFileToOpen, e);
        }

        mainFrame.setCurrentPage(1);

        mainFrame.displayPage(mainFrame.getCurrentPage());

        setTotalPages();

        mainFrame.setTitle(mainFrame.getCurrentDesignerFileName() + " - PDF Forms Designer Version " + version);

        final File configDir = new File(System.getProperty("user.dir"));
        final DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance(configDir);
        properties.addRecentDocument(designerFileToOpen, "recentdesfiles");
        updateRecentDocuments(properties.getRecentDocuments("recentdesfiles"), "recentdesfiles");
    }

    public void importPDF(final String file) {
        final int importType = aquirePDFImportType();
        importPDF(importType, file);
    }

    private int aquirePDFImportType() {
        final PDFImportChooser pic = new PDFImportChooser((Component) mainFrame);
        pic.setVisible(true);

        return pic.getImportType();
    }

    private void importPDF(
            final int importType,
            final String pdfPath) {
        if (importType == PDFImportChooser.IMPORT_NEW) {
            closePDF();

            setPanelsState(true);

            mainFrame.setFormsDocument(new FormsDocument(version));
        }

        try {
            // final JDialog dialog= new JDialog((Frame) mainFrame, true);
            // SwingUtilities.invokeLater(new Runnable() {
            //     public void run() {
            //         JPanel panel = new JPanel();
            //         panel.setLayout(new BorderLayout());
            //         panel.add(new JLabel(("Calculating size of import")));
            //         //dialog.setUndecorated(true);
            //         dialog.add(panel);
            //         dialog.pack();
            //         dialog.setLocationRelativeTo((Component) mainFrame);
            //         dialog.setVisible(true);
            //     }
            // });

            final PdfDecoder pdfDecoder = new PdfDecoder();

            if (pdfPath.startsWith("http:") || pdfPath.startsWith("file:")) {
                pdfDecoder.openPdfFileFromURL(pdfPath);
            } else {
                pdfDecoder.openPdfFile(pdfPath);
            }

            final int pageCount = pdfDecoder.getPageCount();
            // SwingUtilities.invokeLater(new Runnable() {
            //     public void run() {
            //         System.out.println("setting visable = false");
            //         dialog.setVisible(false);
            //     }
            // });

            final ProgressMonitor progressDialog = new ProgressMonitor((Component) mainFrame, "", "", 0, pageCount);
            progressDialog.setMillisToDecideToPopup(0);
            progressDialog.setMillisToPopup(0);
            progressDialog.setNote("Importing page " + 1 + " of " + progressDialog.getMaximum());
            progressDialog.setProgress(0);

            final SwingWorker worker = new SwingWorker() {
                @Override
                public Object construct() {

                    boolean isCancelled;

                    final List<Object[]> pages = new ArrayList<>();
                    int currentLastPage = mainFrame.getTotalNoOfPages();

                    if (importType == PDFImportChooser.IMPORT_NEW) {
                        for (int pdfPageNumber = 1; pdfPageNumber < pageCount + 1; pdfPageNumber++) {
                            currentLastPage++;

                            final Page newPage = new Page("(page " + currentLastPage + ")", pdfPath, pdfPageNumber);

                            final List<IWidget> widgetsOnPage = new ArrayList<>();
                            isCancelled = decodePDFPage(pdfPath, pdfDecoder, progressDialog, pdfPageNumber, newPage, widgetsOnPage);

                            final Object[] properties = {
                                    pdfPageNumber,
                                    newPage,
                                    widgetsOnPage };
                            pages.add(properties);

                            if (isCancelled) {
                                break;
                            }
                        }

                        mainFrame.setCurrentDesignerFileName("Untitled");
                        mainFrame.setTitle("Untitled - PDF Forms Designer Version " + version);

                    } else if (importType == PDFImportChooser.IMPORT_EXISTING) {
                        for (int pdfPageNumber = 1; pdfPageNumber < pageCount + 1; pdfPageNumber++) {
                            currentLastPage++;

                            final Page newPage = new Page("(page " + currentLastPage + ")", pdfPath, pdfPageNumber);

                            final List<IWidget> widgetsOnPage = new ArrayList<>();
                            isCancelled = decodePDFPage(pdfPath, pdfDecoder, progressDialog, pdfPageNumber, newPage, widgetsOnPage);

                            final Object[] properties = {
                                    currentLastPage,
                                    newPage,
                                    widgetsOnPage };
                            pages.add(properties);

                            if (isCancelled) {
                                break;
                            }
                        }
                    }

                    final int finalCurrentLastPage = currentLastPage;

                    EventQueue.invokeLater(() -> {
                        for (final Object[] properties : pages) {
                            final int pageNumber = (Integer) properties[0];
                            final Page newPage = (Page) properties[1];
                            final List<IWidget> widgetsOnPage = (List<IWidget>) properties[2];

                            mainFrame.setCurrentPage(pageNumber);

                            addPage(pageNumber, newPage);

                            for (final IWidget widget : widgetsOnPage) {
                                mainFrame.getDesigner().addWidget(widget);
                            }
                        }

                        setTotalPages();

                        //    mainFrame.setCurrentPage(finalCurrentLastPage);
                        mainFrame.displayPage(mainFrame.getCurrentPage());

                        progressDialog.close();

                    });

                    return null;
                }
            };
            worker.start();

            final File configDir = new File(System.getProperty("user.dir"));
            final DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance(configDir);
            properties.addRecentDocument(pdfPath, "recentpdffiles");
            updateRecentDocuments(properties.getRecentDocuments("recentpdffiles"), "recentpdffiles");

        } catch (final PdfException e) {
            LOGGER.error("Error importing PDF file {}", pdfPath, e);
        }
    }

    private void addButtonGroupsToPage(
            final Element page,
            final Page newPage,
            final int type) {
        final String groupName;
        if (type == IWidget.RADIO_BUTTON) {
            groupName = "radiobuttongroups";
        } else {
            groupName = "checkboxgroups";
        }

        final Element radioButtonGroupsElement = (Element) page.getElementsByTagName(groupName).item(0);

        final List<Element> radioButtonGropusList = XMLUtils.getElementsFromNodeList(radioButtonGroupsElement.getChildNodes());

        for (final Element buttonGroupElement : radioButtonGropusList) {
            final Optional<String> value = XMLUtils.getAttributeFromElement(buttonGroupElement, "buttongroupname");
            final ButtonGroup buttonGroup = new ButtonGroup(type);

            buttonGroup.setName(value.get());

            if (type == IWidget.RADIO_BUTTON) {
                newPage.getRadioButtonGroups().add(buttonGroup);
            } else {
                newPage.getCheckBoxGroups().add(buttonGroup);
            }
        }
    }

    private List<IWidget> getWidgetsFromXMLElement(final Element page) {
        final List<Element> elementsInPage = XMLUtils.getElementsFromNodeList(page.getChildNodes());

        final List<Element> widgetsInPageList = new ArrayList<>();

        for (final Element element : elementsInPage) {
            if (element.getNodeName().equals("widget")) {
                widgetsInPageList.add(element);
            }
        }

        final List<IWidget> widgets = new ArrayList<>();

        for (final Element widgetElement : widgetsInPageList) {
            XMLUtils.getAttributeFromChildElement(widgetElement, "type")
                    .map(type -> IWidget.WIDGET_TYPES.getOrDefault(type.toLowerCase(), IWidget.NONE))
                    .map(widgetType -> {
                        final IWidget widget;
                        // TODO: move to WidgetFactory
                        if (widgetType == IWidget.GROUP) {
                            widget = new GroupWidget();
                            final List<IWidget> widgetsInGroup = getWidgetsFromXMLElement(XMLUtils.getElementsFromNodeList(widgetElement.getElementsByTagName("widgets")).get(0));
                            widget.setWidgetsInGroup(widgetsInGroup);
                        } else {
                            widget = WidgetFactory.createWidget(widgetType, widgetElement);
                        }
                        return widget;
                    })
                    .ifPresent(widgets::add);
        }
        return widgets;
    }

    private void decodePDFPage(
            final PdfDecoder pdfDecoder,
            final String pdfPath,
            final int pdfPageNumber,
            final Page newPage,
            final List<IWidget> widgetsOnPage) {
        try {
            final PdfDecoder decoder = new PdfDecoder();

            if (pdfPath.startsWith("http:") || pdfPath.startsWith("file:")) {
                decoder.openPdfFileFromURL(pdfPath);
            } else {
                decoder.openPdfFile(pdfPath);
            }

            decoder.decodePage(pdfPageNumber);
        } catch (final Exception e) {
            LOGGER.error("Error decoding PDF page {} of file {}", pdfPageNumber, pdfPath, e);
        }

        final PdfPageData pdfPageData = pdfDecoder.getPdfPageData();

        final int pageHeight = pdfPageData.getMediaBoxHeight(pdfPageNumber);
        final int cropHeight = pdfPageData.getCropBoxHeight(pdfPageNumber);

        final int cropX = pdfPageData.getCropBoxX(pdfPageNumber);
        final int cropY = pdfPageData.getCropBoxY(pdfPageNumber);

        /*
         * when parsing a widget we don't want to be updating the display until all widgets are parsed, so add all the widgets to a list
         * so, after parsing, the list can be iterated and the widgets added to the page.
         */
        WidgetParser.parseWidgets(pdfDecoder.getFormRenderer(), newPage, pageHeight, cropHeight, cropX, cropY, mainFrame, widgetsOnPage);

        //newPage.setWidgets(widgetsList);

        pdfDecoder.closePdfFile();
    }

    private void closePDF() {
        mainFrame.setFormsDocument(null);

        mainFrame.getDesigner().close();

        mainFrame.setCurrentDesignerFileName("");
        mainFrame.setTitle("PDF Forms Designer Version " + version);

        mainFrame.setPropertiesCompound(new HashSet<>());
        mainFrame.setPropertiesToolBar(new HashSet<>());

        setPanelsState(false);

        mainFrame.setCurrentPage(0);
    }

    private Map<String, String> getChangedPdfFileLocations(final List<Element> pages) {
        final Set<String> pdfFiles = new HashSet<>();
        for (final Element page : pages) {
            XMLUtils.getPropertyElement(page, "pdffilelocation")
                    .ifPresent(element ->
                            pdfFiles.add(element.getAttributeNode("value").getValue())
                    );
        }

        final Map<String, String> changedFiles = new HashMap<>();
        for (final String fileName : pdfFiles) {
            String newFileName = fileName;

            if (!new File(fileName).exists()) {
                final FileFinder fileFinder = new FileFinder((Component) mainFrame, fileName);
                fileFinder.setVisible(true);
                newFileName = fileFinder.getFileLocation();
            }

            changedFiles.put(fileName, newFileName);
        }

        return changedFiles;
    }

    private void addPage(
            final int pdfPage,
            final Page newPage) {
        mainFrame.getFormsDocument().addPage(pdfPage, newPage);
        mainFrame.addPageToHierarchyPanel(pdfPage, newPage);
    }

    private void setTotalPages() {
        mainFrame.setTotalNoOfDisplayedPages(mainFrame.getTotalNoOfPages());
    }

    private boolean decodePDFPage(
            final String pdfPath,
            final PdfDecoder pdfDecoder,
            final ProgressMonitor progressDialog,
            final int pdfPageNumber,
            final Page newPage,
            final List<IWidget> widgetsOnPage) {
        decodePDFPage(pdfDecoder, pdfPath, pdfPageNumber, newPage, widgetsOnPage);

        if (progressDialog.isCanceled()) {
            return true;
        }

        progressDialog.setProgress(pdfPageNumber);
        progressDialog.setNote("Importing page " + pdfPageNumber + " of " + progressDialog.getMaximum());

        return false;
    }

}
