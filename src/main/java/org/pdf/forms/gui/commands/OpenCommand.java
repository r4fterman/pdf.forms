package org.pdf.forms.gui.commands;

import java.awt.Component;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jpedal.examples.simpleviewer.utils.FileFilterer;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.windows.FileFinder;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.GroupWidget;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class OpenCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(OpenCommand.class);

    private final IMainFrame mainFrame;
    private final RecentDocumentFileList recentDocumentFileList;
    private final Version version;

    OpenCommand(
            final IMainFrame mainFrame,
            final RecentDocumentFileList recentDocumentFileList,
            final Version version) {
        this.mainFrame = mainFrame;
        this.recentDocumentFileList = recentDocumentFileList;
        this.version = version;
    }

    @Override
    public void execute() {
        openDesignerFile();
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
            openDesignerFile(fileToOpen);
        }
    }

    private void openDesignerFile(final File designerFileToOpen) {
        closePDF();

        setPanelsState(true);

        mainFrame.setCurrentDesignerFileName(designerFileToOpen.getAbsolutePath());

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
            logger.error("Error reading designer file " + designerFileToOpen, e);
        }

        mainFrame.setCurrentPage(1);

        mainFrame.displayPage(mainFrame.getCurrentPage());

        setTotalPages();

        mainFrame.setTitle(mainFrame.getCurrentDesignerFileName() + " - PDF Forms Designer Version " + version);

        recentDocumentFileList.addToRecentDesignerDocuments(designerFileToOpen);
    }

    private void setPanelsState(final boolean state) {
        mainFrame.setPanelsState(state);
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

            } catch (final Exception e) {
                logger.error("Error getting widgets from xml", e);
            }
        }
        return widgets;
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
}
