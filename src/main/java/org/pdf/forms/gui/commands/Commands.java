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

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jpedal.PdfDecoder;
import org.jpedal.examples.simpleviewer.utils.FileFilterer;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.BrowserLauncher;
import org.jpedal.utils.SwingWorker;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.fonts.FontSelector;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.designer.gui.DesignerCompound;
import org.pdf.forms.gui.windows.AboutPanel;
import org.pdf.forms.gui.windows.FileFinder;
import org.pdf.forms.gui.windows.PDFImportChooser;
import org.pdf.forms.utils.CustomWidgetsFile;
import org.pdf.forms.utils.DesignerPropertiesFile;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.GroupWidget;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.pdf.forms.widgets.utils.WidgetParser;
import org.pdf.forms.writer.Writer;
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

    private final IMainFrame mainFrame;
    private final String version;

    private final JMenuItem[] recentDesignerDocuments;
    private final JMenuItem[] recentImportedDocuments;
    private final int noOfRecentDocs;

    public Commands(
            final IMainFrame mainFrame,
            final String version) {
        this.mainFrame = mainFrame;
        this.version = version;

        noOfRecentDocs = DesignerPropertiesFile.getInstance().getNoRecentDocumentsToDisplay();
        recentDesignerDocuments = new JMenuItem[noOfRecentDocs];
        recentImportedDocuments = new JMenuItem[noOfRecentDocs];
    }

    public void executeCommand(final int id) {
        switch (id) {
            case NEW:
                newPDF(595, 842);
                break;
            case OPEN:
                openDesignerFile();
                break;
            case CLOSE:
                closePDF();
                break;
            case IMPORT:
                importPDF();
                break;
            case SAVE_FILE:
                saveDesignerFile();
                break;
            case SAVE_FILE_AS:
                saveDesignerFileAs();
                break;
            case PUBLISH:
                publishPDF();
                break;
            case FONT_MANAGEMENT:
                fontManagement();
                break;
            case EXIT:
                System.exit(0);
                break;

            // case ZOOM_IN:
            //     zoom(mainFrame.getCurrentScaling() * (3d / 2d));
            //     break;
            // case ZOOM:
            //     zoom(mainFrame.getCurrentSelectedScaling() / 100d);
            //     break;
            // case ZOOM_OUT:
            //     zoom(mainFrame.getCurrentScaling() * (2d / 3d));
            //     break;

            case INSERT_PAGE:
                insertPage(595, 842);
                break;
            case REMOVE_PAGE:
                removePage();
                break;
            // case ADDSELECTIONTOLIBRARY:
            //     addSelectionToLibrary();
            //     break;

            case GROUP:
                group();
                break;
            case UNGROUP:
                ungroup();
                break;

            case WEBSITE:
                visitWebsite();
                break;

            case ABOUT:
                about();
                break;
            // case BUGREPORT:
            //
            //  LinkedHashMap pdfFilesAndSizes = mainFrame.getFormsDocument().getPdfFilesUsed();
            //
            //  LinkedHashMap filesAndSizes = new LinkedHashMap();
            //  Document documentProperties = mainFrame.getFormsDocument().getDocumentProperties();
            //
            // try {
            //  File designerFile = File.createTempFile("bugreport", ".des");
            //  designerFile.deleteOnExit();
            //
            //  writeXML(documentProperties, designerFile.getAbsolutePath());
            //
            //  Double size = Double.valueOf(round((designerFile.length() / 1000d), 1));
            //  System.out.println(designerFile.getAbsolutePath());
            //  filesAndSizes.put("Designer File", size);
            // } catch (IOException e) {
            //  e.printStackTrace();
            // }
            //
            // filesAndSizes.putAll(pdfFilesAndSizes);
            // System.out.println(filesAndSizes);
            //
            // JDialog dialog = new JDialog((Frame) mainFrame, "Bug report", true);
            //
            // BugReportPanel bugReportPanel = new BugReportPanel(filesAndSizes, dialog);
            //
            //  dialog.add(bugReportPanel);
            //  dialog.pack();
            //  dialog.setLocationRelativeTo((Component) mainFrame);
            //  dialog.setVisible(true);
            //
            //  break;
            default:
                break;
        }
    }

    private void about() {
        JOptionPane.showMessageDialog((Component) mainFrame, new AboutPanel(), "About", JOptionPane.PLAIN_MESSAGE);
    }

    private void visitWebsite() {
        try {
            BrowserLauncher.openURL("http://pdfformsdesigne.sourceforge.net");
        } catch (final IOException e1) {
            JOptionPane.showMessageDialog(null, "Error loading webpage");
            //<start-full><start-demo>
            e1.printStackTrace();
            //<end-demo><end-full>
        }

        //      JMenuItem about = new JMenuItem("About");
        //      about.addActionListener(new ActionListener() {
        //          public void actionPerformed(ActionEvent actionEvent) {
        //   JOptionPane.showMessageDialog((Component) mainFrame, new AboutPanel(), "About", JOptionPane.PLAIN_MESSAGE);
        //          }
        //      });
        //      help.add(visitWebSite);
        //      help.add(about);
        //
        //      menubar.add(help);

    }

    private void addSelectionToLibrary() {
        final CustomWidgetsFile customWidgetsFile = CustomWidgetsFile.getInstance();
        boolean finished = false;

        String name = JOptionPane.showInputDialog((Component) mainFrame, "Enter a name for the new component", "New component name",
                JOptionPane.QUESTION_MESSAGE);

        while (!finished) {
            if (name == null) {
                return;
            }

            if (customWidgetsFile.isNameTaken(name)) {
                name = JOptionPane.showInputDialog((Component) mainFrame, "The name you have entered is already taken, please enter another name", "New component name",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                finished = true;
            }
        }

        customWidgetsFile.addCustomWidget(name, mainFrame.getDesigner().getSelectedWidgets());
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

        final DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance();

        final String[] recentDocs = properties.getRecentDocuments(type);
        if (recentDocs == null) {
            return;
        }

        for (int i = 0; i < noOfRecentDocs; i++) {
            if (recentDocs[i] == null) {
                recentDocs[i] = "";
            }

            final String fileNameToAdd = recentDocs[i];
            final String shortenedFileName = getShortenedFileName(fileNameToAdd);

            recentDocuments[i] = new JMenuItem(i + 1 + ": " + shortenedFileName);

            if (recentDocuments[i].getText().equals(i + 1 + ": ")) {
                recentDocuments[i].setVisible(false);
            }

            recentDocuments[i].setName(fileNameToAdd);
            recentDocuments[i].addActionListener(e -> {
                final JMenuItem item = (JMenuItem) e.getSource();
                final String fileName = item.getName();

                if (type.equals("recentdesfiles")) {
                    openDesignerFile(fileName);
                } else { // "recentpdffiles"
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
                final String shortenedFileName = getShortenedFileName(recentDocs[i]);

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

    private String getShortenedFileName(final String fileNameToAdd) {
        final int maxChars = 30;

        if (fileNameToAdd.length() <= maxChars) {
            return fileNameToAdd;
        }

        final StringTokenizer st = new StringTokenizer(fileNameToAdd, "\\/");

        final int noOfTokens = st.countTokens();
        final String[] arrayedFile = new String[noOfTokens];
        for (int i = 0; i < noOfTokens; i++) {
            arrayedFile[i] = st.nextToken();
        }

        final String filePathBody = fileNameToAdd.substring(arrayedFile[0].length(),
                fileNameToAdd.length() - arrayedFile[noOfTokens - 1].length());

        final StringBuilder builder = new StringBuilder(filePathBody);

        for (int i = noOfTokens - 2; i > 0; i--) {
            final int start = builder.lastIndexOf(arrayedFile[i]);

            final int end = start + arrayedFile[i].length();
            builder.replace(start, end, "...");

            if (builder.toString().length() <= maxChars) {
                break;
            }
        }

        return arrayedFile[0] + builder.toString() + arrayedFile[noOfTokens - 1];
    }

    private void fontManagement() {
        final JDialog dialog = new JDialog((Frame) mainFrame, "Font Management", true);
        final FontSelector fs = new FontSelector(mainFrame, dialog);

        dialog.add(fs);
        dialog.pack();
        dialog.setLocationRelativeTo((Frame) mainFrame);
        dialog.setVisible(true);

        mainFrame.updateAvailiableFonts();
    }

    private void ungroup() {
        final IDesigner designerPanel = mainFrame.getDesigner();

        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

        final IWidget gw = selectedWidgets.iterator().next();

        designerPanel.removeSelectedWidgets();

        final List<IWidget> widgetsInGroup = gw.getWidgetsInGroup();
        for (final IWidget widget : widgetsInGroup) {
            designerPanel.addWidget(widget);
        }

        final Set<IWidget> widgets = new HashSet<>(widgetsInGroup);

        designerPanel.setSelectedWidgets(widgets);

        designerPanel.getMainFrame().setPropertiesCompound(widgets);
        designerPanel.getMainFrame().setPropertiesToolBar(widgets);

        designerPanel.repaint();
    }

    private void group() {
        final IDesigner designerPanel = mainFrame.getDesigner();

        final Set<IWidget> selectedWidgets = designerPanel.getSelectedWidgets();

        final List<IWidget> widgetsInGroup = new ArrayList<>();
        final GroupWidget gw = new GroupWidget();

        final List<IWidget> allWidgets = designerPanel.getWidgets();

        for (final IWidget widget : allWidgets) {
            if (selectedWidgets.contains(widget)) {
                widgetsInGroup.add(widget);
            }
        }

        gw.setWidgetsInGroup(widgetsInGroup);
        designerPanel.addWidget(gw);

        designerPanel.removeSelectedWidgets();

        final Set<IWidget> set = new HashSet<>();
        set.add(gw);
        designerPanel.setSelectedWidgets(set);

        designerPanel.repaint();
    }

    private double round(final double number) {
        final double exponential = Math.pow(10, 2);

        double value = number;
        value *= exponential;
        value = Math.round(value);
        value /= exponential;

        return value;
    }

    private void zoom(final double scaling) {
        if (mainFrame.getDesignerCompoundContent() == DesignerCompound.PREVIEW) {

            mainFrame.setCurrentSelectedScaling(round(scaling * 100));

            final DesignerCompound desgnerCompound = mainFrame.getDesignerCompound();
            desgnerCompound.previewZoom(scaling);
        }
        //mainFrame.setScaling(mainFrame.getScaling() * scaling); @scale
    }

    private void publishPDF() {

        File file;
        String fileToSave;
        boolean finished = false;

        while (!finished) {
            final JFileChooser chooser = new JFileChooser();
            chooser.addChoosableFileFilter(new FileFilterer(new String[] {
                    "pdf"
            },
                    "pdf (*.pdf)"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            final int approved = chooser.showSaveDialog(null);
            if (approved == JFileChooser.APPROVE_OPTION) {

                file = chooser.getSelectedFile();
                fileToSave = file.getAbsolutePath();

                if (!fileToSave.endsWith(".pdf")) {
                    fileToSave += ".pdf";
                    file = new File(fileToSave);
                }

                if (file.exists()) {
                    final int n = JOptionPane.showConfirmDialog((Component) mainFrame, "The file already exists, are you sure you wish to overwrite?",
                            "File already exists", JOptionPane.YES_NO_OPTION);

                    if (n == 1) {
                        continue;
                    }
                }

                final Writer writer = new Writer(mainFrame);

                final int noOfPages = mainFrame.getTotalNoOfPages();
                final List[] widgets = new ArrayList[noOfPages];

                final FormsDocument documentProperties = mainFrame.getFormsDocument();

                for (int i = 0; i < noOfPages; i++) {
                    widgets[i] = documentProperties.getPage(i + 1).getWidgets();
                }

                writer.write(file, widgets, documentProperties.getDocumentProperties());

                finished = true;
            } else {
                return;
            }
        }
    }

    private void saveDesignerFileAs() {
        final Document documentProperties = mainFrame.getFormsDocument().getDocumentProperties();
        saveDesignerFileAs(documentProperties);
    }

    private void saveDesignerFile() {
        final Document documentProperties = mainFrame.getFormsDocument().getDocumentProperties();

        final String currentDesignerFileName = mainFrame.getCurrentDesignerFileName();
        if (currentDesignerFileName.equals("Untitled")) {
            // saving for the first time
            saveDesignerFileAs(documentProperties);
        } else {
            // saving an already saved file
            writeXML(documentProperties, currentDesignerFileName);
        }
    }

    private void saveDesignerFileAs(final Document documentProperties) {
        File file;
        String fileToSave;
        boolean finished = false;

        while (!finished) {
            final JFileChooser chooser = new JFileChooser();
            chooser.addChoosableFileFilter(new FileFilterer(new String[] {
                    "des"
            },
                    "des (*.des)"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            final int approved = chooser.showSaveDialog(null);
            if (approved == JFileChooser.APPROVE_OPTION) {

                file = chooser.getSelectedFile();
                fileToSave = file.getAbsolutePath();

                if (!fileToSave.endsWith(".des")) {
                    fileToSave += ".des";
                    file = new File(fileToSave);
                }

                if (file.exists()) {
                    final int n = JOptionPane.showConfirmDialog((Component) mainFrame, "The file already exists, are you sure you wish to overwrite?",
                            "File already exists", JOptionPane.YES_NO_OPTION);

                    if (n == 1) {
                        continue;
                    }
                }

                mainFrame.setCurrentDesignerFileName(fileToSave);

                writeXML(documentProperties, mainFrame.getCurrentDesignerFileName());

                mainFrame.setTitle(fileToSave + " - PDF Forms Designer Version " + version);

                finished = true;
            } else {
                return;
            }
        }
    }

    private void writeXML(
            final Document documentProperties,
            final String fileName) {
        //        try {
        // InputStream stylesheet = this.getClass().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");
        //
        // TransformerFactory transformerFactory = TransformerFactory.newInstance();
        // Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
        // transformer.transform(new DOMSource(documentProperties), new StreamResult(mainFrame.getCurrentDesignerFileName()));
        //        } catch (TransformerException e) {
        // e.printStackTrace();
        //        }

        try {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            // transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            //initialize StreamResult with File object to save to file
            transformer.transform(new DOMSource(documentProperties), new StreamResult(fileName));
        } catch (final TransformerException e) {
            e.printStackTrace();
        }

    }

    private void newPDF(
            final int width,
            final int height) {
        closePDF();

        mainFrame.setCurrentDesignerFileName("Untitled");
        mainFrame.setTitle("Untitled - PDF Forms Designer Version " + version);

        setPanelsState(true);

        mainFrame.setFormsDocument(new FormsDocument(version));

        insertPage(width, height);
    }

    private void setPanelsState(final boolean state) {
        mainFrame.setPanelsState(state);
    }

    private void openDesignerFile() {
        final JFileChooser chooser = new JFileChooser();

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        final String[] des = new String[] {
                "des"
        };
        chooser.addChoosableFileFilter(new FileFilterer(des, "des (*.des)"));

        final int state = chooser.showOpenDialog((Component) mainFrame);

        final File fileToOpen = chooser.getSelectedFile();

        if (fileToOpen != null && state == JFileChooser.APPROVE_OPTION) {
            openDesignerFile(fileToOpen.getAbsolutePath());
        }
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
                final String pageType = XMLUtils.getAttributeFromChildElement(page, "pagetype");
                final String pageName = XMLUtils.getAttributeFromChildElement(page, "pagename");

                final Element pageData = (Element) page.getElementsByTagName("pagedata").item(0);

                final String value1 = XMLUtils.getAttributeByIndex(pageData, 0);
                final int value2 = Integer.parseInt(XMLUtils.getAttributeByIndex(pageData, 1));

                final Page newPage;
                if (pageType.equals("pdfpage")) {
                    // PDF page
                    final String pdfFileToUse = changedFiles.get(value1);
                    //todo check for skiped PDF files
                    newPage = new Page(pageName, pdfFileToUse, value2);
                } else {
                    // simple page
                    newPage = new Page(pageName, Integer.parseInt(value1), value2);
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
            e.printStackTrace();
        }

        mainFrame.setCurrentPage(1);

        mainFrame.displayPage(mainFrame.getCurrentPage());

        setTotalPages();

        mainFrame.setTitle(mainFrame.getCurrentDesignerFileName() + " - PDF Forms Designer Version " + version);

        final DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance();
        properties.addRecentDocument(designerFileToOpen, "recentdesfiles");
        updateRecentDocuments(properties.getRecentDocuments("recentdesfiles"), "recentdesfiles");
    }

    public void importPDF(final String file) {
        final int importType = aquirePDFImportType();
        importPDF(importType, file);
    }

    private void importPDF() {
        // TODO dont allow import of a pdf into a closed document
        final int importType = aquirePDFImportType();

        final JFileChooser chooser = new JFileChooser();

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        final String[] pdf = new String[] {
                "pdf"
        };
        chooser.addChoosableFileFilter(new FileFilterer(pdf, "Pdf (*.pdf)"));

        final int state = chooser.showOpenDialog((Component) mainFrame);
        final File file = chooser.getSelectedFile();
        if (file != null && state == JFileChooser.APPROVE_OPTION) {
            importPDF(importType, file.getAbsolutePath());
        }
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

            final DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance();
            properties.addRecentDocument(pdfPath, "recentpdffiles");
            updateRecentDocuments(properties.getRecentDocuments("recentpdffiles"), "recentpdffiles");

        } catch (final PdfException e) {
            e.printStackTrace();
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
            final String value = XMLUtils.getAttributeFromElement(buttonGroupElement, "buttongroupname");
            final ButtonGroup buttonGroup = new ButtonGroup(type);

            System.out.println("value = " + value);

            buttonGroup.setName(value);

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
            final String widgetType = XMLUtils.getAttributeFromChildElement(widgetElement, "type");

            try {
                final Field field = IWidget.class.getDeclaredField(widgetType);

                final int type = field.getInt(this);

                final IWidget widget;
                if (type == IWidget.GROUP) {
                    widget = new GroupWidget();
                    final List<IWidget> widgetsInGroup = getWidgetsFromXMLElement(XMLUtils.getElementsFromNodeList(widgetElement.getElementsByTagName("widgets")).get(0));
                    widget.setWidgetsInGroup(widgetsInGroup);
                } else {
                    widget = WidgetFactory.createWidget(type, widgetElement);
                }

                widgets.add(widget);

            } catch (final Exception ex) {
                ex.printStackTrace();
            }
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
            e.printStackTrace();
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

    private void removePage() {

        final int noOfPages = mainFrame.getTotalNoOfPages();
        if (noOfPages == 1) {
            JOptionPane.showMessageDialog((Component) mainFrame, "You cannot remove the last page", "Last Page", JOptionPane.ERROR_MESSAGE);

            return;
        }

        mainFrame.getFormsDocument().removePage(mainFrame.getCurrentPage());
        mainFrame.removePageFromHierarchyPanel(mainFrame.getCurrentPage());

        //System.out.println(mainFrame.getCurrentPage() +" "+ mainFrame.getTotalNoOfPages());

        if (mainFrame.getCurrentPage() == noOfPages) {
            mainFrame.setCurrentPage(mainFrame.getCurrentPage() - 1);
        }

        mainFrame.displayPage(mainFrame.getCurrentPage());

        setTotalPages();
    }

    private void insertPage(
            final int width,
            final int height) {
        final Page newPage = new Page("(page " + (mainFrame.getTotalNoOfPages() + 1) + ")", width, height);

        mainFrame.setCurrentPage(mainFrame.getCurrentPage() + 1);

        addPage(mainFrame.getCurrentPage(), newPage);

        mainFrame.displayPage(mainFrame.getCurrentPage());

        setTotalPages();
    }

    private Map<String, String> getChangedPdfFileLocations(final List<Element> pages) {
        final Set<String> pdfFiles = new HashSet<>();
        for (final Element page : pages) {
            final Element fileLocationElement = XMLUtils.getPropertyElement(page, "pdffilelocation");
            if (fileLocationElement != null) {
                final String fileLocation = fileLocationElement.getAttributeNode("value").getValue();
                pdfFiles.add(fileLocation);
            }
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
