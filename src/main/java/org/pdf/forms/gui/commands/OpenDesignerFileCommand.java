package org.pdf.forms.gui.commands;

import static java.util.stream.Collectors.toUnmodifiableMap;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.*;

import org.pdf.forms.document.FormsDocument;
import org.pdf.forms.document.Page;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.gui.windows.FileFinder;
import org.pdf.forms.model.des.CheckBoxGroups;
import org.pdf.forms.model.des.DesDocument;
import org.pdf.forms.model.des.PageData;
import org.pdf.forms.model.des.RadioButtonGroups;
import org.pdf.forms.model.des.Version;
import org.pdf.forms.readers.des.DesignerProjectFileReader;
import org.pdf.forms.readers.properties.DesignerPropertiesFile;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.utils.WidgetFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenDesignerFileCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(OpenDesignerFileCommand.class);

    private final IMainFrame mainFrame;
    private final Version version;
    private final JMenuItem[] recentDesignerDocuments;
    private final WidgetFactory widgetFactory;
    private final DesignerPropertiesFile designerPropertiesFile;

    public OpenDesignerFileCommand(
            final IMainFrame mainFrame,
            final Version version,
            final WidgetFactory widgetFactory,
            final DesignerPropertiesFile designerPropertiesFile) {
        this.mainFrame = mainFrame;
        this.version = version;
        this.widgetFactory = widgetFactory;
        this.designerPropertiesFile = designerPropertiesFile;

        final int noOfRecentDocs = DesignerPropertiesFile.NO_OF_RECENT_DOCS;
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

        mainFrame.setPanelsState(true);

        mainFrame.setCurrentDesignerFileName(designerFileToOpen);

        readDesignerFile(designerFileToOpen);

        mainFrame.setCurrentPage(1);
        mainFrame.displayPage(mainFrame.getCurrentPage());
        mainFrame.setTotalNoOfDisplayedPages(mainFrame.getTotalNoOfPages());
        mainFrame.setTitle(mainFrame.getCurrentDesignerFileName() + " - PDF Forms Designer Version " + version);

        designerPropertiesFile.addRecentDesignerDocument(designerFileToOpen);
        updateRecentDocuments(designerPropertiesFile.getRecentDesignerDocuments());
    }

    private void readDesignerFile(final String designerFileToOpen) {
        try {
            final File desFile = new File(designerFileToOpen);
            final DesDocument desDocument = new DesignerProjectFileReader(desFile).getDesDocument();

            mainFrame.setFormsDocument(new FormsDocument(desDocument));

            final List<org.pdf.forms.model.des.Page> pages = desDocument.getPage();

            final Map<String, String> changedFiles = getChangedPdfFileLocations(pages);
            for (final org.pdf.forms.model.des.Page page: pages) {
                final Page newPage;

                final String pageName = page.getPageName().orElse("");

                final String pageType = page.getPageType();
                if (pageType.equals("pdfpage")) {
                    final PageData pageData = page.getPageData();
                    // PDF page
                    final String pdfFileToUse = changedFiles.get(pageData.getPdfFileLocation().orElse(""));
                    final int pdfPageNumber = pageData.getPdfPageNumber().orElse(1);
                    //todo check for skip PDF files
                    newPage = new Page(pageName, pdfFileToUse, pdfPageNumber);
                } else {
                    final PageData pageData = page.getPageData();
                    // simple page
                    final int width = pageData.getWidth().orElse(1);
                    final int height = pageData.getHeight().orElse(1);
                    newPage = new Page(pageName, width, height);
                }

                addRadioButtonGroupsToPage(page, newPage);
                addCheckBoxButtonGroupsToPage(page, newPage);

                mainFrame.setCurrentPage(mainFrame.getCurrentPage() + 1);

                addPage(mainFrame.getCurrentPage(), newPage);

                //todo: update hierarchy panel
//                final List<Widget> widgets = page.getWidget();
//                for (final Widget widget: widgets) {
//                    mainFrame.addWidgetToHierarchy(widget);
//                }
                //todo: store model on page
//                newPage.setWidgets(widgets);
            }
        } catch (final Exception e) {
            logger.error("Error opening designer file {}", designerFileToOpen, e);
        }
    }

    private void closePDF() {
        mainFrame.setFormsDocument(null);

        mainFrame.getDesigner().close();

        mainFrame.setCurrentDesignerFileName("");
        mainFrame.setTitle("PDF Forms Designer Version " + version);

        mainFrame.setPropertiesCompound(Set.of());
        mainFrame.setPropertiesToolBar(Set.of());

        mainFrame.setPanelsState(false);

        mainFrame.setCurrentPage(0);
    }

    private Map<String, String> getChangedPdfFileLocations(final List<org.pdf.forms.model.des.Page> pages) {
        return pages.stream()
                .map(org.pdf.forms.model.des.Page::getPdfFileLocation)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toUnmodifiableMap(
                        pdfFile -> pdfFile,
                        this::pdfFileName
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

    private void addRadioButtonGroupsToPage(
            final org.pdf.forms.model.des.Page page,
            final Page newPage) {
        final RadioButtonGroups radioButtonGroups = page.getRadioButtonGroups();
        radioButtonGroups.getButtonGroupNames().forEach(buttonGroupName -> {
            final ButtonGroup buttonGroup = new ButtonGroup(IWidget.RADIO_BUTTON);
            buttonGroup.setName(buttonGroupName);
            newPage.getRadioButtonGroups().add(buttonGroup);
        });
    }

    private void addCheckBoxButtonGroupsToPage(
            final org.pdf.forms.model.des.Page page,
            final Page newPage) {
        final CheckBoxGroups checkBoxGroups = page.getCheckBoxGroups();
        checkBoxGroups.getButtonGroupNames().forEach(buttonGroupName -> {
            final ButtonGroup buttonGroup = new ButtonGroup(IWidget.CHECK_BOX);
            buttonGroup.setName(buttonGroupName);
            newPage.getCheckBoxGroups().add(buttonGroup);
        });
    }

    private void updateRecentDocuments(final List<String> recentDocs) {
        if (recentDocs.isEmpty()) {
            return;
        }

        final JMenuItem[] recentDocuments = recentDesignerDocuments;

        for (int i = 0; i < recentDocs.size(); i++) {
            if (recentDocs.get(i) != null) {
                final String shortenedFileName = FileUtil.getShortenedFileName(recentDocs.get(i), File.separator);
                if (recentDocuments[i] == null) {
                    recentDocuments[i] = new JMenuItem();
                }
                recentDocuments[i].setText(i + 1 + ": " + shortenedFileName);
                recentDocuments[i].setVisible(!recentDocuments[i].getText().equals(i + 1 + ": "));
                recentDocuments[i].setName(recentDocs.get(i));
            }
        }
    }

//    private List<IWidget> getWidgetsFromXMLElement(final org.pdf.forms.model.des.Page page) {
//        final List<Element> widgetsInPageList = XMLUtils.getElementsFromNodeList(page.getChildNodes()).stream()
//                .filter(element -> element.getNodeName().equals("widget"))
//                .collect(toUnmodifiableList());
//
//        final List<IWidget> widgets = new ArrayList<>();
//        for (final Element widgetElement: widgetsInPageList) {
//            XMLUtils.getAttributeValueFromChildElement(widgetElement, "type")
//                    .map(type -> IWidget.WIDGET_TYPES.getOrDefault(type.toLowerCase(), IWidget.NONE))
//                    .map(widgetType -> createWidgetByType(widgetElement, widgetType))
//                    .ifPresent(widgets::add);
//        }
//        return widgets;
//    }

//    private IWidget createWidgetByType(
//            final Element widgetElement,
//            final Integer widgetType) {
//        //todo: move to WidgetFactory
//        if (widgetType == IWidget.GROUP) {
//            final GroupWidget widget = new GroupWidget();
//            final List<IWidget> widgetsInGroup = getWidgetsFromXMLElement(XMLUtils
//                    .getElementsFromNodeList(widgetElement.getElementsByTagName("widgets")).get(0));
//            widget.setWidgetsInGroup(widgetsInGroup);
//            return widget;
//        }
//        return widgetFactory.createWidget(widgetType, widgetElement);
//    }

    private void addPage(
            final int pdfPage,
            final Page newPage) {
        mainFrame.getFormsDocument().addPage(pdfPage, newPage);
        mainFrame.addPageToHierarchyPanel(pdfPage, newPage);
    }

}
