package org.pdf.forms.widgets;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DifferenceEvaluator;

public class IgnoreFontNameDifferenceEvaluator implements DifferenceEvaluator {
    @Override
    public ComparisonResult evaluate(
            final Comparison comparison,
            final ComparisonResult outcome) {
        if (outcome == ComparisonResult.DIFFERENT) {
            final Node controlNode = comparison.getControlDetails().getTarget();
            if (((Attr) controlNode).getOwnerElement() != null) {
                final Element element = ((Attr) controlNode).getOwnerElement();
                final String nameValue = element.getAttribute("name");
                if (nameValue.equals("Font Name")) {
                    return ComparisonResult.SIMILAR;
                }
            }
            System.out.println(outcome.name());
        }
        return outcome;
    }
}
