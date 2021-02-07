package org.pdf.forms.gui.properties.object;

import java.awt.*;
import java.util.Set;

import javax.swing.*;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.object.binding.BindingPanel;
import org.pdf.forms.gui.properties.object.draw.ImageDrawPanel;
import org.pdf.forms.gui.properties.object.field.CheckBoxFieldPanel;
import org.pdf.forms.gui.properties.object.field.ListFieldPanel;
import org.pdf.forms.gui.properties.object.field.RadioButtonFieldPanel;
import org.pdf.forms.gui.properties.object.field.TextFieldFieldPanel;
import org.pdf.forms.gui.properties.object.page.PagePanel;
import org.pdf.forms.gui.properties.object.value.SimpleValuePanel;
import org.pdf.forms.gui.properties.object.value.ValuePanel;
import org.pdf.forms.widgets.IWidget;

public class ObjectPropertiesPanel extends JPanel {

    private final IDesigner designerPanel;
    private final JTabbedPane tabs;

    ObjectPropertiesPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
        this.tabs = new JTabbedPane();

        setLayout(new BorderLayout());

        add(tabs);
    }

    public void setProperties(
            final Set<IWidget> widgets,
            final int type) {
        removeTabs();

        if (type == IWidget.NONE) {
            final Page page = (Page) widgets.iterator().next();

            //todo: make it so you can change a PDF size too
            if (page.getPdfFileLocation() == null) {
                // its a simple page and not a PDF page so we can change its size
                final PagePanel pagePanel = new PagePanel(designerPanel);
                pagePanel.setProperties(widgets);

                tabs.add("Page", pagePanel);
            }
            return;
        }

        setPropertiesToPanel(type, widgets);
    }

    private void setPropertiesToPanel(
            final int type,
            final Set<IWidget> widgets) {
        switch (type) {
            case IWidget.TEXT_FIELD:
                setTextFieldOptionToPanel(widgets);
                break;
            case IWidget.BUTTON:
                setButtonOptionsToPanel(widgets);
                break;
            case IWidget.COMBO_BOX:
                setComboBoxOptionsToPanel(widgets);
                break;
            case IWidget.LIST_BOX:
                setListBoxOptionsToPanel(widgets);
                break;
            case IWidget.CHECK_BOX:
                setCheckBoxOptionsToPanel(widgets);
                break;
            case IWidget.RADIO_BUTTON:
                setRadioButtonOptionsToPanel(widgets);
                break;
            case IWidget.IMAGE:
                setImageOptionsToPanel(widgets);
                break;
            default:
                break;
        }
    }

    private void setCheckBoxOptionsToPanel(final Set<IWidget> widgets) {
        final CheckBoxFieldPanel fieldPanel = new CheckBoxFieldPanel(designerPanel);
        fieldPanel.setProperties(widgets);

        addFieldValueBindingTabs(fieldPanel, widgets);
    }

    private void setRadioButtonOptionsToPanel(final Set<IWidget> widgets) {
        final RadioButtonFieldPanel fieldPanel = new RadioButtonFieldPanel(designerPanel);
        fieldPanel.setProperties(widgets);

        addFieldValueBindingTabs(fieldPanel, widgets);
    }

    private void setListBoxOptionsToPanel(final Set<IWidget> widgets) {
        final ListFieldPanel fieldPanel = new ListFieldPanel(IWidget.LIST_BOX);
        fieldPanel.setDesignerPanel(designerPanel);
        fieldPanel.setProperties(widgets);

        addFieldValueBindingTabs(fieldPanel, widgets);
    }

    private void setComboBoxOptionsToPanel(final Set<IWidget> widgets) {
        final ListFieldPanel fieldPanel = new ListFieldPanel(IWidget.COMBO_BOX);
        fieldPanel.setDesignerPanel(designerPanel);
        fieldPanel.setProperties(widgets);

        addFieldValueBindingTabs(fieldPanel, widgets);
    }

    private void setTextFieldOptionToPanel(final Set<IWidget> widgets) {
        final TextFieldFieldPanel fieldPanel = new TextFieldFieldPanel();
        fieldPanel.setProperties(widgets);

        final SimpleValuePanel simpleValuePanel = new SimpleValuePanel(IWidget.TEXT_FIELD);
        simpleValuePanel.setProperties(widgets);

        addFieldValueBindingTabs(fieldPanel, widgets);
    }

    private void setButtonOptionsToPanel(final Set<IWidget> widgets) {
        final SimpleValuePanel valuePanel = new SimpleValuePanel(IWidget.BUTTON);
        valuePanel.setProperties(widgets);

        tabs.add("Value", valuePanel);
        tabs.add("Binding", createBindingPanel(widgets));
    }

    private void setImageOptionsToPanel(final Set<IWidget> widgets) {
        final ImageDrawPanel imageDrawPanel = new ImageDrawPanel();
        imageDrawPanel.setDesignerPanel(designerPanel);
        imageDrawPanel.setProperties(widgets);

        tabs.add("Draw", imageDrawPanel);
    }

    private void addFieldValueBindingTabs(
            final Component fieldPanel,
            final Set<IWidget> widgetsAndProperties) {
        tabs.add("Field", fieldPanel);
        tabs.add("Value", createValuePanel(widgetsAndProperties));
        tabs.add("Binding", createBindingPanel(widgetsAndProperties));
    }

    private ValuePanel createValuePanel(final Set<IWidget> widgets) {
        final ValuePanel valuePanel = new ValuePanel(designerPanel);
        valuePanel.setProperties(widgets);
        return valuePanel;
    }

    private BindingPanel createBindingPanel(final Set<IWidget> widgets) {
        final BindingPanel bindingPanel = new BindingPanel(designerPanel.getMainFrame());
        bindingPanel.setProperties(widgets);
        return bindingPanel;
    }

    private void removeTabs() {
        while (tabs.getTabCount() > 0) {
            tabs.removeTabAt(0);
        }
    }
}
