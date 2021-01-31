package org.pdf.forms.widgets;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DifferenceEvaluator;

public class PropertyWithIteratorNumberDifferenceEvaluator implements DifferenceEvaluator {

    private final String propertyValue;

    public PropertyWithIteratorNumberDifferenceEvaluator(final String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public ComparisonResult evaluate(
            final Comparison comparison,
            final ComparisonResult outcome) {
        if (outcome == ComparisonResult.DIFFERENT) {
            final Node controlNode = comparison.getControlDetails().getTarget();
            if (controlNode instanceof Attr) {
                final Attr attr = (Attr) controlNode;
                if (isDropDownListName(attr)) {
                    return ComparisonResult.SIMILAR;
                }
            }
        }
        return outcome;
    }

    private boolean isDropDownListName(final Attr attr) {
        if (attr.getName().equals("value")) {
            final String value = attr.getValue();
            return value.startsWith(propertyValue);
        }
        return false;
    }
}
