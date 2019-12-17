package org.pdf.forms.gui.commands;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableSet;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import org.pdf.forms.Configuration;
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
    private final WidgetFactory widgetFactory;
    private final Configuration configuration;

    public OpenDesignerFileCommand(
            final IMainFrame mainFrame,
            final String version,
            final WidgetFactory widgetFactory,
            final Configuration configuration) {
        this.mainFrame = mainFrame;
        this.version = version;
        this.widgetFactory = widgetFactory;
        this.configuration = configuration;

        final int noOfRecentDocs = DesignerPropertiesFile.getInstance(configuration.getConfigDirectory()).getNoRecentDocumentsToDisplay();
        this.recentDesignerDocuments = new JMenuItem[noOfRecentDocs];
    }

    @Override
    public void execute() {
        selectDesignerFile().ifPresent(file -> openDesignerFile(file.getAbsolutePath()));
    }

    private Optional<File> selectDesignerFile() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.addChoosableFileFilter(new DesFileFilter());
        final int state = chooser.showOpenDialog((Component) mainFrame);
        if (state == JFileChooser.APPROVE_OPTION) {
            return Optional.ofNullable(chooser.getSelectedFile());
        }
        return Optional.empty();
    }

    public void openDesignerFile(final String designerFileToOpen) {
        closePDF();

        setPanelsState(true);

        mainFrame.setCurrentDesignerFileName(designerFileToOpen);

        try {
            final String fileName = mainFrame.getCurrentDesignerFileName();
            final Document designerDocumentProperties;
            if (fileName.startsWith("http:") || fileName.startsWith("file:")) {
                designerDocumentProperties = XMLUtils.readDocumentFromUri(fileName);
            } else {
                designerDocumentProperties = XMLUtils.readDocument(new File(fileName));
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
        mainFrame.setTotalNoOfDisplayedPages(mainFrame.getTotalNoOfPages());
        mainFrame.setTitle(mainFrame.getCurrentDesignerFileName() + " - PDF Forms Designer Version " + version);

        final DesignerPropertiesFile properties = DesignerPropertiesFile.getInstance(configuration.getConfigDirectory());
        properties.addRecentDocument(designerFileToOpen, "recentdesfiles");
        updateRecentDocuments(properties.getRecentDocuments("recentdesfiles"));
    }

    private void closePDF() {
        mainFrame.setFormsDocument(null);

        mainFrame.getDesigner().close();

        mainFrame.setCurrentDesignerFileName("");
        mainFrame.setTitle("PDF Forms Designer Version " + version);

        mainFrame.setPropertiesCompound(Set.of());
        mainFrame.setPropertiesToolBar(Set.of());

        setPanelsState(false);

        mainFrame.setCurrentPage(0);
    }

    private Map<String, String> getChangedPdfFileLocations(final List<Element> pages) {
        final Set<String> pdfFiles = pages.stream()
                .map(page -> XMLUtils.getPropertyElement(page, "pdffilelocation"))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(element -> element.getAttributeNode("value").getValue())
                .collect(toUnmodifiableSet());

        return pdfFiles.stream()
                .collect(toMap(
                        pdfFile -> pdfFile,
                        pdfFile -> pdfFileName(pdfFile)
                ));
    }

    private String pdfFileName(final String pdfFile) {
        if (!new File(pdfFile).exists()) {
            final FileFinder fileFinder = new FileFinder((Component) mainFrame, pdfFile);
            fileFinder.setVisible(true);
            return fileFinder.getFileLocation();
        }
        return pdfFile;
    }

    private void addButtonGroupsToPage(
            final Element page,
            final Page newPage,
            final int type) {
        final String groupName = getGroupName(type);

        final Element radioButtonGroupsElement = (Element) page.getElementsByTagName(groupName).item(0);

        XMLUtils.getElementsFromNodeList(radioButtonGroupsElement.getChildNodes())
                .forEach(buttonGroupElement -> {
                    final ButtonGroup buttonGroup = new ButtonGroup(type);
                    XMLUtils.getAttributeFromElement(buttonGroupElement, "buttongroupname")
                            .ifPresent(buttonGroup::setName);

                    if (type == IWidget.RADIO_BUTTON) {
                        newPage.getRadioButtonGroups().add(buttonGroup);
                    } else {
                        newPage.getCheckBoxGroups().add(buttonGroup);
                    }
                });
    }

    private String getGroupName(final int type) {
        if (type == IWidget.RADIO_BUTTON) {
            return "radiobuttongroups";
        }
        return "checkboxgroups";
    }

    private void updateRecentDocuments(final String[] recentDocs) {
        if (recentDocs == null) {
            return;
        }

        final JMenuItem[] recentDocuments = recentDesignerDocuments;

        for (int i = 0; i < recentDocs.length; i++) {
            if (recentDocs[i] != null) {
                final String shortenedFileName = FileUtil.getShortenedFileName(recentDocs[i], File.separator);
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

    private List<IWidget> getWidgetsFromXMLElement(final Element page) {
        final List<Element> widgetsInPageList = XMLUtils.getElementsFromNodeList(page.getChildNodes()).stream()
                .filter(element -> element.getNodeName().equals("widget"))
                .collect(toUnmodifiableList());

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
                            widget = widgetFactory.createWidget(widgetType, widgetElement);
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

    private void setPanelsState(final boolean state) {
        mainFrame.setPanelsState(state);
    }

}
