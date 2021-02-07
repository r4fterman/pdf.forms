package org.pdf.forms.widgets.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pdf.forms.model.des.BindingProperties;
import org.pdf.forms.widgets.IWidget;

public class WidgetArrays {

    private final Map<String, List<IWidget>> widgets = new HashMap<>();

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
                final IWidget remainingWidget = array.get(i);

                final BindingProperties bindingProperties = remainingWidget.getWidgetModel().getProperties().getObject()
                        .getBinding();

                final String arrayIndex = String.valueOf(i);
                bindingProperties.setArrayNumber(arrayIndex);
            }
        }
    }

    public int getNextArrayIndex(final String name) {
        final List<IWidget> array = widgets.get(name);
        return array.size() - 1;
    }
}
