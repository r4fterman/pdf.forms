package org.pdf.forms.gui.properties.object;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.w3c.dom.Element;

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

            //TODO make it so you can change a PDF size too
            if (page.getPdfFileLocation() == null) {
                // its a simple page and not a PDF page so we can change its size
                final PagePanel pagePanel = new PagePanel(designerPanel);
                pagePanel.setProperties(widgets);

                tabs.add("Page", pagePanel);
            }
            return;
        }

        final Map<IWidget, Element> widgetsAndProperties = widgets.stream()
                .collect(Collectors.toUnmodifiableMap(
                        widget -> widget,
                        widget -> (Element) widget.getProperties().getElementsByTagName("object").item(0))
                );

        if (type == IWidget.TEXT_FIELD) {
            setTextFieldOptionToPanel(widgetsAndProperties);
        } else if (type == IWidget.BUTTON) {
            setButtonOptionsToPanel(widgetsAndProperties);
        } else if (type == IWidget.COMBO_BOX) {
            setComboBoxOptionsToPanel(widgetsAndProperties);
        } else if (type == IWidget.LIST_BOX) {
            setListBoxOptionsToPanel(widgetsAndProperties);
        } else if (type == IWidget.CHECK_BOX) {
            setCheckBoxOptionsToPanel(widgetsAndProperties);
        } else if (type == IWidget.RADIO_BUTTON) {
            setRadioButtonOptionsToPanel(widgetsAndProperties);
        } else if (type == IWidget.IMAGE) {
            setImageOptionsToPanel(widgetsAndProperties);
        }
    }

    private void setCheckBoxOptionsToPanel(final Map<IWidget, Element> widgetsAndProperties) {
        final CheckBoxFieldPanel fieldPanel = new CheckBoxFieldPanel(designerPanel);
        fieldPanel.setProperties(widgetsAndProperties);

        addFieldValueBindingTabs(fieldPanel, widgetsAndProperties);
    }
    private void setRadioButtonOptionsToPanel(final Map<IWidget, Element> widgetsAndProperties) {
        final RadioButtonFieldPanel fieldPanel = new RadioButtonFieldPanel(designerPanel);
        fieldPanel.setProperties(widgetsAndProperties);

        addFieldValueBindingTabs(fieldPanel, widgetsAndProperties);
    }

    private void setListBoxOptionsToPanel(final Map<IWidget, Element> widgetsAndProperties) {
        final ListFieldPanel fieldPanel = new ListFieldPanel(IWidget.LIST_BOX);
        fieldPanel.setDesignerPanel(designerPanel);
        fieldPanel.setProperties(widgetsAndProperties);

        addFieldValueBindingTabs(fieldPanel, widgetsAndProperties);
    }

    private void setComboBoxOptionsToPanel(final Map<IWidget, Element> widgetsAndProperties) {
        final ListFieldPanel fieldPanel = new ListFieldPanel(IWidget.COMBO_BOX);
        fieldPanel.setDesignerPanel(designerPanel);
        fieldPanel.setProperties(widgetsAndProperties);

        addFieldValueBindingTabs(fieldPanel, widgetsAndProperties);
    }

    private void setTextFieldOptionToPanel(final Map<IWidget, Element> widgetsAndProperties) {
        final TextFieldFieldPanel fieldPanel = new TextFieldFieldPanel();
        fieldPanel.setProperties(widgetsAndProperties);

        final SimpleValuePanel simpleValuePanel = new SimpleValuePanel(IWidget.TEXT_FIELD);
        simpleValuePanel.setProperties(widgetsAndProperties);

        addFieldValueBindingTabs(fieldPanel, widgetsAndProperties);
    }

    private void setButtonOptionsToPanel(final Map<IWidget, Element> widgetsAndProperties) {
        final SimpleValuePanel valuePanel = new SimpleValuePanel(IWidget.BUTTON);
        valuePanel.setProperties(widgetsAndProperties);

        tabs.add("Value", valuePanel);
        tabs.add("Binding", createBindingPanel(widgetsAndProperties));
    }

    private void setImageOptionsToPanel(final Map<IWidget, Element> widgetsAndProperties) {
        final ImageDrawPanel imageDrawPanel = new ImageDrawPanel();
        imageDrawPanel.setDesignerPanel(designerPanel);
        imageDrawPanel.setProperties(widgetsAndProperties);

        tabs.add("Draw", imageDrawPanel);
    }

    private void addFieldValueBindingTabs(
            final Component fieldPanel,
            final Map<IWidget, Element> widgetsAndProperties) {
        tabs.add("Field", fieldPanel);
        tabs.add("Value", createValuePanel(widgetsAndProperties));
        tabs.add("Binding", createBindingPanel(widgetsAndProperties));
    }

    private ValuePanel createValuePanel(final Map<IWidget, Element> widgetsAndProperties) {
        final ValuePanel valuePanel = new ValuePanel(designerPanel);
        valuePanel.setProperties(widgetsAndProperties);
        return valuePanel;
    }

    private BindingPanel createBindingPanel(final Map<IWidget, Element> widgetsAndProperties) {
        final BindingPanel bindingPanel = new BindingPanel();
        bindingPanel.setDesignerPanel(designerPanel);
        bindingPanel.setProperties(widgetsAndProperties);
        return bindingPanel;
    }

    private void removeTabs() {
        while (tabs.getTabCount() > 0) {
            tabs.removeTabAt(0);
        }
    }
}
