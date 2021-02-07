package org.pdf.forms.writer;

import static java.util.stream.Collectors.toUnmodifiableMap;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.*;

import org.pdf.forms.fonts.FontHandler;
import org.pdf.forms.gui.IMainFrame;
import org.pdf.forms.model.des.BorderProperties;
import org.pdf.forms.model.des.Borders;
import org.pdf.forms.model.des.DesDocument;
import org.pdf.forms.model.des.JavaScriptContent;
import org.pdf.forms.model.des.LayoutProperties;
import org.pdf.forms.model.des.Widget;
import org.pdf.forms.widgets.CheckBoxWidget;
import org.pdf.forms.widgets.IWidget;
import org.pdf.forms.widgets.RadioButtonWidget;
import org.pdf.forms.widgets.components.PdfCaption;
import org.pdf.forms.writer.PdfDocumentLayout.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RadioCheckField;

public class Writer {

    private final Logger logger = LoggerFactory.getLogger(Writer.class);

    private final Map<String, PdfName> pdfEventMap = Map.of(
            "mouseEnter", PdfName.E,
            "mouseExit", PdfName.X,
            "mouseDown", PdfName.D,
            "mouseUp", PdfName.U,
            "change", PdfName.F,
            "keystroke", PdfName.K
    );

    private final Set<String> fontSubstitutions = new HashSet<>();

    private final IMainFrame mainFrame;
    private final FontHandler fontHandler;
    private final Map<Integer, PdfComponentWriter> componentWriterMap;

    public Writer(
            final IMainFrame mainFrame,
            final FontHandler fontHandler) {
        this.mainFrame = mainFrame;
        this.fontHandler = fontHandler;

        componentWriterMap = Map.of(
                IWidget.TEXT_FIELD, new PdfTextFieldWriter(fontHandler),
                IWidget.TEXT, new PdfTextWriter(fontHandler),
                IWidget.IMAGE, new PdfImageWriter(),
                IWidget.RADIO_BUTTON, new PdfRadioButtonWriter(),
                IWidget.CHECK_BOX, new PdfCheckBoxWriter(fontHandler),
                IWidget.COMBO_BOX, new PdfComboBoxWriter(fontHandler),
                IWidget.LIST_BOX, new PdfListBoxWriter(fontHandler),
                IWidget.BUTTON, new PdfButtonWriter(fontHandler)
        );
    }

    public Set<String> getFontSubstitutions() {
        return fontSubstitutions;
    }

    public void write(
            final File fileToWriteTo,
            final Map<Integer, List<IWidget>> widgetsByPageNumber,
            final DesDocument designerDocument) {
        final List<org.pdf.forms.model.des.Page> pages = designerDocument.getPage();
        final Optional<String> javaScript = getJavaScript(designerDocument.getJavaScript());
        final PdfDocumentLayout pdfDocumentLayout = getPdfDocumentLayout(pages);
        if (pdfDocumentLayout.getPdfPages().isEmpty()) {
            logger.info("write: PDF page list is empty.");
            writeModelToPDF(fileToWriteTo, widgetsByPageNumber, pages, javaScript);
        } else {
            logger.info("write: " + pages.size() + " PDF pages.");
            writeExternalPDFPagesToPDF(fileToWriteTo,
                    widgetsByPageNumber,
                    pages,
                    javaScript,
                    pdfDocumentLayout);
        }
    }

