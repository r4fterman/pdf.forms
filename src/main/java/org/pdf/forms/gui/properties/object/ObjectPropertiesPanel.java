package org.pdf.forms.gui.properties.object;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.pdf.forms.document.Page;
import org.pdf.forms.gui.designer.IDesigner;
import org.pdf.forms.gui.properties.object.binding.BindingPanel;
import org.pdf.forms.gui.properties.object.draw.ImageDrawPanel;
import org.pdf.forms.gui.properties.object.field.ListFieldPanel;
import org.pdf.forms.gui.properties.object.field.RadioButtonFieldPanel;
import org.pdf.forms.gui.properties.object.field.TextFieldFieldPanel;
import org.pdf.forms.gui.properties.object.page.PagePanel;
import org.pdf.forms.gui.properties.object.value.SimpleValuePanel;
import org.pdf.forms.gui.properties.object.value.ValuePanel;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ObjectPropertiesPanel extends JPanel {

    private final JTabbedPane tabs = new JTabbedPane();
    private IDesigner designerPanel;

    ObjectPropertiesPanel() {
        setLayout(new BorderLayout());
        add(tabs);
    }

    public void setDesignerPanel(final IDesigner designerPanel) {
        this.designerPanel = designerPanel;
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
        } else {
            final Map<IWidget, Element> widgetsAndProperties = new HashMap<>();
            for (final IWidget widget : widgets) {
                final Document properties = widget.getProperties();
                final Element objectProperties = (Element) properties.getElementsByTagName("object").item(0);
                widgetsAndProperties.put(widget, objectProperties);
            }
            final BindingPanel bindingPanel = new BindingPanel();
            bindingPanel.setDesignerPanel(designerPanel);

            switch (type) {
                case IWidget.TEXT_FIELD: {
                    final TextFieldFieldPanel textFieldFieldPanel = new TextFieldFieldPanel();
                    textFieldFieldPanel.setProperties(widgetsAndProperties);

                    final SimpleValuePanel simpleValuePanel = new SimpleValuePanel(IWidget.TEXT_FIELD);
                    simpleValuePanel.setProperties(widgetsAndProperties);

                    bindingPanel.setProperties(widgetsAndProperties);

                    tabs.add("Field", textFieldFieldPanel);
                    tabs.add("Value", simpleValuePanel);
                    tabs.add("Binding", bindingPanel);

                    break;
                }
                case IWidget.BUTTON: {
                    final SimpleValuePanel simpleValuePanel = new SimpleValuePanel(IWidget.BUTTON);
                    simpleValuePanel.setProperties(widgetsAndProperties);

                    bindingPanel.setProperties(widgetsAndProperties);

                    tabs.add("Value", simpleValuePanel);
                    tabs.add("Binding", bindingPanel);

                    break;
                }
                case IWidget.COMBO_BOX: {
                    final ListFieldPanel fieldPanel = new ListFieldPanel(IWidget.COMBO_BOX);
                    fieldPanel.setDesignerPanel(designerPanel);
                    fieldPanel.setProperties(widgetsAndProperties);

                    final ValuePanel valuePanel = new ValuePanel();
                    valuePanel.setDesignerPanel(designerPanel);
                    valuePanel.setProperties(widgetsAndProperties);

                    bindingPanel.setProperties(widgetsAndProperties);

                    tabs.add("Field", fieldPanel);
                    tabs.add("Value", valuePanel);
                    tabs.add("Binding", bindingPanel);

                    break;
                }
                case IWidget.LIST_BOX: {
                    final ListFieldPanel fieldPanel = new ListFieldPanel(IWidget.LIST_BOX);
                    fieldPanel.setDesignerPanel(designerPanel);
                    fieldPanel.setProperties(widgetsAndProperties);

                    final ValuePanel valuePanel = new ValuePanel();
                    valuePanel.setDesignerPanel(designerPanel);
                    valuePanel.setProperties(widgetsAndProperties);

                    bindingPanel.setProperties(widgetsAndProperties);

                    tabs.add("Field", fieldPanel);
                    tabs.add("Value", valuePanel);
                    tabs.add("Binding", bindingPanel);

                    break;
                }
                case IWidget.CHECK_BOX:
                case IWidget.RADIO_BUTTON: {
                    final RadioButtonFieldPanel radioButtonFieldPanel = new RadioButtonFieldPanel();
                    radioButtonFieldPanel.setDesignerPanel(designerPanel, type);
                    radioButtonFieldPanel.setProperties(widgetsAndProperties);

                    final ValuePanel valuePanel = new ValuePanel();
                    valuePanel.setDesignerPanel(designerPanel);
                    valuePanel.setProperties(widgetsAndProperties);

                    bindingPanel.setProperties(widgetsAndProperties);

                    tabs.add("Field", radioButtonFieldPanel);
                    tabs.add("Value", valuePanel);
                    tabs.add("Binding", bindingPanel);

                    break;
                }
                case IWidget.IMAGE: {
                    final ImageDrawPanel imageDrawPanel = new ImageDrawPanel();
                    imageDrawPanel.setDesignerPanel(designerPanel);
                    imageDrawPanel.setProperties(widgetsAndProperties);

                    tabs.add("Draw", imageDrawPanel);

                    break;
                }
                default:
                    break;
            }
        }
    }

    private void removeTabs() {
        while (tabs.getTabCount() > 0) {
            tabs.removeTabAt(0);
        }
    }
}
