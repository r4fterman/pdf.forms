package org.pdf.forms.widgets.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pdf.forms.utils.XMLUtils;
import org.pdf.forms.widgets.IWidget;
import org.w3c.dom.Element;

public class WidgetArrays {

    private Map<String, List<IWidget>> widgets = new HashMap<>();

    public boolean isWidgetArrayInList(final String name) {
        return widgets.containsKey(name);
    }

    public void addWidgetToArray(
            final String name,
            final IWidget widget) {
        widgets.computeIfAbsent(name, k -> new ArrayList<>()).add(widget);
    }

    public void removeWidgetFromArray(
            final IWidget widget,
            final String name) {
        final List<IWidget> array = widgets.get(name);
        if (array == null) {
            return;
        }

        array.remove(widget);

        if (array.size() == 1) {
            widgets.remove(name);
        } else {
            for (int i = 0; i < array.size(); i++) {
                final IWidget w = array.get(i);
                final Element objectElement = (Element) w.getProperties().getElementsByTagName("object").item(0);
                final Element bindingElement = (Element) objectElement.getElementsByTagName("binding").item(0);

                final String arrayIndex = String.valueOf(i);
                XMLUtils.getPropertyElement(bindingElement, "Array Number")
                        .ifPresent(arrayNumberElement -> arrayNumberElement.getAttributeNode("value").setValue(arrayIndex));
                w.setObjectProperties(objectElement);
            }
        }
    }

    public int getNextArrayIndex(final String name) {
        final List<IWidget> array = widgets.get(name);
        return array.size() - 1;
    }
}
