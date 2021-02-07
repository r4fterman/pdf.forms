package org.pdf.forms.document;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.pdf.forms.widgets.ButtonGroup;
import org.pdf.forms.widgets.IWidget;

public class Page {

    private final List<ButtonGroup> radioButtonGroups = new ArrayList<>();
    private final List<ButtonGroup> checkBoxGroups = new ArrayList<>();

    private final String pageName;

    private List<IWidget> widgets = new ArrayList<>();
    private int height;
    private int width;
    private int pdfPageNumber;
    private String pdfFileLocation;

    private int rotation = 0;

    public Page(
            final String pageName,
            final int width,
            final int height) {
        this.width = width;
        this.height = height;

        this.pageName = pageName;
    }

    public Page(
            final String pageName,
            final String pdfFile,
            final int pdfPage) {
        this.pdfFileLocation = pdfFile;
        this.pdfPageNumber = pdfPage;

        this.pageName = pageName;
    }

    public int getPdfPageNumber() {
        return pdfPageNumber;
    }

    public String getPdfFileLocation() {
        return pdfFileLocation;
    }

    public String getPageName() {
        return pageName;
    }

    public List<IWidget> getWidgets() {
        return widgets;
    }

    public List<ButtonGroup> getRadioButtonGroups() {
        return radioButtonGroups;
    }

    public List<ButtonGroup> getCheckBoxGroups() {
        return checkBoxGroups;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(final int rotation) {
        this.rotation = rotation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Dimension getSize() {
        return new Dimension(width, height);
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public void setSize(final Dimension size) {
        this.width = size.width;
        this.height = size.height;
    }

    public void setWidgets(final List<IWidget> widgets) {
        this.widgets = widgets;
    }

    public Optional<ButtonGroup> getCheckBoxGroup(final String groupName) {
        return checkBoxGroups.stream()
                .filter(group -> group.getName().equals(groupName))
                .findFirst();
    }
}