    private void writeModelToPDF(
            final File fileToWriteTo,
            final Map<Integer, List<IWidget>> widgetsByPageNumber,
            final List<org.pdf.forms.model.des.Page> pages,
            final Optional<String> javaScript) {
        // this is just a plain, hand made document
        final Document document = new Document(getPageSize(pages, 1));
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            final PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            final GlobalPdfWriter globalPdfWriter = new GlobalPdfWriter(writer);

            final List<IWidget> widgetsForFirstPage = widgetsByPageNumber.get(0);
            addWidgets(writer, widgetsForFirstPage, document.getPageSize(), 0, globalPdfWriter);

            if (widgetsForFirstPage.isEmpty()) {
                logger.info("Widgets for page 1 are empty.");
                writer.setPageEmpty(false);
            }

            for (int pageNumber = 1; pageNumber < pages.size(); pageNumber++) {
                final int currentPage = pageNumber + 1;
                final Rectangle pageSize = getPageSize(pages, currentPage);
                document.setPageSize(pageSize);

                document.newPage();

                final List<IWidget> widgetsForPage = widgetsByPageNumber.get(pageNumber);
                addWidgets(writer, widgetsForPage, document.getPageSize(), currentPage, globalPdfWriter);

                if (widgetsForPage.isEmpty()) {
                    logger.info("Widget for page {} are empty.", currentPage);
                    writer.setPageEmpty(false);
                }
            }

            javaScript.ifPresent(writer::addJavaScript);
        } catch (IOException | DocumentException e) {
            logger.error("Writing to file {} failed!", fileToWriteTo, e);
        } finally {
            document.close();
        }
    }

    private void writeExternalPDFPagesToPDF(
            final File fileToWriteTo,
            final Map<Integer, List<IWidget>> widgetsByPageNumber,
            final List<org.pdf.forms.model.des.Page> pages,
            final Optional<String> javaScript,
            final PdfDocumentLayout pdfDocumentLayout) {
        // we've got pages imported from other PDF's
        try (FileOutputStream fos = new FileOutputStream(fileToWriteTo)) {
            final PdfReader reader = createPdfReader(pages, pdfDocumentLayout);

            final PdfStamper stamper = new PdfStamper(reader, fos);
            final GlobalPdfWriter globalPdfWriter = new GlobalPdfWriter(stamper);

            for (int pageNumber = 0; pageNumber < pages.size(); pageNumber++) {
                final org.pdf.forms.model.des.Page page = pages.get(pageNumber);

                final int currentPage = pageNumber + 1;

                final List<IWidget> widgetList = widgetsByPageNumber.get(pageNumber);
                if (isPdfPage(page)) {
                    // this page has been imported
                    final AcroFields acroFields = stamper.getAcroFields();
                    acroFields.removeFieldsFromPage(currentPage);

                    addWidgets(stamper.getWriter(),
                            widgetList,
                            reader.getPageSizeWithRotation(currentPage),
                            currentPage,
                            globalPdfWriter);
                } else {
                    // this is a brand new page
                    stamper.insertPage(currentPage, getPageSize(pages, currentPage));

                    addWidgets(stamper.getWriter(),
                            widgetList,
                            getPageSize(pages, currentPage),
                            currentPage,
                            globalPdfWriter);
                }
            }

            javaScript.ifPresent(stamper::addJavaScript);

            stamper.close();
        } catch (IOException | DocumentException e) {
            logger.error("Writing to file {} failed!", fileToWriteTo, e);
        }
    }

    private PdfReader createPdfReader(
            final List<org.pdf.forms.model.des.Page> pages,
            final PdfDocumentLayout pdfDocumentLayout) throws DocumentException, IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //todo: use PdfCopy instead
        final PdfCopyFields pdfCopyFields = new PdfCopyFields(baos);

        for (int i = 0; i < pages.size(); i++) {
            final Page page = pdfDocumentLayout.getPage(i + 1);

            if (page.isPdfPage()) {
                final PdfReader pdfReader = new PdfReader(page.getPdfPath());
                final List<Integer> pageNumbers = List.of(page.getPdfPageNumber());

                pdfCopyFields.addDocument(pdfReader, pageNumbers);
            }
        }

        pdfCopyFields.close();

        return new PdfReader(baos.toByteArray());
    }

    private Optional<String> getJavaScript(final org.pdf.forms.model.des.JavaScriptContent javaScriptContent) {
        final Map<PdfName, String> eventsAndScripts = getEventAndScriptMap(javaScriptContent.getEvents());
        final Collection<String> values = eventsAndScripts.values();
        if (values.isEmpty()) {
            return Optional.empty();
        }

        //todo: what script part should be found here?
        final String script = values.iterator().next();
        if (script == null || script.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(script);
    }

    private boolean isPdfPage(final org.pdf.forms.model.des.Page page) {
        return page.getPdfFileLocation().isPresent();
    }

    private Rectangle getPageSize(
            final List<org.pdf.forms.model.des.Page> pages,
            final int currentPage) {
        final org.pdf.forms.model.des.Page page = pages.get(currentPage - 1);
        final int width = page.getPageData().getWidth().orElse(25);
        final int height = page.getPageData().getHeight().orElse(25);
        return new Rectangle(width, height);
    }

    private void handleButtonGroups(
            final PdfWriter writer,
            final List<IWidget> widgets,
            final Rectangle pageSize,
            final int currentPage,
            final GlobalPdfWriter globalPdfWriter) throws IOException, DocumentException {

        final Map<String, List<IWidget>> radioButtonGroups = new HashMap<>();
        final Map<String, List<IWidget>> checkBoxGroups = new HashMap<>();

        for (final IWidget widget: widgets) {
            if (widget.getType() == IWidget.RADIO_BUTTON || widget.getType() == IWidget.CHECK_BOX) {
                final int widgetType = widget.getType();

                final Map<String, List<IWidget>> buttonGroup;
                final String groupName;
                if (widgetType == IWidget.RADIO_BUTTON) {
                    final RadioButtonWidget rbw = (RadioButtonWidget) widget;
                    groupName = rbw.getRadioButtonGroupName();
                    buttonGroup = radioButtonGroups;
                } else {
                    final CheckBoxWidget cbw = (CheckBoxWidget) widget;
                    groupName = cbw.getCheckBoxGroupName();
                    buttonGroup = checkBoxGroups;
                }

                final List<IWidget> buttonsInGroup = buttonGroup.computeIfAbsent(groupName, k -> new ArrayList<>());
                buttonsInGroup.add(widget);
            }
        }

        radioButtonGroups.putAll(checkBoxGroups);

        for (final Map.Entry<String, List<IWidget>> entry: radioButtonGroups.entrySet()) {
            final String groupName = entry.getKey();
            final List<IWidget> widgetsInGroup = entry.getValue();
            final IWidget testWidget = widgetsInGroup.get(0);

            final int type = testWidget.getType();

            final PdfFormField top;
            if (type == IWidget.RADIO_BUTTON) {
                top = PdfFormField.createRadioButton(writer, true);
            } else {
                top = PdfFormField.createCheckBox(writer);
            }

            top.setFieldName(groupName);
            top.setValueAsName(groupName);

            writeJavaScript(writer, pageSize, currentPage, globalPdfWriter, widgetsInGroup, type, top);

            globalPdfWriter.addAnnotation(top, currentPage);
        }
    }

    private void writeJavaScript(
            final PdfWriter writer,
            final Rectangle pageSize,
            final int currentPage,
            final GlobalPdfWriter globalPdfWriter,
            final List<IWidget> widgetsInGroup,
            final int type,
            final PdfFormField top) throws IOException, DocumentException {
        for (final IWidget widget: widgetsInGroup) {
            final Map<String, String> events = widget.getJavaScript().getEvents();
            if (!events.isEmpty()) {
                writeOutCaption(widget, pageSize, currentPage, globalPdfWriter);

                final AbstractButton value = (AbstractButton) widget.getValueComponent();

                final java.awt.Rectangle valueBounds;
                if (type == IWidget.RADIO_BUTTON) {
                    valueBounds = new java.awt.Rectangle(13, 13);
                } else {
                    java.awt.Rectangle iconBounds = new java.awt.Rectangle(13, 13);
                    final Icon icon = value.getIcon();
                    if (icon != null) {
                        final int iconWidth = icon.getIconWidth();
                        final int iconHeight = icon.getIconHeight();

                        iconBounds = new java.awt.Rectangle(iconWidth, iconHeight);
                    }
                    valueBounds = iconBounds;
                }

                final java.awt.Rectangle actualBounds = value.getBounds();
                final Point actualLocation = widget.getAbsoluteLocationsOfValue();

                actualLocation.y += (actualBounds.height / 2d) - (valueBounds.height / 2d);

                valueBounds.setLocation(actualLocation);
                final Rectangle pdfValueBounds = convertJavaCoordsToPdfCoords(valueBounds, pageSize);

                final RadioCheckField check = new RadioCheckField(writer, pdfValueBounds, getName(widget), "Yes");
                check.setChecked(value.isSelected());

                addBorder(widget, check);

                final PdfFormField field;
                if (type == IWidget.RADIO_BUTTON) {
                    check.setCheckType(RadioCheckField.TYPE_CIRCLE);
                    field = check.getRadioField();
                } else {
                    check.setCheckType(RadioCheckField.TYPE_CROSS);
                    field = check.getCheckField();
                }

                top.addKid(field);

                final Map<PdfName, String> eventsAndScripts = getEventAndScriptMap(events);
                addJavaScriptToFormField(eventsAndScripts, field, writer);
            }
        }
    }

    private void addWidgets(
            final PdfWriter writer,
            final List<IWidget> widgets,
            final Rectangle pageSize,
            final int currentPage,
            final GlobalPdfWriter globalPdfWriter) throws IOException, DocumentException {

        handleButtonGroups(writer, widgets, pageSize, currentPage, globalPdfWriter);

        for (final IWidget widget: widgets) {
            final int type = widget.getType();

            if (type == IWidget.GROUP) {
                addWidgets(writer, widget.getWidgetsInGroup(), pageSize, currentPage, globalPdfWriter);
            } else {
                final PdfFormField field = componentWriterMap.get(type).write(
                        widget,
                        pageSize,
                        currentPage,
                        writer,
                        globalPdfWriter);

                final JavaScriptContent javaScript = widget.getJavaScript();
                final Map<String, String> events = javaScript.getEvents();
                if (!events.isEmpty()) {
                    final Map<PdfName, String> eventsAndScripts = getEventAndScriptMap(events);
                    addJavaScriptToFormField(eventsAndScripts, field, writer);
                }

                globalPdfWriter.addAnnotation(field, currentPage);
            }
        }
    }

    private String getName(final IWidget widget) {
        final String widgetName = widget.getWidgetName();
        if (mainFrame.getWidgetArrays().isWidgetArrayInList(widgetName)) {
            final int arrayNumber = widget.getArrayNumber();
            return widgetName + "[" + arrayNumber + "]";
        }

        return widgetName;
    }

    private void addBorder(
            final IWidget widget,
            final BaseField baseField) {
        final Widget model = widget.getWidgetModel();
        final BorderProperties borderProperties = model.getProperties().getBorder();
        final Borders borders = borderProperties.getBorders();

        final String style = borders.getBorderStyle().orElse("None");

        if ("Solid".equals(style)) {
            baseField.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
        } else if ("Dashed".equals(style)) {
            baseField.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
        } else if ("Beveled".equals(style)) {
            baseField.setBorderStyle(PdfBorderDictionary.STYLE_BEVELED);
        } else if ("None".equals(style)) {
            return;
        } else {
            return;
        }

        final String color = borders.getBorderColor().orElse(String.valueOf(Color.WHITE.getRGB()));
        baseField.setBorderColor(new GrayColor(Integer.parseInt(color)));

        final String width = borders.getBorderWidth().orElse("1");
        baseField.setBorderWidth(Integer.parseInt(width));
    }

    private void addJavaScriptToFormField(
            final Map<PdfName, String> eventsAndScripts,
            final PdfFormField formField,
            final PdfWriter writer) {
        for (final PdfName pdfName: eventsAndScripts.keySet()) {
            final String script = eventsAndScripts.getOrDefault(pdfName, "");
            if (!script.isEmpty()) {
                formField.setAdditionalActions(pdfName, PdfAction.javaScript(script, writer));
            }
        }
    }

    private PdfDocumentLayout getPdfDocumentLayout(final List<org.pdf.forms.model.des.Page> pages) {
        final PdfDocumentLayout pdfDocumentLayout = new PdfDocumentLayout();

        for (final org.pdf.forms.model.des.Page page: pages) {
            page.getPdfFileLocation().ifPresentOrElse(fileLocation -> {
                        // its an imported page
                        page.getPdfPageNumber().ifPresent(pdfPageNumber ->
                                pdfDocumentLayout
                                        .addPage(true, fileLocation, Integer.parseInt(pdfPageNumber))
                        );
                    },
                    () -> {
                        // is a hand made page
                        pdfDocumentLayout.addPage(false);
                    });
        }

        return pdfDocumentLayout;
    }

    private Map<PdfName, String> getEventAndScriptMap(final Map<String, String> eventsInModel) {
        return eventsInModel.entrySet().stream()
                .collect(toUnmodifiableMap(
                        entry -> pdfEventMap.get(entry.getKey()),
                        Map.Entry::getValue
                ));
    }

    private void writeOutCaption(
            final IWidget widget,
            final Rectangle pageSize,
            final int currentPage,
            final GlobalPdfWriter globalPdfWriter) {
        final PdfCaption pdfCaption = widget.getCaptionComponent();
        if (pdfCaption == null) {
            return;
        }

        if (widget.isComponentSplit()) {
            final Widget model = widget.getWidgetModel();
            final LayoutProperties layoutProperties = model.getProperties().getLayout();
            if (layoutProperties.getCaption().isPositionNone()) {
                return;
            }
        }

        final java.awt.Rectangle captionBounds = pdfCaption.getBounds();
        captionBounds.setLocation(widget.getAbsoluteLocationsOfCaption());
        final Rectangle pdfCaptionBounds = convertJavaCoordsToPdfCoords(captionBounds, pageSize);

        // write out caption
        final PdfContentByte pdfWriterContentByte = globalPdfWriter.getContentByte(currentPage);
        pdfWriterContentByte.saveState();
        pdfWriterContentByte.concatCTM(
                1,
                0,
                0,
                1,
                pdfCaptionBounds.getLeft(),
                pdfCaptionBounds.getTop() - captionBounds.height);

        final Font font = pdfCaption.getFont();
        final String fontDirectory = fontHandler.getFontDirectory(font);

        DefaultFontMapper mapper = new DefaultFontMapper();
        mapper.insertDirectory(fontDirectory);

        /*
         * we need to make this erroneous call to awtToPdf to see if an exception is thrown, if it is, it is
         * probably because the font cannot be embedded due to licensing restrictions, so substitute with Helvetica
         */
        try {
            mapper.awtToPdf(font);
        } catch (final Exception e) {
            logger.error("Error writing out caption", e);
            mapper = new DefaultFontMapper();
            fontSubstitutions.add(font.getFontName());
        }

        final Graphics2D graphics2D = pdfWriterContentByte.createGraphics(
                captionBounds.width,
                captionBounds.height,
                mapper,
                true,
                .95f);
        pdfCaption.paint(graphics2D);

        graphics2D.dispose();
        pdfWriterContentByte.restoreState();
    }

    private Rectangle convertJavaCoordsToPdfCoords(
            final java.awt.Rectangle bounds,
            final Rectangle pageSize) {
        final int javaX1 = bounds.x - IMainFrame.INSET;
        final int javaY1 = bounds.y - IMainFrame.INSET;

        final int javaX2 = javaX1 + bounds.width;

        final float pdfY1 = pageSize.getHeight() - javaY1 - bounds.height;
        final float pdfY2 = pdfY1 + bounds.height;

        return new Rectangle(javaX1, pdfY1, javaX2, pdfY2);
    }
}
