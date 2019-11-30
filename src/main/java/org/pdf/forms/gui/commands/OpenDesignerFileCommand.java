package org.pdf.forms.gui.commands;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jpedal.examples.simpleviewer.utils.FileFilterer;
import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.windows.FileFinder;
import org.pdf.forms.utils.DesignerPropertiesFile;
import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.GroupWidget;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OpenDesignerFileCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(OpenDesignerFileCommand.class);

    private final IMainFrame mainFrame;
    private final String version;
    private final JMenuItem[] recentDesignerDocuments;
    private final JMenuItem[] recentImportedDocuments;

    OpenDesignerFileCommand(
            final IMainFrame mainFrame,
            final String version) {
        this.mainFrame = mainFrame;
        this.version = version;

        final File configDir = new File(System.getProperty("user.dir"));
        final int noOfRecentDocs = DesignerPropertiesFile.getInstance(configDir).getNoRecentDocumentsToDisplay();
        recentDesignerDocuments = new JMenuItem[noOfRecentDocs];
        recentImportedDocuments = new JMenuItem[noOfRecentDocs];
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
            logger.error("Error opening designer file {}", designerFileToOpen, e);
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

    private void addPage(
            final int pdfPage,
            final Page newPage) {
        mainFrame.getFormsDocument().addPage(pdfPage, newPage);
        mainFrame.addPageToHierarchyPanel(pdfPage, newPage);
    }

    private void setTotalPages() {
        mainFrame.setTotalNoOfDisplayedPages(mainFrame.getTotalNoOfPages());
    }

    private void setPanelsState(final boolean state) {
        mainFrame.setPanelsState(state);
    }

}
