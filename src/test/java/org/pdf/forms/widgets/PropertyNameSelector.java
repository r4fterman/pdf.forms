package org.pdf.forms.widgets;

import java.util.Map;

import javax.xml.namespace.QName;

import org.w3c.dom.Element;
import org.xmlunit.diff.ElementSelector;
import org.xmlunit.diff.ElementSelectors;
import org.xmlunit.util.Nodes;

public class PropertyNameSelector implements ElementSelector {

    @Override
    public boolean canBeCompared(
            final Element controlElement,
            final Element testElement) {
        if (!ElementSelectors.byName.canBeCompared(controlElement, testElement)) {
            return false;
        }

        final Map<QName, String> cAttrs = Nodes.getAttributes(controlElement);
        final Map<QName, String> tAttrs = Nodes.getAttributes(testElement);
        if (cAttrs.size() != tAttrs.size()) {
            return false;
        }

        final String cNameValue = getAttrNameValue(cAttrs);
        final String tNameValue = getAttrNameValue(tAttrs);

        return cNameValue.equals(tNameValue);
    }

    private String getAttrNameValue(final Map<QName, String> attributes) {
        return attributes.entrySet().stream()
                .filter(entry -> entry.getKey().getLocalPart().equals("name"))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse("");
    }
}
