package org.pdf.forms.gui.commands;

import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableMap;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
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
import org.pdf.forms.model.des.Widget;
import org.pdf.forms.readers.des.DesignerProjectFileReader;
import org.pdf.forms.readers.properties.DesignerPropertiesFile;
import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.GroupWidget;
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
        // draw page
        mainFrame.displayPage(1);
        mainFrame.setTotalNoOfDisplayedPages(mainFrame.getTotalNoOfPages());
        mainFrame.setTitle(designerFileToOpen + " - PDF Forms Designer Version " + version.getVersion());

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
                final Page newPage = convertToFormsPage(changedFiles, page);

                addRadioButtonGroupsToPage(page, newPage);
                addCheckBoxButtonGroupsToPage(page, newPage);

                mainFrame.setCurrentPage(mainFrame.getCurrentPage() + 1);
                addPage(mainFrame.getCurrentPage(), newPage);

                final List<IWidget> widgets = getWidgetsFromPage(page);
                for (final IWidget widget: widgets) {
                    mainFrame.addWidgetToHierarchy(widget);
                }
                newPage.setWidgets(widgets);
            }
        } catch (final Exception e) {
            logger.error("Error opening designer file {}", designerFileToOpen, e);
        }
    }

    private Page convertToFormsPage(
            final Map<String, String> changedFiles,
            final org.pdf.forms.model.des.Page page) {
        final String pageName = page.getPageName().orElse("");
        if (page.isPdfPage()) {
            // PDF page
            final PageData pageData = page.getPageData();
            final String pdfFileToUse = changedFiles.get(pageData.getPdfFileLocation().orElse(""));
            final int pdfPageNumber = pageData.getPdfPageNumber().orElse(1);
            //todo check for skip PDF files
            return new Page(pageName, pdfFileToUse, pdfPageNumber);
        }

        // simple page
        final PageData pageData = page.getPageData();
        final int width = pageData.getWidth().orElse(1);
        final int height = pageData.getHeight().orElse(1);
        return new Page(pageName, width, height);
    }

    private void closePDF() {
        mainFrame.setFormsDocument(null);

        mainFrame.getDesigner().close();

        mainFrame.setCurrentDesignerFileName("");
        mainFrame.setTitle("PDF Forms Designer Version " + version.getVersion());

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

    private List<IWidget> getWidgetsFromPage(final org.pdf.forms.model.des.Page page) {
        return page.getWidget().stream()
                .map(this::createWidgetByType)
                .collect(toUnmodifiableList());
    }

    private IWidget createWidgetByType(final org.pdf.forms.model.des.Widget widget) {
        final int widgetType = getWidgetType(widget);
        //todo: move to WidgetFactory
        if (widgetType == IWidget.GROUP) {
            final GroupWidget groupWidget = new GroupWidget();

            final List<IWidget> widgetsInGroup = new ArrayList<>();
            final List<Widget> widgetInGroupList = widget.getWidgets().getWidget();
            for (final Widget widgetInGroup: widgetInGroupList) {
                widgetsInGroup.add(createWidgetByType(widgetInGroup));
            }
            groupWidget.setWidgetsInGroup(widgetsInGroup);

            return groupWidget;
        }
        return widgetFactory.createWidget(widgetType, widget);
    }

    private int getWidgetType(final Widget widget) {
        return widget.getType()
                .map(type -> IWidget.WIDGET_TYPES.getOrDefault(type.toLowerCase(), IWidget.NONE))
                .orElse(IWidget.NONE);
    }

    private void addPage(
            final int pageNumber,
            final Page newPage) {
        mainFrame.getFormsDocument().addPage(pageNumber, newPage);
        mainFrame.addPageToHierarchyPanel(pageNumber, newPage);
    }

}
