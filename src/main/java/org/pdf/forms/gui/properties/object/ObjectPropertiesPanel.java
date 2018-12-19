/*
* ===========================================
* PDF Forms Designer
* ===========================================
*
* Project Info:  http://pdfformsdesigne.sourceforge.net
* (C) Copyright 2006-2008..
* Lead Developer: Simon Barnett (n6vale@googlemail.com)
*
* 	This file is part of the PDF Forms Designer
*
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


*
* ---------------
* ObjectPropertiesPanel.java
* ---------------
*/
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

    private IDesigner designerPanel;

    private JTabbedPane tabs = new JTabbedPane();

    public ObjectPropertiesPanel(/*DesignerPanel designerPanel*/) {
        setLayout(new BorderLayout());

        //tabs.setBorder(BorderFactory.createBevelBorder(5,Color.yellow,Color.yellow));

        add(tabs);
    }

    public void setDesignerPanel(IDesigner designerPanel) {
        this.designerPanel = designerPanel;
    }

    public void setProperties(Set<IWidget> widgets, int type) {
        removeTabs();

        if (type == IWidget.NONE) {
        	Page page = (Page) widgets.iterator().next();

        	//TODO make it so you can change a PDF size too
        	if(page.getPdfFileLocation() == null){ // its a simple page and not a PDF page so we can change its size
        		PagePanel pagePanel = new PagePanel(designerPanel);
        		pagePanel.setProperties(widgets);

        		tabs.add("Page", pagePanel);
        	}
		} else {
			Map<IWidget, Element> widgetsAndProperties = new HashMap<>();
            for (IWidget widget : widgets) {
                Document properties = widget.getProperties();

                Element objectProperties = (Element) properties.getElementsByTagName("object").item(0);

                widgetsAndProperties.put(widget, objectProperties);
            }
			BindingPanel bindingPanel = new BindingPanel();
			bindingPanel.setDesignerPanel(designerPanel);
			switch (type) {
			case IWidget.TEXT_FIELD: {
                TextFieldFieldPanel textFieldFieldPanel = new TextFieldFieldPanel();
                textFieldFieldPanel.setProperties(widgetsAndProperties);

                SimpleValuePanel simpleValuePanel = new SimpleValuePanel(IWidget.TEXT_FIELD);
                simpleValuePanel.setProperties(widgetsAndProperties);

                bindingPanel.setProperties(widgetsAndProperties);

                tabs.add("Field", textFieldFieldPanel);
                tabs.add("Value", simpleValuePanel);
                tabs.add("Binding", bindingPanel);

                break;
            } case IWidget.BUTTON: {
                SimpleValuePanel simpleValuePanel = new SimpleValuePanel(IWidget.BUTTON);
                simpleValuePanel.setProperties(widgetsAndProperties);

                bindingPanel.setProperties(widgetsAndProperties);

                tabs.add("Value", simpleValuePanel);
                tabs.add("Binding", bindingPanel);

                break;
            } case IWidget.COMBO_BOX: {
                ListFieldPanel fieldPanel = new ListFieldPanel(IWidget.COMBO_BOX);
                fieldPanel.setDesignerPanel(designerPanel);
                fieldPanel.setProperties(widgetsAndProperties);

                ValuePanel valuePanel = new ValuePanel();
                valuePanel.setDesignerPanel(designerPanel);
                valuePanel.setProperties(widgetsAndProperties);

                bindingPanel.setProperties(widgetsAndProperties);

                tabs.add("Field", fieldPanel);
                tabs.add("Value", valuePanel);
                tabs.add("Binding", bindingPanel);

                break;
            } case IWidget.LIST_BOX: {
                ListFieldPanel fieldPanel = new ListFieldPanel(IWidget.LIST_BOX);
                fieldPanel.setDesignerPanel(designerPanel);
                fieldPanel.setProperties(widgetsAndProperties);

                ValuePanel valuePanel = new ValuePanel();
                valuePanel.setDesignerPanel(designerPanel);
                valuePanel.setProperties(widgetsAndProperties);

                bindingPanel.setProperties(widgetsAndProperties);

                tabs.add("Field", fieldPanel);
                tabs.add("Value", valuePanel);
                tabs.add("Binding", bindingPanel);

                break;
            } case IWidget.CHECK_BOX: {
                RadioButtonFieldPanel radioButtonFieldPanel = new RadioButtonFieldPanel();
                radioButtonFieldPanel.setDesignerPanel(designerPanel, type);
                radioButtonFieldPanel.setProperties(widgetsAndProperties);

                ValuePanel valuePanel = new ValuePanel();
                valuePanel.setDesignerPanel(designerPanel);
                valuePanel.setProperties(widgetsAndProperties);

                bindingPanel.setProperties(widgetsAndProperties);

                tabs.add("Field", radioButtonFieldPanel);
                tabs.add("Value", valuePanel);
                tabs.add("Binding", bindingPanel);

                break;
            } case IWidget.RADIO_BUTTON: {
                RadioButtonFieldPanel radioButtonFieldPanel = new RadioButtonFieldPanel();
                radioButtonFieldPanel.setDesignerPanel(designerPanel, type);
                radioButtonFieldPanel.setProperties(widgetsAndProperties);

                ValuePanel valuePanel = new ValuePanel();
                valuePanel.setDesignerPanel(designerPanel);
                valuePanel.setProperties(widgetsAndProperties);

                bindingPanel.setProperties(widgetsAndProperties);

                tabs.add("Field", radioButtonFieldPanel);
                tabs.add("Value", valuePanel);
                tabs.add("Binding", bindingPanel);

                break;
            } case IWidget.IMAGE:

				ImageDrawPanel imageDrawPanel = new ImageDrawPanel();
				imageDrawPanel.setDesignerPanel(designerPanel);
				imageDrawPanel.setProperties(widgetsAndProperties);

				tabs.add("Draw", imageDrawPanel);

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
